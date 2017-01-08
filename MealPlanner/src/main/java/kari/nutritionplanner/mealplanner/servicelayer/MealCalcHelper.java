/*
 * Copyright (C) 2017 kari
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kari.nutritionplanner.mealplanner.servicelayer;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.domain.Meal;
import kari.nutritionplanner.mealplanner.util.ProcessIngredients;

/**
 * Apuluokka GUI:lle, jossa säilötään saatuja arvoja, kunnes ne saadaan
 * syötettyä MealCalculatorille.
 *
 * @author kari
 */
public class MealCalcHelper {

    private Meal meal;
    private int desiredCalories;
    private int desiredProtein;
    private int desiredFat;
    private final CalculateMeal cm;
    private final ProcessIngredients ingredientProcessor;

    public MealCalcHelper(CalculateMeal cm) throws IOException {
        this.cm = cm;
        this.meal = new Meal();
        this.ingredientProcessor = new ProcessIngredients();
    }

    /**
     * Asettaa halutun pääraaka-aineen nimen perusteella.
     *
     * @param name pääraaka-aineen nimi
     */
    public void setMainIngredient(String name) {
        meal.setMainIngredient(cm.getIngredients().get("mains").get(getIdForMainIng(name)));
    }

    public void clear() {
        this.meal = new Meal();
        this.desiredCalories = 0;
        this.desiredFat = 0;
        this.desiredProtein = 0;
    }

    public void setSideIngredient(String name) {
        if (!name.contains("misc")) {
            meal.setSideIngredient(cm.getIngredients().get("sides").get(getIdForSideIng(name)));
        } else {
            meal.setSideIngredient(new Ingredient(99999, "misc"));
        }
    }

    public void setSauce(String name) {
        // TODO: tääkin jossain vaiheessa ehkä ajankohtainen
    }

    public void setDesiredCalories(int desiredCalories) {
        this.desiredCalories = desiredCalories;
    }

    public void setDesiredFat(int desiredFat) {
        this.desiredFat = desiredFat;
    }

    public void setDesiredProtein(int desiredProtein) {
        this.desiredProtein = desiredProtein;
    }

    public int getMainIngredientId() {
        return this.meal.getMainIngredient().getId();
    }
    
    public int getSideIngredientId() {
        return this.meal.getSideIngredient().getId();
    }

    public int getDesiredCalories() {
        return desiredCalories;
    }

    public int getDesiredFat() {
        return desiredFat;
    }

    public int getDesiredProtein() {
        return desiredProtein;
    }

    /**
     * Käyttöliittymälle tarjottu metodi, joka palauttaa pääraaka-aineen
     * id-numeron nimen perusteella.
     *
     * @param name haettavan raaka-aineen nimi
     * @return haetun raaka-aineen id
     * @see Ingredient
     */
    public int getIdForMainIng(String name) {
        Map<Integer, Ingredient> mains = cm.getIngredients().get("mains");
        for (Integer i : mains.keySet()) {
            if (mains.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                return i;
            }
        }
        return 0;
    }
    
    public int getIdForSideIng(String name) {
        Map<Integer, Ingredient> sides = cm.getIngredients().get("sides");
        for (Integer i : sides.keySet()) {
            if (sides.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Käyttöliittymälle tarjottu metodi, joka palauttaa listan kaikista
     * valittavissa olevista pääraaka-aineista.
     *
     * @return listan pääraaka-aineista, jotka ovat käyttäjän valittavissa.
     * @throws IOException
     * @see Ingredient
     */
    public List<Ingredient> getMainIngredients() throws IOException {
        return ingredientProcessor.getMainIngredients();
    }

    public Map<Integer, Ingredient> getMainIngredientsAsMap() {
        return cm.getIngredients().get("mains");
    }
    
    public Map<Integer, Ingredient> getSideIngredientsAsMap() {
        return cm.getIngredients().get("sides");
    }
    
    public List<Ingredient> getSideIngredients() throws IOException {
        return ingredientProcessor.getSideIngredients();
    }
}
