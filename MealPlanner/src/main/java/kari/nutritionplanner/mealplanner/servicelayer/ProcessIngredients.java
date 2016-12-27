/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.servicelayer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.util.SCVReader;

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
        SCVReader lfnr = new SCVReader("main_ingredients.csv");
        List<Ingredient> ingredients = lfnr.searchAllIngredients();

        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
            mainIgredients.put(ingredient.getName(), ingredient);
        }
    }

    private void addSideIngredients() {
        SCVReader lfnr = new SCVReader("side_ingredients.csv");
        List<Ingredient> ingredients = lfnr.searchAllIngredients();

        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
            sideIgredients.put(ingredient.getName(), ingredient);
        }
    }

    private void addSauces() {
        SCVReader lfnr = new SCVReader("sauces.csv");
        List<Ingredient> ingredients = lfnr.searchAllIngredients();

        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
            sauces.put(ingredient.getName(), ingredient);
        }
    }

    private void addSidesAndStuffs() {
        SCVReader lfnr = new SCVReader("sidesAndStuff.csv");
        List<Ingredient> ingredients = lfnr.searchAllIngredients();

        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
            sidesAndMisc.put(ingredient.getName(), ingredient);
        }
    }

    private Ingredient addIngredient(Ingredient ing) {
        SCVReader fMacroR = new SCVReader("component_value_stub.csv");
        fMacroR.searchMacros(ing);
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
