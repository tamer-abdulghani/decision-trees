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
        DataSet set = new DataSet();

        ArrayList<Characteristic> listOfCharas = extractListOfCharacteristics(charasList, true);
        set.setCharas(listOfCharas);
        ArrayList<Row> listOfRows = extractListOfRows(listOfCharas);
        set.setRows(listOfRows);

        return set;
    }

    public TestDataSet extractTestingData(ArrayList<String> charasList) {
        TestDataSet set = new TestDataSet();

        ArrayList<Characteristic> listOfCharas = extractListOfCharacteristics(charasList, false);
        set.setCharas(listOfCharas);
        ArrayList<Row> listOfRows = extractListOfRows(listOfCharas);
        set.setRows(listOfRows);

        return set;
    }

    public void testNotNullValue(List<String> list) {

    }

    public void createTrainingRow() {

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

    /*
     * charName empty when you want to return all rows
     */
    public void listRows(String charName) {

    }

    private ArrayList<Characteristic> extractListOfCharacteristics(ArrayList<String> charasList, boolean isTraining) {

        ArrayList<Characteristic> listOfCharas = new ArrayList<>();

        try {
            String sql = "SELECT * FROM characteristics";

            if (isTraining) {
                sql += " WHERE dataSourceId = " + 1 + " ";
            } else {
                sql += " WHERE dataSourceId != " + 1 + " ";
            }

            if (charasList != null) {
                sql += " AND nameChar in (" + String.join(",", charasList) + ")";
            }

            Statement stmt = conn.createStatement();
            ResultSet result = stmt.executeQuery(sql);

            while (result.next()) {
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
        }

        return listOfCharas;
    }

    private ArrayList<Row> extractListOfRows(ArrayList<Characteristic> listOfCharas) {
        ArrayList<Row> rows = new ArrayList<>();
        ArrayList<Integer> nullableRows = new ArrayList<>();

        if (listOfCharas != null && listOfCharas.size() > 0) {
            try {
                String sql = "SELECT * FROM rows";

                List<String> a = new ArrayList<String>();
                a = listOfCharas.stream().map(e -> "" + e.getId()).collect(Collectors.toList());

                sql += " WHERE CharId in (" + String.join(",", a) + ")";

                System.out.println("" + sql);
                Statement stmt = conn.createStatement();
                ResultSet result = stmt.executeQuery(sql);

                while (result.next()) {
                    int rowId = result.getInt(1);
                    int charId = result.getInt(2);
                    String value = result.getString(3);

                    Characteristic c = listOfCharas.stream().filter(x -> x.getId() == charId).collect(Collectors.toList()).get(0);

                    if (!value.equals("")) {
                        if (!rows.stream().map(e -> e.getRowId()).collect(Collectors.toList()).contains(rowId)) {
                            rows.add(new Row(rowId));
                        }

                        Row row = rows.stream().filter(e -> e.getRowId() == rowId).collect(Collectors.toList()).get(0);

                        row.insertNewValue(c, value);
                    } else {
                        nullableRows.add(rowId);
                    }
                }

            } catch (SQLException e) {
                System.out.println("Error Listing Rows: " + e);
            }
        }

        for (int i = 0; i < nullableRows.size(); i++) {
            int d = nullableRows.get(i);
            if (rows.stream().filter(x -> x.getRowId() == d).collect(Collectors.toList()).size() > 0) {
                Row r = rows.stream().filter(x -> x.getRowId() == d).collect(Collectors.toList()).get(0);
                if (rows.contains(r)) {
                    rows.remove(r);
                }
            }
        }

        /*
        for (Row r : rows) {
            for (Map.Entry<Characteristic, Value> el : r.getValuesMap().entrySet()) {
                System.out.println("" + el.getKey().getName() + ":" + el.getValue());
            }
        }
         */
        return rows;
    }

}
