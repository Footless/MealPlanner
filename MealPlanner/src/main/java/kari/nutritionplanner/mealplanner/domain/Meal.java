/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.domain;

/**
 *
 * @author kari
 */
public class Meal {

    private Ingredient mainIngredient;
    private Ingredient sideIngredient;
    private Ingredient sauce;
    private Ingredient misc;
    private double mainIngredientAmount;
    private double sideIngredientAmount;
    private double sauceAmount;
    private double miscAmount;

    public void setMainIngredient(Ingredient mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public void setSideIngredient(Ingredient sideIngredient) {
        this.sideIngredient = sideIngredient;
    }

    public void setSauce(Ingredient sauce) {
        this.sauce = sauce;
    }

    public void setMisc(Ingredient misc) {
        this.misc = misc;
    }

    public void setMainIngredientAmount(double mainIngedientAmount) {
        this.mainIngredientAmount = mainIngedientAmount;
    }

    public void setSideIngredientAmount(double sideIngredientAmount) {
        this.sideIngredientAmount = sideIngredientAmount;
    }

    public void setSauceAmount(double sauceAmount) {
        this.sauceAmount = sauceAmount;
    }

    public void setMiscAmount(double miscAmount) {
        this.miscAmount = miscAmount;
    }

    public double getProtein() {
        if (mainIngredient != null) {
            return mainIngredientAmount * mainIngredient.getProtein();
        }
        return 0;
    }

    public double getFat() {
        double fat = 0;
        if (mainIngredient != null) {
            fat += mainIngredientAmount * mainIngredient.getFat();
        }
        if (sauce != null) {
            fat += sauceAmount * sauce.getFat();
        }
        return fat;
    }

    public double getCalories() {
        double calories = 0;
        if (mainIngredient != null) {
            calories += mainIngredientAmount * mainIngredient.getCalories();
        }
        if (sauce != null) {
            calories += sauceAmount * sauce.getCalories();
        }
        if (sideIngredient != null) {
            calories += sauceAmount * sauce.getCalories();
        }
        if (misc != null) {
            calories += miscAmount * misc.getCalories();
        }
        return calories;
    }

    public Ingredient getSideIngredient() {
        return sideIngredient;
    }

}
