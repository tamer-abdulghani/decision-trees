/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.models;

import decisiontrees.helpers.WekaHelperModel;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Tamer
 */
public class DataSet {

    private String name;
    private List<Characteristic> charas;
    private List<Row> rows;

    public DataSet(String name) {
        this.name = name;
        this.charas = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    /**
     * Checking the values ranges for each characteristic and check if the
     * distinct values count is less 10, then it is a Categorical
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
     * Create single characteristic tree belongs to only one characteristic
     *
     * @param profile characteristic that represent the profile.
     * @param target characteristic that represent the target class (here is
     * "Survived").
     * @return SingleCharacteristicTree with HashMap containing Pairs
     * ProfileValue and TargetValue
     */
    public SingleCharacteristicTree createSingleCharacteristicTree(Characteristic profile, Characteristic target) {

        SingleCharacteristicTree tree = new SingleCharacteristicTree(profile, target);

        // helper hashmap which contains the value of profile and coresponding target value with the count number
        /*
        3.0->false:372
             true:119
        1.0->false:80
             true:134
        2.0->false:97
             true:87
         */
        Map<Value, Map<Value, Integer>> map = new HashMap<>();
        for (Row r : this.rows) {
            Value profileValue = r.getValuesMap().get(profile);
            Value targetValue = r.getValuesMap().get(target);

            if (!map.containsKey(profileValue)) {
                map.put(profileValue, new HashMap<>());
            }
            if (map.get(profileValue).containsKey(targetValue)) {
                int oldCount = map.get(profileValue).get(targetValue);
                map.get(profileValue).replace(targetValue, oldCount + 1);
            } else {
                map.get(profileValue).put(targetValue, 1);
            }

        }

        /*
        for (Map.Entry<Value, Map<Value, Integer>> el : list.entrySet()) {
            for (Map.Entry<Value, Integer> el2 : el.getValue().entrySet()) {
                System.out.println("" + el.getKey() + "->" + el2.getKey() + ":" + el2.getValue());
            }
        }
         */
        Map<Value, Value> mapProfileTarget = new HashMap<>();
        for (Map.Entry<Value, Map<Value, Integer>> ele : map.entrySet()) {

            mapProfileTarget.put(
                    ele.getKey(),
                    ele.getValue().entrySet().stream().max((x, y) -> x.getValue() > y.getValue() ? 1 : -1).get().getKey()
            );

            // Means from the list that we build (profile , (target,count) ), give me the maximum value correspnd to specific target
            // System.out.println(ele.getKey() + ":" + ele.getValue().entrySet().stream().max((x, y) -> x.getValue() > y.getValue() ? 1 : -1).get().getValue());
        }
        tree.setProfileTargetMap(mapProfileTarget);

        return tree;
    }

    /**
     * Create list of single characteristic trees with all possible
     * characteristics
     *
     * @param target characteristic that represent the target class (here is
     * "Survived").
     * @return Array List of SingleCharacteristicTree Objects
     */
    public List<SingleCharacteristicTree> generateAllPossibleTrees(Characteristic target) {
        List<SingleCharacteristicTree> allTreesList = new ArrayList<>();

        List<Characteristic> listCategoricalCharas = this.charas
                .stream()
                .filter(x -> x.getName().toLowerCase().equals("sex")
                || x.getName().toLowerCase().equals("pclass")
                || x.getName().toLowerCase().equals("sibsp")
                || x.getName().toLowerCase().equals("parch")
                || x.getName().toLowerCase().equals("embarked")
                )
                .collect(Collectors.toList());

        for (Characteristic profile : listCategoricalCharas) {
            SingleCharacteristicTree tree = createSingleCharacteristicTree(profile, target);
            allTreesList.add(tree);
        }

        return allTreesList;
    }

    /**
     * @return C4.5 Decision Tree through J48 Weka Classifier Object.
     */
    public WekaHelperModel createDecisionTreeC45() throws FileNotFoundException, IOException, Exception {

        FastVector attributesList = new FastVector(4);
        int classIndex = -1;
        int index = 0;
        for (Characteristic c : this.charas) {

            FastVector vector = new FastVector(c.getPossibleValues().size());
            for (Value v : c.getPossibleValues()) {
                vector.add(v.toString());
            }
            Attribute attribute = new Attribute(c.getName(), vector);
            attributesList.add(attribute);
            if (c.getName().toLowerCase().equals("survived")) {
                classIndex = index;
            }
            index++;
        }

        Instances trainDataSet = new Instances("TrainingDataSet", attributesList, this.rows.size());
        trainDataSet.setClassIndex(classIndex);

        for (Row r : this.rows) {
            Instance instance = new DenseInstance(r.getValuesMap().size());
            for (Map.Entry<Characteristic, Value> a : r.getValuesMap().entrySet()) {
                System.out.println("" + a.getKey().getName().toString());
                Attribute a1 = (Attribute) attributesList.stream().filter(x -> ((Attribute) x).name().toLowerCase().equals(a.getKey().getName().toLowerCase())).findFirst().get();

                /*
                *   Set value of the Attribute (for example: 'Sex') and the Value of this row (for example: 'male')
                 */
                instance.setValue(a1, a.getValue().toString());
            }
            trainDataSet.add(instance);
        }

        J48 cls = new J48();
        cls.buildClassifier(trainDataSet);
        return new WekaHelperModel(trainDataSet, cls);
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
    public List<Characteristic> getCharas() {
        return charas;
    }

    /**
     * @param charas the charas to set
     */
    public void setCharas(List<Characteristic> charas) {
        this.charas = charas;
    }

    /**
     * @return the rows
     */
    public List<Row> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public String evaluateWekaModel(WekaHelperModel wekaModel, Instances testDataSet) {
        Evaluation eTest = null;
        try {

            eTest = new Evaluation(wekaModel.getTrainDataSet());
            eTest.evaluateModel(wekaModel.getClassifier(), testDataSet);
            return eTest.toSummaryString();

        } catch (Exception ex) {
            Logger.getLogger(DataSet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
