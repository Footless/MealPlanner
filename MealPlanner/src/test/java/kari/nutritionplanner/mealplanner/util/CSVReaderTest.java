/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import javax.swing.JOptionPane;

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
    public void setUp() {
        reader = new CSVReader("main_ingredients.csv");
    }

     @Test
     public void testNewReader() {
         assertTrue(reader != null);
     }
     
     @Test
     public void testSearchMacros() throws IOException {
         reader = new CSVReader("component_value.csv");
         Ingredient ing = new Ingredient(8062, "test");
         reader.searchMacros(ing);
         assertEquals(79.660611, ing.getCalories(), delta);
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
     public void testSearchMacrosInvalidIng() throws IOException {
         reader = new CSVReader("component_value.csv");
         Ingredient ing = new Ingredient(56033, "test");
         assertFalse(reader.searchMacros(ing));
     }
     
     @Test
     public void testSearchMacrosNullIng() throws IOException {
         reader = new CSVReader("component_value.csv");
         Ingredient ing = null;
         assertFalse(reader.searchMacros(ing));
         assertTrue(reader.searchMacros(new Ingredient(805, "Kuha")));
     }
     
     @Rule
     public ExpectedException thrown = ExpectedException.none();
     
     @Test
     public void testConstructor() {
         thrown.equals(JOptionPane.class);
         CSVReader reader2 = new CSVReader("null.csv");
     }
     
     @Test
     public void testSearchAllMacros() throws IOException {
         reader = new CSVReader("component_value.csv");
         List<Ingredient> ings = new ArrayList<>();
         ings.add(new Ingredient(805, "Kuha"));
         ings.add(new Ingredient(34307, "Nyhtökaura, nude"));
         reader.searchAllMacros(ings);
         assertEquals(18.658, ings.get(0).getProtein(), delta);
         assertEquals(0.433, ings.get(0).getFat(), delta);
         assertEquals(31.2, ings.get(1).getProtein(), delta);
         assertEquals(4.9, ings.get(1).getFat(), delta);
         reader.closeReader();
     }
     
     @Test(expected = IOException.class)
     public void testExceptionOnClosedReader() throws IOException {
         reader = new CSVReader("component_value.csv");
         List<Ingredient> ings = reader.getAllIngredients();
         assertTrue(ings.size() > 3000);
         reader.closeReader();
         reader.getAllIngredients();
     }
}
