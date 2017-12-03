/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.models;

import java.util.Map;

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
}
