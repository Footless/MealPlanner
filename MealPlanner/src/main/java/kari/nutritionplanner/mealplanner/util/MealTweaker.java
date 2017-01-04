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
package kari.nutritionplanner.mealplanner.util;

import kari.nutritionplanner.mealplanner.domain.Meal;

/**
 * CalculateMealin apuluokka, joka pyörittelee luodun aterian raaka-aineita sekä lopuksi
 * pyöristää arvot järkevään muotoon.
 * 
 * @author kari
 */
public class MealTweaker {

    private final Meal meal;
    private final double calories;
    private final double protein;
    private final double fat;
    private final MacroCalculator mc;

    public MealTweaker(Meal meal, double calories, double protein, double fat) {
        this.meal = meal;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.mc = new MacroCalculator();
    }

    public void tweakMeal() {
        evenUpIngredients(calories, protein, fat);
        roundUpIngredients();
    }

    private void proteinSubber(double protein) {
        if (protein < meal.getProtein()) {
            double proteinToSub = protein - meal.getProtein();
            if (meal.getSideIngredient().getProtein() > 3) {
                subFromBoth(proteinToSub);
            } else {
                subFromMain(proteinToSub);
            }
        }
    }

    private void subFromMain(double proteinToSub) {
        double mainAmount = mc.calculateAmountForProtein(proteinToSub, meal.getMainIngredient());
        if (meal.getMainIngredientAmount() + mainAmount > 0.8) {
            meal.setMainIngredientAmount(meal.getMainIngredientAmount() + mainAmount);
        } else if (meal.getMainIngredient().getProtein() > 23) {
            meal.setMainIngredientAmount(0.5);
        } else {
            meal.setMainIngredientAmount(0.8);
        }
    }

    private void subFromBoth(double proteinToSub) {
        double mainAmount = mc.calculateAmountForProtein(proteinToSub / 2, meal.getMainIngredient());
        double sideAmount = mc.calculateAmountForProtein(proteinToSub / 2, meal.getSideIngredient());
        if (meal.getMainIngredientAmount() + mainAmount > 0.5 && meal.getSideIngredientAmount() + sideAmount > 0) {
            meal.setMainIngredientAmount(meal.getMainIngredientAmount() + mainAmount);
            meal.setSideIngredientAmount(meal.getSideIngredientAmount() + sideAmount);
        } else {
            subFromMain(proteinToSub);
        }
    }

    private void caloriesTweaker(double calories) {
        double caloriesToAdd = calories - meal.getCalories();
        double caloriesToAddSide = (caloriesToAdd / 8) * 4;
        double caloriesToAddMain = (caloriesToAdd / 8) * 3;
        double caloriesToAddsauce = (caloriesToAdd / 8);
        meal.setSideIngredientAmount(meal.getSideIngredientAmount() + mc.calculateAmountForCalories(caloriesToAddSide, meal.getSideIngredient()));
        meal.setSauceAmount(meal.getSauceAmount() + mc.calculateAmountForCalories(caloriesToAddsauce, meal.getSauce()));
        meal.setMainIngredientAmount(meal.getMainIngredientAmount() + mc.calculateAmountForCalories(caloriesToAddMain, meal.getMainIngredient()));
    }

    private void fatSubber(double fat) {
        if (fat < meal.getFat() && meal.getSauceAmount() > 0) {
            double fatToSub = fat - meal.getFat();
            meal.setSauceAmount(meal.getSauceAmount() + mc.calculateAmountForFat(fatToSub, meal.getSauce()));
            if (meal.getSauceAmount() < 0.1) {
                meal.setSauceAmount(0);
            }
        }
    }

    private void evenUpIngredients(double calories, double protein, double fat) {
        for (int i = 0; i < 10; i++) {
            if (allOk(calories, protein, fat)) {
                break;
            }
            proteinSubber(protein);
            fatSubber(fat);
            caloriesTweaker(calories);
        }
    }

    private boolean allOk(double calories, double protein, double fat) {
        return (meal.getCalories() <= calories + 5 && meal.getCalories() >= calories - 5)
                && (meal.getProtein() <= protein + 1 && meal.getProtein() >= protein - 1)
                && (meal.getFat() <= fat + 1 && meal.getFat() >= fat - 1);
    }

    private void roundUpIngredients() {
        double main = Math.ceil(meal.getMainIngredientAmount() * 100) / 100;
        double side = Math.ceil(meal.getSideIngredientAmount() * 100) / 100;
        double sauce = Math.ceil(meal.getSauceAmount() * 100) / 100;
        meal.setMainIngredientAmount(main);
        meal.setSideIngredientAmount(side);
        meal.setSauceAmount(sauce);
    }

}
