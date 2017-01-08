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
 * CalculateMealin apuluokka, joka pyörittelee luodun aterian raaka-aineita sekä
 * lopuksi pyöristää arvot järkevään muotoon.
 *
 * @author kari
 */
public class MealTweaker {

    private final Meal meal;
    private final double calories;
    private final double protein;
    private final double fat;
    private final MacroCalculator mc;

    /**
     * Konstuktori saa Meal-olion jota pitäisi säätää, sekä tavoitemakrot.
     *
     * @param meal Meal-olio, jolle säädöt pitäisi tehdä
     * @param calories haluttu määrä kaloreja
     * @param protein haluttu määrä proteiinin
     * @param fat haluttu määrä rasvaa
     */
    public MealTweaker(Meal meal, double calories, double protein, double fat) {
        this.meal = meal;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.mc = new MacroCalculator();
    }

    /**
     * Tekee kaikki taiat. Pyörittelee makroja ja säätää niitä, lopuksi
     * pyöristää määrät järkevään muotoon.
     *
     */
    public void tweakMeal() {
        evenUpIngredients(calories, protein, fat);
        roundUpIngredients();
    }

    private void proteinSubber(double protein) {
//        if (protein < meal.getProtein()) {
        double proteinToSub = protein - meal.getProtein();
        if (meal.getSideIngredient().getProtein() > 3) {
            subFromBoth(proteinToSub);
        } else {
            subFromMain(proteinToSub);
        }
//        }
    }

    private void subFromMain(double proteinToSub) {
        double mainAmount = mc.calculateAmountForProtein(proteinToSub, meal.getMainIngredient());
        if (meal.getMainIngredientAmount() + mainAmount > 0.5) {
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
        if (meal.getMainIngredientAmount() < 0.5) {
            meal.setMainIngredientAmount(0.5);
        }
    }

    private void caloriesTweaker(double calories) {
        double caloriesToTweak = calories - meal.getCalories();
        double caloriesToTweakSide = mc.calculateAmountForCalories((caloriesToTweak / 8) * 4, meal.getSideIngredient());
        double caloriesToTweakMain = mc.calculateAmountForCalories((caloriesToTweak / 8) * 1, meal.getMainIngredient());
        double caloriesToTweakSauce = mc.calculateAmountForCalories((caloriesToTweak / 8) * 3, meal.getSauce());
        double caloriesToAddTweakSide = mc.calculateAmountForCalories(caloriesToTweak, meal.getSideIngredient());
        double caloriesToTweakTotalMain = mc.calculateAmountForCalories(caloriesToTweak, meal.getMainIngredient());
        if (meal.getSideIngredientAmount() + caloriesToTweakSide >= 0 && meal.getMainIngredientAmount() + caloriesToTweakMain >= 0.8) {
            meal.setSideIngredientAmount(meal.getSideIngredientAmount() + caloriesToTweakSide);
            meal.setMainIngredientAmount(meal.getMainIngredientAmount() + caloriesToTweakMain);
            meal.setSauceAmount(meal.getSauceAmount() + caloriesToTweakSauce);
        } else if (meal.getSideIngredientAmount() + caloriesToAddTweakSide >= 0) {
            meal.setSideIngredientAmount(meal.getSideIngredientAmount() + caloriesToAddTweakSide);
        } else if (meal.getMainIngredientAmount() + caloriesToTweakTotalMain >= 0.8) {
            meal.setMainIngredientAmount(meal.getMainIngredientAmount() + caloriesToTweakTotalMain);
        } else {
            meal.setSideIngredientAmount(meal.getSideIngredientAmount() + caloriesToTweakSide);
            meal.setMainIngredientAmount(meal.getMainIngredientAmount() + caloriesToTweakMain);
        }
        if (meal.getSideIngredientAmount() < 0) {
            meal.setSideIngredientAmount(0);
        }
        if (meal.getMainIngredientAmount() < 0.5) {
            meal.setMainIngredientAmount(0.5);
        }
        if (meal.getSauceAmount() < 0) {
            meal.setSauceAmount(0);
        }
    }

    private void fatSubber(double fat) {
//        if (fat < meal.getFat() && meal.getSauceAmount() > 0) {
        double fatToSub = fat - meal.getFat();
        meal.setSauceAmount(meal.getSauceAmount() + mc.calculateAmountForFat(fatToSub, meal.getSauce()));
        if (meal.getSauceAmount() < 0.1) {
            meal.setSauceAmount(0);
        }
//        }
    }

    private void evenUpIngredients(double calories, double protein, double fat) {
        for (int i = 0; i < 20; i++) {
            if (allOk(calories, protein, fat)) {
                break;
            }
            fatSubber(fat);
            proteinSubber(protein);
            caloriesTweaker(calories);
        }
    }

    private boolean allOk(double calories, double protein, double fat) {
        return (meal.getCalories() <= calories + 20 && meal.getCalories() >= calories - 20)
                && (meal.getProtein() <= protein + 1 && meal.getProtein() >= protein - 1)
                && (meal.getFat() <= fat + 1 && meal.getFat() >= fat - 1);
    }

    /**
     * Pyöristää aterian raaka-aineiden määrät kahden desimaalin tarkkuuteen.
     * Esim. 1.23 on 123 grammaa.
     */
    public void roundUpIngredients() {
        double main = (Math.ceil(meal.getMainIngredientAmount() * 100)) / 100;
        double side = Math.ceil(meal.getSideIngredientAmount() * 100) / 100;
        double sauce = Math.ceil(meal.getSauceAmount() * 100) / 100;
        meal.setMainIngredientAmount(main);
        meal.setSideIngredientAmount(side);
        meal.setSauceAmount(sauce);
    }

}
