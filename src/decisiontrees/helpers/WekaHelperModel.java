/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.helpers;

import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 *
 * @author Tamer
 */
public class WekaHelperModel {

    private Instances trainDataSet;
    private Instances testDataSet;
    private J48 classifier;

    public WekaHelperModel(Instances trainDataSet, J48 cls) {
        this.classifier = cls;
        this.trainDataSet = trainDataSet;
        this.testDataSet = testDataSet;
    }

    /**
     * @return the trainDataSet
     */
    public Instances getTrainDataSet() {
        return trainDataSet;
    }

    /**
     * @param trainDataSet the trainDataSet to set
     */
    public void setTrainDataSet(Instances trainDataSet) {
        this.trainDataSet = trainDataSet;
    }

    /**
     * @return the classifier
     */
    public J48 getClassifier() {
        return classifier;
    }

    /**
     * @param classifier the classifier to set
     */
    public void setClassifier(J48 classifier) {
        this.classifier = classifier;
    }

    /**
     * @return the testDataSet
     */
    public Instances getTestDataSet() {
        return testDataSet;
    }

    /**
     * @param testDataSet the testDataSet to set
     */
    public void setTestDataSet(Instances testDataSet) {
        this.testDataSet = testDataSet;
    }

}
