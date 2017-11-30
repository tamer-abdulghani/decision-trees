/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontrees;

import decisiontrees.controllers.mainController;
import decisiontrees.models.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Tamer
 */
public class DecisionTrees {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        /**
         * Specifying our characteristic input
         */
        ArrayList<String> charalist = new ArrayList<String>();
        charalist.add("Parch");
        charalist.add("Pclass");
        charalist.add("Sex");
        charalist.add("SibSp");
        charalist.add("Embarked");
        charalist.add("Survived");

        /**
         * Loading training dataset
         */
        DAO database = new DAO();
        DataSet dataset = database.extractTrainingData(charalist);
        System.out.println("===========");
        System.out.println("Nb of characterstics loaded: " + dataset.getCharas().size());
        System.out.println("Nb of rows loaded: " + dataset.getRows().size());
        System.out.println("===========");

        /**
         * Generating All Possible Trees
         */
        ArrayList<SingleCharacteristicTree> trees = dataset.generateAllPossibleTrees();
        String result = "";

        for (SingleCharacteristicTree tree : trees) {
            result += tree.toString();
            result += "\n";
            result += "*****************************";
            result += "\n";
        }
        System.out.println(result);

        /**
         * Loading test dataset and assessing the quality of all the trees
         */
        database.extractTestingData(charalist);
        TestDataSet trainDataSet = database.extractTestingData(charalist);
        for (SingleCharacteristicTree tree : trees) {
            float propotions = trainDataSet.assessingQualityOfTree(tree);

            result = "";
            result += tree.getProfile().getName() + " Decision Tree Quality is the following \n";
            result += "correct answers = " + propotions + " % \n";
            result += "wronge answers = " + (100 - propotions) + " % \n";

            System.out.println(result);
        }
        /*
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DecisionTrees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(DecisionTrees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DecisionTrees.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(DecisionTrees.class.getName()).log(Level.SEVERE, null, ex);
        }
        mainController controller = new mainController();
        controller.startGUI();*/

    }
}
