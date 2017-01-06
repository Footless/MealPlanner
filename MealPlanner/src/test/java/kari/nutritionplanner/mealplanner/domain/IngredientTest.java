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
public class IngredientTest {
    private Ingredient ing;
    private final double delta = 0.0001;
    
    public IngredientTest() {
    }

    @Before
    public void setUp() {
        this.ing = new Ingredient(1, "test");
    }

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
         ing.setCalories(30);
         assertEquals(30, ing.getCalories(), delta);
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
