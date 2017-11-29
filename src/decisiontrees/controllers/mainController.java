/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.controllers;

import decisiontrees.models.DAO;
import decisiontrees.models.SingleCharacteristicTree;
import decisiontrees.models.Characteristic;
import decisiontrees.models.DataSet;
import decisiontrees.models.TestDataSet;
import decisiontrees.views.mainFrame;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;

/**
 *
 * @author Tamer
 */
public class mainController {

    private DAO database;
    private DataSet trainingModel;
    private TestDataSet testModel;
    private mainFrame frame;

    public mainController() {
        this.frame = new mainFrame(this);
        this.database = new DAO();

    }

    public void startGUI() {
        this.frame.setVisible(true);
    }

    public void loadTrainDataSet() {
        ArrayList<String> charalist = new ArrayList<String>();
        charalist.add("Parch");
        charalist.add("Pclass");
        charalist.add("Sex");
        charalist.add("SibSp");
        charalist.add("Embarked");
        charalist.add("Survived");

        this.trainingModel = this.database.extractTrainingData(charalist);
        this.frame.displayTrainingCharasCategoricalOnly(this.trainingModel);
    }

    public void buildElementaryTree(Characteristic selectedChara) {
        SingleCharacteristicTree tree = this.trainingModel.createSingleCharacteristicTree(selectedChara);
        this.frame.displaySingleTree(tree);
    }

    public void buildAllTrees() {
        ArrayList<SingleCharacteristicTree> trees = this.trainingModel.generateAllPossibleTrees();
        this.frame.displayAllTrees(trees);
    }

    public void loadTestDataSet() {
        ArrayList<String> charalist = new ArrayList<String>();
        charalist.add("Parch");
        charalist.add("Pclass");
        charalist.add("Sex");
        charalist.add("SibSp");
        charalist.add("Embarked");
        charalist.add("Survived");

        this.testModel = this.database.extractTestingData(charalist);
        this.frame.displayTestModel(this.testModel);
    }

    public void assessQualityOfTree(SingleCharacteristicTree tree) {
        float propotions = this.testModel.assessingQualityOfTree(tree);
        this.frame.displayTreeQuality(tree, propotions);
    }

    public String buildDecisionTreeWithC45(String filePath) {
        try {
            return this.trainingModel.createDecisionTreeC45(filePath).toString();
        } catch (Exception ex) {
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Error";
    }

}
