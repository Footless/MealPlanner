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
public class SCVReaderTest {
    SCVReader reader;
    double delta = 0.001;
    
    public SCVReaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        reader = new SCVReader("main_ingredients.csv");
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void testNewReader() {
         assertTrue(reader != null);
     }
     
     @Test
     public void testSearchMacros() {
         reader = new SCVReader("component_value.csv");
         Ingredient ing = new Ingredient(8062, "test");
         reader.searchMacros(ing);
         assertEquals(79.660611855, ing.getCalories(), delta);
         assertEquals(5.473, ing.getCarb(), delta);
         assertEquals(3.762, ing.getFat(), delta);
         assertEquals(5.64, ing.getProtein(), delta);
         assertEquals(0.333, ing.getFiber(), delta);
     }
}
