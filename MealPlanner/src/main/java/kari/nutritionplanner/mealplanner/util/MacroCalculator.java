/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.util;

import kari.nutritionplanner.mealplanner.domain.Ingredient;

/**
 *
 * @author kari
 */
public class MacroCalculator {

    public double calculateAmountForProtein(double protein, Ingredient ing) {
        if (ing.getProtein() > 0) {
            return protein / ing.getProtein(); // kertaa sata jos haluaa grammoina
        }
        return 0;
    }
    
    /**
     *
     * @param fat
     * @param ing
     * @return
     */
    public double calculateAmountForFat(double fat, Ingredient ing) {
        if (ing.getFat() > 0) {
            return fat / ing.getFat(); // kertaa sata jos haluaa grammoina
        }
        return 0;
    }
    
    public double calculateAmountForCalories(double calories, Ingredient ing) {
        if (ing.getCalories() > 0) {
            return calories / ing.getCalories(); // kertaa sata jos haluaa grammoina
        }
        return 0;
    }
}
