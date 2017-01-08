package kari.nutritionplanner.mealplanner.util;

import java.io.IOException;
import java.util.ArrayList;
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
            if (addMacrosToIngredient(ingredient)) {
                mainIgredients.put(ingredient.getId(), ingredient);
            }
        }
    }

    private void addSideIngredients() throws IOException {
        CSVReader lfnr = new CSVReader("side_ingredients.csv");
        List<Ingredient> ingredients = lfnr.getAllIngredients();

        for (Ingredient ingredient : ingredients) {
            if (addMacrosToIngredient(ingredient)) {
                sideIgredients.put(ingredient.getId(), ingredient);
            }
        }
    }

    private void addSauces() throws IOException {
        CSVReader lfnr = new CSVReader("sauces.csv");
        List<Ingredient> ingredients = lfnr.getAllIngredients();

        for (Ingredient ingredient : ingredients) {
            if (addMacrosToIngredient(ingredient)) {
                sauces.put(ingredient.getId(), ingredient);
            }
        }
    }

    private void addSidesAndStuffs() throws IOException {
        CSVReader lfnr = new CSVReader("sidesAndStuff.csv");
        List<Ingredient> ingredients = lfnr.getAllIngredients();

        for (Ingredient ingredient : ingredients) {
            if (addMacrosToIngredient(ingredient)) {
                sidesAndMisc.put(ingredient.getId(), ingredient);
            }
        }
    }

    public boolean addMacrosToIngredient(Ingredient ing) throws IOException {
        CSVReader fMacroR = new CSVReader("component_value_stub.csv");
        return fMacroR.searchMacros(ing);
    }

    private void addAll() throws IOException {
        addMainIngredients();
        addSideIngredients();
        addSauces();
        addSidesAndStuffs();
    }

    public List<Ingredient> getMainIngredients() {
        List<Ingredient> mains = new ArrayList<>();
        mainIgredients.values().stream().forEach((ing) -> {
            mains.add(ing);
        });
        return  mains;
    }
    
    public List<Ingredient> getSideIngredients() {
        List<Ingredient> sides = new ArrayList<>();
        sideIgredients.values().stream().forEach((ing) -> {
            sides.add(ing);
        });
        return sides;
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
