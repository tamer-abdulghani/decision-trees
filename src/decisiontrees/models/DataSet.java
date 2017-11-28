/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.models;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;

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

    /**
     * Checking the values ranges for each characteristic and check if the
     * distinct values count is greater less 10, then it is a Categorical
     * Characteristic, otherwise we can consider this is NOT categorical
     * Characteristic,
     */
    public void updateCharactersticsPossibleValues() {
        for (Characteristic c : charas) {
            ArrayList<Value> values = new ArrayList<>();
            for (Row r : rows) {
                values.add(r.getValuesMap().get(c));
            }

            List<Value> distinctValues = values.stream().distinct().collect(Collectors.toList());
            if (distinctValues.size() < 10) {
                c.setCategorical(true);
                c.setPossibleValues(distinctValues);
            } else {
                c.setCategorical(false);
            }
        }

        /*
        for (Characteristic c : charas) {
            System.out.println("Characteristic : " + c.getName() + ", is Categorical : " + c.getCategorical());
            for (Value v : c.getPossibleValues()) {
                System.out.println("" + v.toString());
            }
            System.out.println("*********************");
        }
         */
    }

    /**
     * create single characteristic tree belongs to only one characteristic
     *
     * @param profile specific characteristic
     * @return SingleCharacteristicTree Object
     */
    public SingleCharacteristicTree createSingleCharacteristicTree(Characteristic profile) {

        Characteristic target = this.charas.stream().filter(x -> x.getName().toLowerCase().equals("survived")).findFirst().get();

        SingleCharacteristicTree tree = new SingleCharacteristicTree(profile, target, rows);

        return tree;
    }

    /**
     * create list of single characteristic trees with all possible
     * characteristics
     *
     * @return Array List of SingleCharacteristicTree Objects
     */
    public ArrayList<SingleCharacteristicTree> generateAllPossibleTrees() {
        ArrayList<SingleCharacteristicTree> allTreesList = new ArrayList<>();
        Characteristic target = this.charas.stream().filter(x -> x.getName().toLowerCase().equals("survived")).findFirst().get();

        List<Characteristic> listCategoricalCharas = this.charas
                .stream()
                .filter(x -> x.getName().toLowerCase().equals("sex")
                || x.getName().toLowerCase().equals("pclass")
                || x.getName().toLowerCase().equals("sibsp")
                || x.getName().toLowerCase().equals("parch")
                || x.getName().toLowerCase().equals("embarked")
                )
                .collect(Collectors.toList());

        for (Characteristic c : listCategoricalCharas) {
            SingleCharacteristicTree tree = new SingleCharacteristicTree(c, target, rows);
            allTreesList.add(tree);
        }

        return allTreesList;
    }

    /**
     * @param wekaFilePath the file path of weka .arff file
     * @return the decision tree of this algorithm, then you need just to print
     * Classifier Object.
     */
    public Classifier createDecisionTreeC45(String wekaFilePath) throws FileNotFoundException, IOException, Exception {

        BufferedReader reader = new BufferedReader(new FileReader(wekaFilePath));
        Instances train = new Instances(reader);
        train.setClassIndex(train.numAttributes() - 1);
        Classifier cls = new J48();
        cls.buildClassifier(train);
        return cls;
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
