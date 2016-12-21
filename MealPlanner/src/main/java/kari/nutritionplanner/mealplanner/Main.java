/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner;

import kari.nutritionplanner.mealplanner.domain.Ingredient;
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
        FoodNameReader scvr = new FoodNameReader("food_utf.csv");
        Ingredient ingredient = scvr.search("kuha");
        System.out.println(ingredient.getId());
        System.out.println(ingredient.getName());
        FoodMacroReader fmr = new FoodMacroReader("component_value.csv");
        fmr.search(ingredient);
        System.out.println(ingredient.getCalories());
        System.out.println(ingredient.getCarb());
        System.out.println(ingredient.getFat());
        System.out.println(ingredient.getProtein());
        System.out.println(ingredient.getFiber());
    }

}
