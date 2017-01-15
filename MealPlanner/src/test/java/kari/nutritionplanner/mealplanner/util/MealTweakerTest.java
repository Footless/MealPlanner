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
import kari.nutritionplanner.mealplanner.servicelayer.MealCalcHelper;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kari
 */
public class MealTweakerTest {

    private MealTweaker mt;
    private CalculateMeal cm;
    private MealCalcHelper helper;
    private final double delta = 0.0001;
    private final double caloriesDelta = 15.5;
    private final double proteinAndFatDelta = 5.5;
    private Meal meal;

    public MealTweakerTest() {
    }

    @Before
    public void setUp() {
        ProcessIngredients pi = new ProcessIngredients(true);
        this.cm = new CalculateMeal(pi);
        this.helper = new MealCalcHelper(pi);
        meal = new Meal();
        meal.setMainIngredient(cm.getIngredients().get("mains").get(helper.getIdForMainIng("kuha")));
        meal.setSideIngredient(cm.getIngredients().get("sides").get(204));
        meal.setSauce(cm.getIngredients().get("sauces").get(5009));
        meal.setSauce(cm.getIngredients().get("sidesAndMisc").get(33182));
        meal.setMainIngredientAmount(1.5);
        meal.setSideIngredientAmount(1.1);
        meal.setSauceAmount(0.4);
        meal.setMiscAmount(0.5);
        mt = new MealTweaker(meal, 500, 30, 5);
    }

    @Test
    public void testConstructor() {
        assertTrue(this.mt != null);
    }
    
//    @Test
//    public void testTweakerCal() {
//        mt.tweakMeal();
//        assertEquals(500, meal.getCalories(), caloriesDelta);
//    }
//    
//    @Test
//    public void testTweakerProt() {
//        mt.tweakMeal();
//        assertEquals(30, meal.getProtein(), proteinAndFatDelta);
//    }
//    
//    @Test
//    public void testTweakerFat() {
//        mt.tweakMeal();
//        assertEquals(5, meal.getFat(), proteinAndFatDelta);
//    }
    
    @Test
    public void testRounder() {
        meal.setMainIngredient(cm.getIngredients().get("mains").get(805));
        meal.setMainIngredientAmount(1.456345);
        meal.setSideIngredientAmount(1.456345);
        meal.setSauceAmount(1.456345);
        mt.roundUpIngredients();
        assertEquals(1.46, meal.getMainIngredientAmount(), 0.0);
        assertEquals(1.46, meal.getSideIngredientAmount(), 0.0);
        assertEquals(1.46, meal.getSauceAmount(), 0.0);
    }
    
    @Test
    public void testZeroChecker() {
        mt.getMeal().setSauceAmount(-0.2);
        mt.getMeal().setSideIngredientAmount(-0.6);
        mt.checkLessThanZeros();
        assertEquals(0, mt.getMeal().getSideIngredientAmount(), delta);
        assertEquals(0, mt.getMeal().getSauceAmount(), delta);
    }
}
