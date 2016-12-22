/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner;

import java.util.Map;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.servicelayer.ProcessIngredients;
import kari.nutritionplanner.mealplanner.util.FoodMacroReader;
import kari.nutritionplanner.mealplanner.util.FoodNameReader;

/**
 *
 * @author kari
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ProcessIngredients pi = new ProcessIngredients();
        Map<String, Map<String, Ingredient>> ings = pi.getIngredients();
        for (String s : ings.keySet()) {
            for (String str : ings.get(s).keySet()) {
                System.out.println(ings.get(s).get(str));
            }
            System.out.println("------");
        }
    }

}
