/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner;

import java.util.Map;
import javax.swing.SwingUtilities;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.gui.UserInterface;
import kari.nutritionplanner.mealplanner.servicelayer.CalculateMeal;
import kari.nutritionplanner.mealplanner.util.ProcessIngredients;

/**
 *
 * @author kari
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        ProcessIngredients pi = new ProcessIngredients();
//        Map<String, Map<Integer, Ingredient>> ings = pi.getIngredients();
//        for (String s : ings.keySet()) {
//            for (Integer i : ings.get(s).keySet()) {
//                System.out.println(ings.get(s).get(i));
//            }
//            System.out.println("------");
//        }
//        CalculateMeal cm = new CalculateMeal();
//        if (cm.calculateAllMeal(805, 500, 40, 15)) {
//            System.out.println("success");
//        }
//        System.out.println(cm.getMeal());
        UserInterface ui = new UserInterface();
        SwingUtilities.invokeLater(ui);
    }
}
