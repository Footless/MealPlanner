/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.servicelayer;

import java.util.Map;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.domain.Meal;

/**
 *
 * @author kari
 */
public class CalculateMeal {
    private Meal meal;
    private ProcessIngredients ingredientProcessor;
    private Map<String, Map<String, Ingredient>> ingredients;
    
    public CalculateMeal() {
        this.ingredientProcessor = new ProcessIngredients();
        this.ingredients = ingredientProcessor.getIngredients();
    }
}
