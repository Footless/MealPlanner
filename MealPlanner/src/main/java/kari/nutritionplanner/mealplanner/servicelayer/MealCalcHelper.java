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

import java.util.List;
import java.util.Map;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.domain.Meal;
import kari.nutritionplanner.mealplanner.util.ProcessIngredients;

/**
 * Apuluokka GUI:lle, jossa säilötään saatuja arvoja, kunnes ne saadaan
 * syötettyä MealCalculatorille. Tarjoaa myös apumetodeja raaka-aineiden saamiseen.
 *
 * @author kari
 */
public class MealCalcHelper {

    private Meal meal;
    private int desiredCalories;
    private int desiredProtein;
    private int desiredFat;
    private final ProcessIngredients ingredientProcessor;

    /**
     * Konstruktori saa parametrinä CalculateMeal-olion, jonka metodeja se voi
     * käyttää.
     *
     * @param ingredientProcessor ProcessIngredients-olio raaka-aineita varten
     * @see ProcessIngredients
     */
    public MealCalcHelper(ProcessIngredients ingredientProcessor) {
        this.meal = new Meal();
        this.ingredientProcessor = ingredientProcessor;
    }

    /**
     * Asettaa halutun pääraaka-aineen nimen perusteella.
     *
     * @param name pääraaka-aineen nimi
     */
    public void setMainIngredient(String name) {
        meal.setMainIngredient(ingredientProcessor.getIngredients().get("mains").get(getIdForMainIng(name)));
    }

    /**
     * Tyhjentää kaikki arvot. Käytetään aterian laskemisen lopuksi.
     */
    public void clear() {
        this.meal = new Meal();
        this.desiredCalories = 0;
        this.desiredFat = 0;
        this.desiredProtein = 0;
    }

    /**
     * Asettaa halutun lisäkkeen annetun nimen perusteella. Jos nimi on "misc"
     * asettaa dummy-Ingredientin id:llä 99999, jotta CalculateMeal osaa
     * myöhemmässä vaiheessa arpoa satunnaisen raaka-aineen lisäkkeeksi.
     *
     * @param name raaka-aineen nimi
     */
    public void setSideIngredient(String name) {
        if (!name.contains("misc")) {
            if (ingredientProcessor.getIngredients().get("sides").containsKey(getIdForSideIng(name.toLowerCase()))) {
                meal.setSideIngredient(ingredientProcessor.getIngredients().get("sides").get(getIdForSideIng(name.toLowerCase())));
            }
        } else {
            meal.setSideIngredient(new Ingredient(99999, "misc"));
        }
    }

    /**
     * Asettaa käyttäjän syöttämät tavoitekalorit muistiin.
     *
     * @param desiredCalories halutut kalorit liukulukuna
     */
    public void setDesiredCalories(int desiredCalories) {
        this.desiredCalories = desiredCalories;
    }

    /**
     * Asettaa käyttäjän syöttämän halutun rasvamäärän muistiin.
     *
     * @param desiredFat haluttu rasvan määrä liukulukuna
     */
    public void setDesiredFat(int desiredFat) {
        this.desiredFat = desiredFat;
    }

    /**
     * Asettaa käyttäjän syöttämän halutun proteiinimäärän muistiin.
     *
     * @param desiredProtein haluttu proteiinin määrä liukulukuna
     */
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
        Map<Integer, Ingredient> mains = ingredientProcessor.getIngredients().get("mains");
        for (Integer i : mains.keySet()) {
            if (mains.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Palauttaa id-numeron annetulle raaka-aineelle.
     *
     * @param name raaka-aineen nimi
     * @return raaka-aineen id-numero
     */
    public int getIdForSideIng(String name) {
        Map<Integer, Ingredient> sides = ingredientProcessor.getIngredients().get("sides");
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
     * @see Ingredient
     */
    public List<Ingredient> getMainIngredients() {
        return ingredientProcessor.getMainIngredients();
    }

    /**
     * Palauttaa Mapin kaikista saatavilla olevista pääraaka-aineista.
     *
     * @return Map-olio, jossa kaikki saatavilla olevat pääraaka-aineet.
     */
    public Map<Integer, Ingredient> getMainIngredientsAsMap() {
        return ingredientProcessor.getIngredients().get("mains");
    }

    /**
     * Palauttaa Mapin kaikista saatavilla olevista lisäkkeistä.
     *
     * @return Map-olio, jossa kaikki saatavilla olevat lisäkkeet.
     */
    public Map<Integer, Ingredient> getSideIngredientsAsMap() {
        return ingredientProcessor.getIngredients().get("sides");
    }

    /**
     * Palauttaa saatavilla olevat lisäkkeet listana.
     *
     * @return List-olio jossa kaikki saatavilla olevat lisäkkeet.
     */
    public List<Ingredient> getSideIngredients() {
        return ingredientProcessor.getSideIngredients();
    }
}
