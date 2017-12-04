/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.models;

import java.util.Map;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 *
 * @author Tamer
 */
public class TestingDataSet extends DataSet {

    public TestingDataSet(String name) {
        super(name);
    }

    public float assessingQualityOfTree(SingleCharacteristicTree tree) {
        float correctAnswers = 0;
        float wrongAnswers = 0;

        for (Row r : this.getRows()) {
            Value actualValue = r.getValuesMap().get(tree.getTarget());
            for (Map.Entry<Characteristic, Value> el : r.getValuesMap().entrySet()) {

                if (el.getKey().equals(tree.getProfile())) {
                    //System.out.println("" + el.getKey().getName() + ":" + el.getValue() + ": actual : " + actualValue + " : tree : " + tree.getTargetValue(el.getValue()));
                    Value predictedValue = tree.getTargetValue(el.getValue());
                    //System.out.println(el.getKey().getType().toString() + "____" + predictedValue + ":" + actualValue + " ==" + predictedValue.equals(actualValue));
                    if (predictedValue.equals(actualValue)) {
                        correctAnswers++;
                    } else {
                        wrongAnswers++;
                    }
                }
            }
        }
        System.out.println("" + correctAnswers);
        System.out.println("" + wrongAnswers);
        float propotion = ((float) (correctAnswers / (correctAnswers + wrongAnswers))) * 100;
        return propotion;
    }

    public Instances generateWekaTestingDataSet() {
        FastVector attributesList = new FastVector(4);
        int classIndex = -1;
        int index = 0;
        for (Characteristic c : this.getCharas()) {
            System.out.println("2435234523452345"+c.getPossibleValues().size());
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

        Instances testDataSet = new Instances("TrainingDataSet", attributesList, this.getRows().size());
        testDataSet.setClassIndex(classIndex);

        for (Row r : this.getRows()) {
            Instance instance = new DenseInstance(r.getValuesMap().size());
            for (Map.Entry<Characteristic, Value> a : r.getValuesMap().entrySet()) {
                System.out.println("" + a.getKey().getName().toString());
                Attribute a1 = (Attribute) attributesList.stream().filter(x -> ((Attribute) x).name().toLowerCase().equals(a.getKey().getName().toLowerCase())).findFirst().get();

                /*
                *   Set value of the Attribute (for example: 'Sex') and the Value of this row (for example: 'male')
                 */
                instance.setValue(a1, a.getValue().toString());
            }
            testDataSet.add(instance);
        }

        return testDataSet;
    }
}
