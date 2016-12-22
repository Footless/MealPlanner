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
import kari.nutritionplanner.mealplanner.util.LocalFoodNameReader;

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
        addAll();
    }

    private void addMainIngredients() {
        LocalFoodNameReader lfnr = new LocalFoodNameReader("main_ingredients.csv");
        List<Ingredient> ingredients = lfnr.searchAll();

        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
            mainIgredients.put(ingredient.getName(), ingredient);
        }
    }

    private void addSideIngredients() {
        LocalFoodNameReader lfnr = new LocalFoodNameReader("side_ingredients.csv");
        List<Ingredient> ingredients = lfnr.searchAll();

        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
            sideIgredients.put(ingredient.getName(), ingredient);
        }
    }

    private void addSauces() {
        LocalFoodNameReader lfnr = new LocalFoodNameReader("sauces.csv");
        List<Ingredient> ingredients = lfnr.searchAll();

        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
            sauces.put(ingredient.getName(), ingredient);
        }
    }

    private void addSidesAndStuffs() {
        LocalFoodNameReader lfnr = new LocalFoodNameReader("sidesAndStuff.csv");
        List<Ingredient> ingredients = lfnr.searchAll();

        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
            sidesAndMisc.put(ingredient.getName(), ingredient);
        }
    }

    private Ingredient addIngredient(Ingredient ing) {
        FoodMacroReader fMacroR = new FoodMacroReader("component_value.csv");
        fMacroR.search(ing);
        return ing;
    }

    private void addAll() {
        addMainIngredients();
        addSideIngredients();
        addSauces();
        addSidesAndStuffs();
    }

    public Map<String, Map<String, Ingredient>> getIngredients() {
        Map<String, Map<String, Ingredient>> ingredients = new HashMap<>();
        ingredients.put("mains", mainIgredients);
        ingredients.put("sides", sideIgredients);
        ingredients.put("sauces", sauces);
        ingredients.put("sidesAndMisc", sidesAndMisc);
        return ingredients;
    }
}
