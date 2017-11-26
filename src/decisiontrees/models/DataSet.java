/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Tamer
 */
public class DataSet {

    private String name;
    private ArrayList<Characteristic> charas;
    private ArrayList<Row> rows;

    public DataSet() {
        this.charas = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    public SingleCharacteristicTree createCharacteristicTree(Characteristic c) {

        Characteristic target = this.charas.stream().filter(x -> x.getName().toLowerCase().equals("survived")).findFirst().get();

        SingleCharacteristicTree tree = new SingleCharacteristicTree(c, target, rows);

        return tree;
    }

    public ArrayList<SingleCharacteristicTree> generateAllPossibleTrees() {
        ArrayList<SingleCharacteristicTree> allTreesList = new ArrayList<>();
        Characteristic target = this.charas.stream().filter(x -> x.getName().toLowerCase().equals("survived")).findFirst().get();

        List<Characteristic> listCategoricalCharas = this.charas
                .stream()
                .filter(x -> x.getName().toLowerCase().equals("sex")
                || x.getName().toLowerCase().equals("pclass")
                || x.getName().toLowerCase().equals("sibsp")
                || x.getName().toLowerCase().equals("parch")
                )
                .collect(Collectors.toList());

        for (Characteristic c : listCategoricalCharas) {
            SingleCharacteristicTree tree = new SingleCharacteristicTree(c, target, rows);
            allTreesList.add(tree);
        }

        return allTreesList;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the charas
     */
    public ArrayList<Characteristic> getCharas() {
        return charas;
    }

    /**
     * @param charas the charas to set
     */
    public void setCharas(ArrayList<Characteristic> charas) {
        this.charas = charas;
    }

    /**
     * @return the rows
     */
    public ArrayList<Row> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(ArrayList<Row> rows) {
        this.rows = rows;
    }

}
