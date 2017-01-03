/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.domain;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kari
 */
public class Meal {

    private final Map<Integer, Ingredient> ingredients;
    private final Map<Integer, Double> amounts;
    
    public Meal() {
        this.ingredients = new HashMap<>();
        this.amounts = new HashMap<>();
        this.amounts.put(1, 0.0);
        this.amounts.put(2, 0.0);
        this.amounts.put(3, 0.0);
        this.amounts.put(4, 0.0);
        this.ingredients.put(1, new Ingredient(0, "dump"));
        this.ingredients.put(2, new Ingredient(0, "dump"));
        this.ingredients.put(3, new Ingredient(0, "dump"));
        this.ingredients.put(4, new Ingredient(0, "dump"));
    }

    public void setMainIngredient(Ingredient mainIngredient) {
        ingredients.replace(1, mainIngredient);
    }

    public void setSideIngredient(Ingredient sideIngredient) {
        ingredients.replace(4, sideIngredient);
    }

    public void setSauce(Ingredient sauce) {
        ingredients.replace(2, sauce);
    }

    public void setMisc(Ingredient misc) {
        ingredients.replace(3, misc);
    }

    public void setMainIngredientAmount(double mainIngredientAmount) {
        this.amounts.replace(1, mainIngredientAmount);
    }

    public void setSideIngredientAmount(double sideIngredientAmount) {
        this.amounts.replace(4, sideIngredientAmount);
    }

    public void setSauceAmount(double sauceAmount) {
        this.amounts.replace(2, sauceAmount);
    }

    public void setMiscAmount(double miscAmount) {
        this.amounts.replace(3, miscAmount);
    }

    public double getProtein() {
        double protein = 0;
        for (Integer i : ingredients.keySet()) {
            protein += ingredients.get(i).getProtein() * amounts.get(i);
        }
        return protein;
    }

    public double getFat() {
        double fat = 0;
        for (Integer i : ingredients.keySet()) {
            fat += ingredients.get(i).getFat() * amounts.get(i);
        }
        return fat;
    }

    public double getCalories() {
        double calories = 0;
        for (Integer i : ingredients.keySet()) {
            calories += ingredients.get(i).getCalories() * amounts.get(i);
        }
        return calories;
    }

    public double getCarbs() {
        double carbs = 0;
        for (Integer i : ingredients.keySet()) {
            carbs += ingredients.get(i).getCarb() * amounts.get(i);
        }
        return carbs;
    }

    public double getFiber() {
        double fibers = 0;
        for (Integer i : ingredients.keySet()) {
            fibers += ingredients.get(i).getFiber() * amounts.get(i);
        }
        return fibers;
    }

    public Ingredient getSideIngredient() {
        return ingredients.get(4);
    }
    
    public Ingredient getMisc() {
        return ingredients.get(3);
    }

    public double getMainIngredientAmount() {   //kerrotaan sadalla tulostusvaiheessa
        return amounts.get(1);
    }

    public double getSauceAmount() {   //kerrotaan sadalla tulostusvaiheessa
        return amounts.get(2);
    }

    public Ingredient getSauce() {
        return ingredients.get(2);
    }

    public Ingredient getMainIngredient() {
        return ingredients.get(1);
    }

    public double getMiscAmount() {   //kerrotaan sadalla tulostusvaiheessa
        return amounts.get(3);
    }

    public double getSideIngredientAmount() {   //kerrotaan sadalla tulostusvaiheessa
        return amounts.get(4);
    }

    @Override
    public String toString() {
        return "Ainekset:\n"
                + ingredients.get(1).getName() + ": " + getMainIngredientAmount() * 100 + "gr\n"
                + ingredients.get(4).getName() + ": " + getSideIngredientAmount() * 100 + "gr\n"
                + ingredients.get(2).getName() + ": " + getSauceAmount() * 100 + "gr\n"
                + ingredients.get(3).getName() + ": " + getMiscAmount() * 100 + "gr\n\n"
                + "Ravintoarvot: \n"
                + "Kilokalorit: " + Math.ceil(getCalories()) + "kcal\n"
                + "Proteiini: " + Math.ceil(getProtein()) + "gr\n"
                + "Rasva: " + Math.ceil(getFat()) + "gr\n"
                + "Hiilihydraatit: " + Math.ceil(getCarbs()) + "gr\n"
                + "Kuidut: " + Math.ceil(getFiber()) + "gr\n";
    }

}
