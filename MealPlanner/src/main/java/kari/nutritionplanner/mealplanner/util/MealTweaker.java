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
        evenUpIngredients();
        roundUpIngredients();
    }

    private void proteinAlterer() {
        if (!proteinOk()) {
            double proteinToSub = protein - meal.getProtein();
            if (meal.getSideIngredient().getProtein() > 3 && protein < meal.getProtein()) {
                subFromBoth(proteinToSub);
            } else {
                subFromMain(proteinToSub);
            }
        }
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
        double mainAmount = mc.calculateAmountForProtein(proteinToSub / 4 * 3, meal.getMainIngredient());
        double sideAmount = mc.calculateAmountForProtein(proteinToSub / 4, meal.getSideIngredient());
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

    private void caloriesTweaker() {
        if (calories < meal.getCalories()) {
            subCalories();
        } else {
            addCalories();
        }
    }

    private void fatSubber() {
        if (!fatOk()) {
            double fatToSub = fat - meal.getFat();
            meal.setSauceAmount(meal.getSauceAmount() + mc.calculateAmountForFat(fatToSub, meal.getSauce()));
            if (meal.getSauceAmount() < 0.1) {
                meal.setSauceAmount(0);
            }
        }
    }

    private void evenUpIngredients() {
        for (int i = 0; i < 50; i++) {
            if (allOk()) {
                break;
            }
            fatSubber();
            proteinAlterer();
            caloriesTweaker();
        }
    }

    private boolean allOk() {
        return caloriesOk() && proteinOk() && fatOk();
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

    private void subCalories() {
        double toSub = calories - meal.getCalories();
        // jos protskut ja rasva ok, vähennä vain lisäkettä, jos sitä on
        if (proteinOk() && fatOk() && meal.getSideIngredientAmount() > 0.05) {
            alterCaloriesFromSide(toSub);
            // jos protskut ok, vähennetään lisäkettä ja soosia, jos lisäkettä on
        } else if (proteinOk() && meal.getSideIngredientAmount() > 0.05) {
            alterCaloriesFromSideAndSauce(toSub);
            // jos protskut tai rasva ok ja joko soosi tai lisäke on lähellä nollaa, vähennetään pääraaka-ainetta
        } else if (proteinOk() && (meal.getSideIngredientAmount() < 0.1 || meal.getSauceAmount() < 0.1)) {
            alterCaloriesFromMain(toSub);
            // jos rasva ok, vähennetään pääraaka-ainetta ja lisuketta, jos lisuketta on
        } else if (fatOk() && meal.getSideIngredientAmount() > 0.05) {
            alterCaloriesFromMainAndSide(toSub);
            // tai sitten vähennetään vähän kaikkia
        } else {
            alterCaloriesFromAll(toSub);
        }
        if (meal.getSideIngredientAmount() < 0) {
            meal.setSideIngredientAmount(0);
        }
        if (meal.getSauceAmount() < 0) {
            meal.setSauceAmount(0);
        }
    }

    private void addCalories() {
        double toAdd = calories - meal.getCalories();
        // jos protskut ja rasva ok, lisää vain lisäkettä
        if (proteinOk() && fatOk()) {
            alterCaloriesFromSide(toAdd);
        } else if (proteinOk()) {
            alterCaloriesFromSideAndSauce(toAdd);
        } else {
            alterCaloriesFromAll(toAdd);
        }
    }

    private boolean proteinOk() {
        return (meal.getProtein() <= protein + 2 && meal.getProtein() >= protein - 2);
    }

    private boolean caloriesOk() {
        return (meal.getCalories() <= calories + 20 && meal.getCalories() >= calories - 20);
    }

    private boolean fatOk() {
        return (meal.getFat() <= fat + 2 && meal.getFat() >= fat - 2);
    }

    private void alterCaloriesFromSide(double toAlter) {
        double amountToSubFromSide = mc.calculateAmountForCalories(toAlter, meal.getSideIngredient());
        meal.setSideIngredientAmount(meal.getSideIngredientAmount() + amountToSubFromSide);
    }

    private void alterCaloriesFromSideAndSauce(double toAlter) {
        double amountToSubFromSide = mc.calculateAmountForCalories((toAlter / 8 * 5), meal.getSideIngredient());
        double amountToSubFromSauce = mc.calculateAmountForCalories((toAlter / 8 * 3), meal.getSauce());
        meal.setSideIngredientAmount(meal.getSideIngredientAmount() + amountToSubFromSide);
        meal.setSauceAmount(meal.getSauceAmount() + amountToSubFromSauce);
    }

    private void alterCaloriesFromAll(double toAlter) {
        double amountToSubFromSide = mc.calculateAmountForCalories((toAlter / 8 * 5), meal.getSideIngredient());
        double amountToSubFromSauce = mc.calculateAmountForCalories((toAlter / 8 * 2), meal.getSauce());
        double amountToSubFromMain = mc.calculateAmountForCalories(toAlter / 8, meal.getMainIngredient());
        meal.setSideIngredientAmount(meal.getSideIngredientAmount() + amountToSubFromSide);
        meal.setSauceAmount(meal.getSauceAmount() + amountToSubFromSauce);
        meal.setMainIngredientAmount(meal.getMainIngredientAmount() + amountToSubFromMain);
    }

    private void alterCaloriesFromMainAndSide(double toAlter) {
        double amountToSubFromSide = mc.calculateAmountForCalories((toAlter / 4 * 3), meal.getSideIngredient());
        double amountToSubFromMain = mc.calculateAmountForCalories(toAlter / 4, meal.getMainIngredient());
        meal.setSideIngredientAmount(meal.getSideIngredientAmount() + amountToSubFromSide);
        meal.setMainIngredientAmount(meal.getMainIngredientAmount() + amountToSubFromMain);
    }

    private void alterCaloriesFromMain(double toAlter) {
        double amountToSubFromMain = mc.calculateAmountForCalories(toAlter, meal.getMainIngredient());
        meal.setMainIngredientAmount(meal.getMainIngredientAmount() + amountToSubFromMain);
    }
}
