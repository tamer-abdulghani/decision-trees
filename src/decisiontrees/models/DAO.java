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
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Tamer
 */
public class DAO {

    private static String url = "jdbc:mysql://localhost/titanic_project";
    private static String user = "root";
    private static String pass = "12345678";

    private Connection conn;

    public DAO() {
        try {
            conn = DriverManager.getConnection(url, user, pass);

        } catch (SQLException e) {
            System.out.println("Error Connecting: " + e);
        }
    }

    /*
     * charName empty when you want to return all rows
     */
    public DataSet extractTrainingData(ArrayList<String> charasList) {
        DataSet trainingSet = new DataSet();

        /*
        Loading characteristic from database.
         */
        ArrayList<Characteristic> listOfCharas = extractListOfCharacteristics(charasList, true);
        trainingSet.setCharas(listOfCharas);

        /*
        Loading rows from database.
         */
        ArrayList<Row> listOfRows = extractListOfRows(listOfCharas);
        trainingSet.setRows(listOfRows);

        /**
         * Set possible values for each characteristic if it is categorical type
         */
        trainingSet.updateCharactersticsPossibleValues();

        return trainingSet;
    }

    public TestDataSet extractTestingData(ArrayList<String> charasList) {
        TestDataSet testingSet = new TestDataSet();

        /*
        Loading characteristic from database.
         */
        ArrayList<Characteristic> listOfCharas = extractListOfCharacteristics(charasList, false);
        testingSet.setCharas(listOfCharas);

        /*
        Loading rows from database.
         */
        ArrayList<Row> listOfRows = extractListOfRows(listOfCharas);
        testingSet.setRows(listOfRows);

        return testingSet;
    }

    public void loadDatasources() {
        try {
            String sql = "SELECT * FROM datasources";
            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sql);

            while (result.next()) {
                System.out.println("DataSrouces: " + result.getInt(1) + ", " + result.getString(2));
            }

        } catch (SQLException e) {
            System.out.println("Error Listing Charasterstics: " + e);
        }
    }

    private ArrayList<Characteristic> extractListOfCharacteristics(ArrayList<String> charasList, boolean isTraining) {

        ArrayList<Characteristic> listOfCharas = new ArrayList<>();

        try {
            String sql = "SELECT * FROM characteristics";

            if (isTraining) {
                /*
                loading only training dataset.
                 */
                sql += " WHERE dataSourceId = " + 1 + " ";
            } else {

                /*
                loading only testing dataset + gender submission
                 */
                sql += " WHERE dataSourceId != " + 1 + " ";
            }

            if (charasList != null) {
                /*
                loading specific charateristic from the charasList parameter.
                 */
                sql += " AND nameChar in ('" + String.join("','", charasList) + "','Survived')";
                System.out.println("" + sql);
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

    private ArrayList<Row> extractListOfRows(ArrayList<Characteristic> listOfCharas) {
        ArrayList<Row> allRows = new ArrayList<>();
        ArrayList<Integer> nullableRows = new ArrayList<>();

        if (listOfCharas != null && listOfCharas.size() > 0) {
            try {
                String sql = "SELECT * FROM rows";

                List<String> selectedCharasIDs = new ArrayList<String>();
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
