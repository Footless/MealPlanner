package kari.nutritionplanner.mealplanner.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kari.nutritionplanner.mealplanner.domain.Ingredient;

/**
 * Hakee raaka-aineet tiedostoista SCVReaderiä käyttäen. Raaka-aineet
 * tallennetaan Map-olioihin, tyypin mukaisesti.
 * 
 * @see CSVReader
 * @author kari
 */

public class ProcessIngredients {

    private final Map<Integer, Ingredient> mainIgredients;
    private final Map<Integer, Ingredient> sideIgredients;
    private final Map<Integer, Ingredient> sauces;
    private final Map<Integer, Ingredient> sidesAndMisc;

    public ProcessIngredients() throws IOException {
        this.mainIgredients = new HashMap<>();
        this.sidesAndMisc = new HashMap<>();
        this.sauces = new HashMap<>();
        this.sideIgredients = new HashMap<>();
        addAll();
    }

    private void addMainIngredients() throws IOException {
        CSVReader lfnr = new CSVReader("main_ingredients.csv");
        List<Ingredient> ingredients = lfnr.getAllIngredients();

        for (Ingredient ingredient : ingredients) {
            if (addIngredient(ingredient)) {
                mainIgredients.put(ingredient.getId(), ingredient);
            }
        }
    }

    private void addSideIngredients() throws IOException {
        CSVReader lfnr = new CSVReader("side_ingredients.csv");
        List<Ingredient> ingredients = lfnr.getAllIngredients();

        for (Ingredient ingredient : ingredients) {
            if (addIngredient(ingredient)) {
                sideIgredients.put(ingredient.getId(), ingredient);
            }
        }
    }

    private void addSauces() throws IOException {
        CSVReader lfnr = new CSVReader("sauces.csv");
        List<Ingredient> ingredients = lfnr.getAllIngredients();

        for (Ingredient ingredient : ingredients) {
            if (addIngredient(ingredient)) {
                sauces.put(ingredient.getId(), ingredient);
            }
        }
    }

    private void addSidesAndStuffs() throws IOException {
        CSVReader lfnr = new CSVReader("sidesAndStuff.csv");
        List<Ingredient> ingredients = lfnr.getAllIngredients();

        for (Ingredient ingredient : ingredients) {
            if (addIngredient(ingredient)) {
                sidesAndMisc.put(ingredient.getId(), ingredient);
            }
        }
    }

    private boolean addIngredient(Ingredient ing) throws IOException {
        CSVReader fMacroR = new CSVReader("component_value_stub.csv");
        if (fMacroR.searchMacros(ing)) {
            return true;
        }
        ;
        return false;
    }

    private void addAll() throws IOException {
        addMainIngredients();
        addSideIngredients();
        addSauces();
        addSidesAndStuffs();
    }

    public List<Ingredient> getMainIngredients() throws IOException {
        CSVReader lfnr = new CSVReader("main_ingredients.csv");
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
