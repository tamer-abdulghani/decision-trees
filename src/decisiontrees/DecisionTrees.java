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
import java.util.stream.Collectors;

/**
 *
 * @author Tamer
 */
public class DecisionTrees {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        mainController controller = new mainController();
        controller.startGUI();
    }
}
