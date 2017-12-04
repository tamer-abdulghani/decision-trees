/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Tamer
 */
public class DAO {

    private static final String url = "jdbc:mysql://localhost/project_titanic";
    private static final String user = "root";
    private static final String pass = "12345678";

    private Connection conn;

    public DAO() {
        try {
            conn = DriverManager.getConnection(url, user, pass);

        } catch (SQLException e) {
            System.out.println("Error Connecting: " + e);
        }
    }

    /**
     * This method extract the Training Data Set from database.
     *
     * @param charasList a list of characteristics that we want to extract from
     * the database
     * @param datasourceId a number represent the datasourceId in database that
     * refer to training dataset not the testing.
     * @return DataSet object contains a list of characteristics and a list of
     * rows.
     */
    public DataSet extractTrainingData(List<String> charasList, int datasourceId) {

        DataSet trainingSet = new DataSet("Training Data Set");

        /*
        Loading training characteristics from database.
         */
        List<Characteristic> listOfCharas = extractListOfCharacteristics(charasList, datasourceId);
        trainingSet.setCharas(listOfCharas);

        /*
        Loading rows from database.
         */
        List<Row> listOfRows = extractListOfRows(listOfCharas);
        trainingSet.setRows(listOfRows);

        /**
         * Set possible values for each characteristic if it is categorical type
         */
        trainingSet.updateCharactersticsPossibleValues();

        return trainingSet;
    }

    /**
     * This method extract the Testing Data Set from database.
     *
     * @param charasList a list of characteristics that we want to extract from
     * the database
     * @param datasourceId a number represent the datasourceId in database that
     * refer to testing dataset not the training.
     * @return DataSet object contains a list of characteristics and a list of
     * rows.
     */
    public TestingDataSet extractTestingData(List<String> charasList, int datasourceId) {
        TestingDataSet testingSet = new TestingDataSet("Testing Data Set");

        /*
        Loading testing characteristics from database.
         */
        List<Characteristic> listOfCharas = extractListOfCharacteristics(charasList, datasourceId);
        testingSet.setCharas(listOfCharas);

        /*
        Loading rows from database related to the testing Characterstics
         */
        List<Row> listOfRows = extractListOfRows(listOfCharas);
        testingSet.setRows(listOfRows);

        /**
         * Set possible values for each characteristic if it is categorical type
         */
        testingSet.updateCharactersticsPossibleValues();

        return testingSet;
    }

    private ArrayList<Characteristic> extractListOfCharacteristics(List<String> charasList, int datasourceId) {

        ArrayList<Characteristic> listOfCharas = new ArrayList<>();

        try {
            String sql = "SELECT * FROM characteristics";

            /*
                loading dataset based on datasourceId, either training characterstics or testing characterstics.
             */
            sql += " WHERE dataSourceId = " + datasourceId + " ";

            if (charasList != null) {
                /*
                loading specific charateristic from the charasList parameter.
                 */
                sql += " AND nameChar in ('" + String.join("','", charasList) + "')";
                //System.out.println("" + sql);
            }

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sql);

            while (result.next()) {
                /*
                reading from result
                 */
                int idChar = result.getInt(1);
                String nameChar = result.getString(2);
                String typeChar = result.getString(3);

                CharacteristicType type = CharacteristicType.Boolean;

                switch (typeChar.toLowerCase()) {
                    case "numerical":
                        type = CharacteristicType.Numerical;
                        break;
                    case "textual":
                        type = CharacteristicType.Textual;
                        break;
                    case "boolean":
                        type = CharacteristicType.Boolean;
                        break;
                }

                listOfCharas.add(new Characteristic(idChar, nameChar, type));
            }

        } catch (SQLException e) {
            System.out.println("Error loading characterstics" + e);
        }

        return listOfCharas;
    }

    private List<Row> extractListOfRows(List<Characteristic> listOfCharas) {
        List<Row> allRows = new ArrayList<>();
        List<Integer> nullableRows = new ArrayList<>();

        if (listOfCharas != null && listOfCharas.size() > 0) {
            try {
                String sql = "SELECT * FROM tuples";

                List<String> selectedCharasIDs;
                selectedCharasIDs = listOfCharas.stream().map(e -> "" + e.getId()).collect(Collectors.toList());

                /*
                loading only specific characterstics 
                 */
                sql += " WHERE CharId in (" + String.join(",", selectedCharasIDs) + ")";

                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(sql);

                while (result.next()) {
                    /*
                    Reading rows from result
                     */
                    int rowId = result.getInt(1);
                    int charId = result.getInt(2);
                    String value = result.getString(3);

                    Characteristic c = listOfCharas.stream().filter(x -> x.getId() == charId).collect(Collectors.toList()).get(0);

                    /*
                    checking if the value equals null ==> mark this row as nullable row.
                     */
                    if (value.equals("")) {
                        nullableRows.add(rowId);
                    } else {
                        /*
                            if rows not yet loaded to the list ==> add 
                         */
                        if (!allRows.stream().map(e -> e.getRowId()).collect(Collectors.toList()).contains(rowId)) {
                            allRows.add(new Row(rowId));
                        }

                        /*
                             get and update the row with selected characterstic value.
                         */
                        Row row = allRows.stream().filter(e -> e.getRowId() == rowId).collect(Collectors.toList()).get(0);
                        row.insertNewValue(c, value);
                    }
                }

            } catch (SQLException e) {
                System.out.println("Error loading Rows: " + e);
            }
        }

        /*
        delete from the result every row that is exist in the nullable rows list, because it contains null values.
         */
        for (int i = 0; i < nullableRows.size(); i++) {
            int nullRow = nullableRows.get(i);
            if (allRows.stream().filter(x -> x.getRowId() == nullRow).collect(Collectors.toList()).size() > 0) {
                Row r = allRows.stream().filter(x -> x.getRowId() == nullRow).collect(Collectors.toList()).get(0);
                if (allRows.contains(r)) {
                    allRows.remove(r);
                }
            }
        }

        return allRows;
    }

}
