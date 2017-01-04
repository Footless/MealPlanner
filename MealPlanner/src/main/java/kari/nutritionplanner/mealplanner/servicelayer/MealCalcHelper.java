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
import kari.nutritionplanner.mealplanner.domain.Meal;

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
    private CalculateMeal cm;

    public MealCalcHelper(CalculateMeal cm) throws IOException {
        this.cm = cm;
        this.meal = new Meal();
    }

    /**
     * Asettaa halutun pääraaka-aineen nimen perusteella.
     * @param name pääraaka-aineen nimi
     */
    
    public void setMainIngredient(String name) {
        meal.setMainIngredient(cm.getIngredients().get("mains").get(cm.getMainIngId(name)));
    }
    
    public void clear() {
        this.meal = new Meal();
        this.desiredCalories = 0;
        this.desiredFat = 0;
        this.desiredProtein = 0;
    }
    
    public void setSideIngredient(String name) {
        // TODO: joku näppärä tapa tähän, jossa mahdollisuus myös satunnaiseen, ehkä
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

    public int getDesiredCalories() {
        return desiredCalories;
    }

    public int getDesiredFat() {
        return desiredFat;
    }

    public int getDesiredProtein() {
        return desiredProtein;
    }
    
    
}
