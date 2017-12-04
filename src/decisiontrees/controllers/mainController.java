/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees.controllers;

import decisiontrees.helpers.WekaHelperModel;
import decisiontrees.models.DAO;
import decisiontrees.models.SingleCharacteristicTree;
import decisiontrees.models.Characteristic;
import decisiontrees.models.DataSet;
import decisiontrees.models.TestingDataSet;
import decisiontrees.views.mainFrame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.trees.J48;
import weka.core.Instances;

/**
 *
 * @author Tamer
 */
public class mainController {

    private DAO database;
    private DataSet trainingDataSet;
    private TestingDataSet testingDataSet;
    private mainFrame frame;
    private WekaHelperModel wekaModel;

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
        int trainingDatasourceId = 11;

        this.trainingDataSet = this.database.extractTrainingData(charalist, trainingDatasourceId);
        this.frame.displayTrainingCharasCategoricalOnly(this.trainingDataSet);
    }

    public void buildElementaryTree(Characteristic selectedChara) {
        Characteristic target = this.trainingDataSet.getCharas().stream().filter(x -> x.getName().toLowerCase().equals("survived")).findFirst().get();
        SingleCharacteristicTree tree = this.trainingDataSet.createSingleCharacteristicTree(selectedChara, target);
    }

    public void buildAllTrees() {
        Characteristic target = this.trainingDataSet.getCharas().stream().filter(x -> x.getName().toLowerCase().equals("survived")).findFirst().get();
        List<SingleCharacteristicTree> trees = this.trainingDataSet.generateAllPossibleTrees(target);
        this.frame.displayAllTrees(trees);
    }

    public void loadTestDataSet() {
        List<String> charalist = new ArrayList<String>();
        charalist.add("Parch");
        charalist.add("Pclass");
        charalist.add("Sex");
        charalist.add("SibSp");
        charalist.add("Embarked");
        charalist.add("Survived");
        int trainingDatasourceId = 12;

        this.testingDataSet = this.database.extractTestingData(charalist, trainingDatasourceId);
        this.frame.displayTestModel(this.testingDataSet);
    }

    public void assessQualityOfTree(SingleCharacteristicTree tree) {
        float propotions = this.testingDataSet.assessingQualityOfTree(tree);
        this.frame.displayTreeQuality(tree, propotions);
    }

    public void assessQualityOfAllTree() {
        Map<SingleCharacteristicTree, Float> mapTreePropotion = new HashMap<>();
        Characteristic target = this.trainingDataSet.getCharas().stream().filter(x -> x.getName().toLowerCase().equals("survived")).findFirst().get();

        List<SingleCharacteristicTree> allTrees = this.trainingDataSet.generateAllPossibleTrees(target);

        for (SingleCharacteristicTree tree : allTrees) {
            mapTreePropotion.put(tree, this.testingDataSet.assessingQualityOfTree(tree));
        }

        this.frame.displayAllTreesQuality(mapTreePropotion);

    }

    public void bestDecisionTree() {
        Characteristic target = this.trainingDataSet.getCharas().stream().filter(x -> x.getName().toLowerCase().equals("survived")).findFirst().get();
        List<SingleCharacteristicTree> allTrees = this.trainingDataSet.generateAllPossibleTrees(target);
        float MaxPropotionCorrectAnswers = 0;
        SingleCharacteristicTree bestTree = null;

        for (SingleCharacteristicTree tree : allTrees) {
            if (this.testingDataSet.assessingQualityOfTree(tree) > MaxPropotionCorrectAnswers) {
                MaxPropotionCorrectAnswers = this.testingDataSet.assessingQualityOfTree(tree);
                bestTree = tree;
            }
        }

        this.frame.displayBestTree(bestTree, MaxPropotionCorrectAnswers);

    }

    public J48 buildDecisionTreeWithC45() {
        try {
            this.wekaModel = this.trainingDataSet.createDecisionTreeC45();
            return this.wekaModel.getClassifier();
        } catch (Exception ex) {
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public void evaluateWekaModel() {

        Instances wekaTestDataSet = this.testingDataSet.generateWekaTestingDataSet();

        String result = this.trainingDataSet.evaluateWekaModel(this.wekaModel, wekaTestDataSet);

        this.frame.displayWekaEvaluationResult(result);
    }

}
