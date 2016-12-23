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
public class IngredientTest {
    private Ingredient ing;
    double delta = 0.0001;
    
    public IngredientTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.ing = new Ingredient(1, "test");
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void testConstructor() {
         int id = ing.getId();
         String name = ing.toString();
         assertEquals("test", name);
         assertEquals(1, id);
     }
     
     @Test
     public void testName() {
         assertEquals("test", ing.getName());
     }
     
     @Test
     public void testCalories() {
         ing.setCalories(140.3);
         double toTest = ing.getCalories();
         assertEquals(140.3, toTest, delta);
     }
     
     @Test
     public void testFiber() {
         ing.setFiber(140.3);
         double toTest = ing.getFiber();
         assertEquals(140.3, toTest, delta);
     }
     
     @Test
     public void testCarb() {
         ing.setCarb(140.3);
         double toTest = ing.getCarb();
         assertEquals(140.3, toTest, delta);
     }
     
     @Test
     public void testFat() {
         ing.setFat(140.3);
         double toTest = ing.getFat();
         assertEquals(140.3, toTest, delta);
     }
     
     @Test
     public void testProtein() {
         ing.setProtein(140.3);
         double toTest = ing.getProtein();
         assertEquals(140.3, toTest, delta);
     }
}
