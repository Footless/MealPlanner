/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.servicelayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.util.FoodMacroReader;
import kari.nutritionplanner.mealplanner.util.FoodNameReader;

/**
 *
 * @author kari
 */
public class ProcessIngredients {
    private Map<String, Ingredient> mainIgredients;
    private Map<String, Ingredient> sideIgredients;
    private Map<String, Ingredient> sauces;
    private Map<String, Ingredient> sidesAndMisc;

    public ProcessIngredients() {
        this.mainIgredients = new HashMap<>();
        this.sidesAndMisc = new HashMap<>();
        this.sauces = new HashMap<>();
        this.sideIgredients = new HashMap<>();
    }
    
    private void addMAinIngredient(String s) {
        mainIgredients.put(addIngredient(s).getName(), addIngredient(s));
    }
    
    private void addSideIngredient(String s) {
        sideIgredients.put(addIngredient(s).getName(), addIngredient(s));
    }
    
    private void addSauce(String s) {
        sauces.put(addIngredient(s).getName(), addIngredient(s));
    }
    
    private void addSidesAndStuff(String s) {
        sidesAndMisc.put(addIngredient(s).getName(), addIngredient(s));
    }
    
    private Ingredient addIngredient(String s) {
        FoodNameReader fNameR = new FoodNameReader("food_utf.csv");
        Ingredient ing = fNameR.search(s);
        FoodMacroReader fMacroR = new FoodMacroReader("component_value.csv");
        fMacroR.search(ing);
        return ing;
    }
}
