/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.domain;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kari
 */
public class MealTest {
    private Meal meal;
    private final double delta = 0.0001;
    
    public MealTest() {
    }
    
    @Before
    public void setUp() {
        meal = new Meal();
    }
    
     @Test
     public void testGetProteinWhenEmpty() {
         double toTest = meal.getProtein();
         assertEquals(0, toTest, delta);
     }
     
     @Test
     public void testGetProteinWhenAmountButNoIngredient() {
         meal.setMainIngredientAmount(120.4);
         double toTest = meal.getProtein();
         assertEquals(0, toTest, delta);
     }
     
     @Test
     public void testGetProteinWhenIngredientButAmountZero() {
         Ingredient ing = new Ingredient(1, "test");
         ing.setProtein(20.5);
         meal.setMainIngredient(ing);
         double toTest = meal.getProtein();
         assertEquals(0, toTest, delta);
     }
     
     @Test
     public void testGetProtein() {
         Ingredient ing = new Ingredient(1, "test");
         ing.setProtein(20.5);
         meal.setMainIngredient(ing);
         meal.setMainIngredientAmount(10);
         double toTest = meal.getProtein();
         assertEquals(205, toTest, delta);
     }
     
     @Test
     public void testGetFiber() {
         Ingredient ing = new Ingredient(1, "test");
         ing.setFiber(10.5);
         meal.setMainIngredient(ing);
         meal.setMainIngredientAmount(10);
         double toTest = meal.getFiber();
         assertEquals(105, toTest, delta);
     }
     
     @Test
     public void testGetCarbs() {
         Ingredient ing = new Ingredient(1, "test");
         ing.setCarb(30.5);
         meal.setMainIngredient(ing);
         meal.setMainIngredientAmount(10);
         double toTest = meal.getCarbs();
         assertEquals(305, toTest, delta);
     }
     
     @Test
     public void testGetCalories() {
         Ingredient ingredient = new Ingredient(100000, "test");
         Ingredient ingredient2 = new Ingredient(100000, "test");
         Ingredient ingredient3 = new Ingredient(100000, "test");
         Ingredient ingredient4 = new Ingredient(100000, "test");
         ingredient.setCalories(20);
         meal.setMainIngredient(ingredient);
         meal.setMainIngredientAmount(10);
         ingredient2.setCalories(100);
         meal.setSauce(ingredient2);
         meal.setSauceAmount(1);
         ingredient3.setCalories(50);
         meal.setMisc(ingredient3);
         meal.setMiscAmount(1);
         ingredient4.setCalories(25);
         meal.setSideIngredient(ingredient4);
         meal.setSideIngredientAmount(1);
         double toTest = meal.getCalories();
         assertEquals(375, toTest, delta);
     }
     
     
}
