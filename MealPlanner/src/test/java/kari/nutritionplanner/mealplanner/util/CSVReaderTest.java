/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kari
 */
public class CSVReaderTest {
    CSVReader reader;
    private final double delta = 0.001;
    
    public CSVReaderTest() {
    }
    
    @Before
    public void setUp() throws FileNotFoundException {
        reader = new CSVReader("main_ingredients.csv");
    }

     @Test
     public void testNewReader() {
         assertTrue(reader != null);
     }
     
     @Test
     public void testSearchMacros() throws FileNotFoundException, IOException {
         reader = new CSVReader("component_value.csv");
         Ingredient ing = new Ingredient(8062, "test");
         reader.searchMacros(ing);
         assertEquals(79.660611855, ing.getCalories(), delta);
         assertEquals(5.473, ing.getCarb(), delta);
         assertEquals(3.762, ing.getFat(), delta);
         assertEquals(5.64, ing.getProtein(), delta);
         assertEquals(0.333, ing.getFiber(), delta);
     }
     
     @Test
     public void testSearchAllIngs() throws IOException {
         List<Ingredient> ings = reader.getAllIngredients();
         assertTrue(ings.size() > 0);
     }
     
     @Test
     public void testSearchMacrosInvalidIng() throws FileNotFoundException, IOException {
         reader = new CSVReader("component_value.csv");
         Ingredient ing = new Ingredient(56033, "test");
         assertFalse(reader.searchMacros(ing));
     }
     
     @Test
     public void testSearchMacrosNullIng() throws FileNotFoundException, IOException {
         reader = new CSVReader("component_value.csv");
         Ingredient ing = null;
         assertFalse(reader.searchMacros(ing));
     }
}
