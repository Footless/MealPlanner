/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.util;

import kari.nutritionplanner.mealplanner.domain.Ingredient;
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
public class MacroCalculatorTest {
    MacroCalculator mc;
    private double delta = 0.0001;
    
    public MacroCalculatorTest() {
    }
    
    @Before
    public void setUp() {
         mc = new MacroCalculator();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testEmptyMainIngredient() {
        Ingredient ing = new Ingredient(1, "test");
        double toTest = mc.calculateAmountForProtein(40, ing);
        assertEquals(0, toTest, delta);
    }
    
    @Test
    public void testEmptySauceIngredient() {
        Ingredient ing = new Ingredient(1, "test");
        double toTest = mc.calculateAmountForFat(40, ing);
        assertEquals(0, toTest, delta);
    }
    
    @Test
    public void testEmptySideIngredient() {
        Ingredient ing = new Ingredient(1, "test");
        double toTest = mc.calculateAmountForCalories(40, ing);
        assertEquals(0, toTest, delta);
    }
    
    @Test
    public void testMainIngredientCalculator() {
        Ingredient ing = new Ingredient(1, "test");
        ing.setProtein(20.0);
        double toTest = mc.calculateAmountForProtein(40, ing);
        assertEquals(2.0, toTest, delta);
    }
    
    @Test
    public void testSauceCalculator() {
        Ingredient ing = new Ingredient(1, "test");
        ing.setFat(20.0);
        double toTest = mc.calculateAmountForFat(40, ing);
        assertEquals(2.0, toTest, delta);
    }
    
    @Test
    public void testSideCalculator() {
        Ingredient ing = new Ingredient(1, "test");
        ing.setCalories(20.0);
        double toTest = mc.calculateAmountForCalories(40, ing);
        assertEquals(2.0, toTest, delta);
    }
}
