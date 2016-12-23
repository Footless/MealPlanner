/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kari
 */
public class MealTest {
    private Meal meal;
    private double delta = 0.0001;
    
    public MealTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        meal = new Meal();
    }
    
    @After
    public void tearDown() {
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
         meal.setMainIngredientAmount(100);
         double toTest = meal.getProtein();
         assertEquals(2050, toTest, delta);
     }
}
