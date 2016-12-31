/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.util;

import java.util.ArrayList;
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

    private Map<Integer, Ingredient> mainIgredients;
    private Map<Integer, Ingredient> sideIgredients;
    private Map<Integer, Ingredient> sauces;
    private Map<Integer, Ingredient> sidesAndMisc;

    public ProcessIngredients() {
        this.mainIgredients = new HashMap<>();
        this.sidesAndMisc = new HashMap<>();
        this.sauces = new HashMap<>();
        this.sideIgredients = new HashMap<>();
        addAll();
    }

    private void addMainIngredients() {
        SCVReader lfnr = new SCVReader("main_ingredients.csv");
        List<Ingredient> ingredients = lfnr.getAllIngredients();

        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
            mainIgredients.put(ingredient.getId(), ingredient);
        }
    }

    private void addSideIngredients() {
        SCVReader lfnr = new SCVReader("side_ingredients.csv");
        List<Ingredient> ingredients = lfnr.getAllIngredients();

        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
            sideIgredients.put(ingredient.getId(), ingredient);
        }
    }

    private void addSauces() {
        SCVReader lfnr = new SCVReader("sauces.csv");
        List<Ingredient> ingredients = lfnr.getAllIngredients();

        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
            sauces.put(ingredient.getId(), ingredient);
        }
    }

    private void addSidesAndStuffs() {
        SCVReader lfnr = new SCVReader("sidesAndStuff.csv");
        List<Ingredient> ingredients = lfnr.getAllIngredients();

        for (Ingredient ingredient : ingredients) {
            addIngredient(ingredient);
            sidesAndMisc.put(ingredient.getId(), ingredient);
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
    
    public List<Ingredient> getMainIngredients() {
        SCVReader lfnr = new SCVReader("main_ingredients.csv");
        List<Ingredient> mains = lfnr.getAllIngredients();
        return mains;
    }

    public Map<String, Map<Integer, Ingredient>> getIngredients() {
        Map<String, Map<Integer, Ingredient>> ingredients = new HashMap<>();
        ingredients.put("mains", mainIgredients);
        ingredients.put("sides", sideIgredients);
        ingredients.put("sauces", sauces);
        ingredients.put("sidesAndMisc", sidesAndMisc);
        return ingredients;
    }
}
