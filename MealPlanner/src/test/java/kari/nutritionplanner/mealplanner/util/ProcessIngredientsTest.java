/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.util;

import java.io.IOException;
import java.util.Map;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kari
 */
public class ProcessIngredientsTest {
    private final double delta = 0.0001;
    private ProcessIngredients pi;
    
    public ProcessIngredientsTest() {
    }
    
    @Before
    public void setUp() throws IOException {
        this.pi = new ProcessIngredients();
    }

     @Test
     public void testGetIngredients() {
         Map<String, Map<Integer, Ingredient>> ingredients = pi.getIngredients();
         assertTrue(ingredients.containsKey("mains"));
         assertTrue(ingredients.containsKey("sides"));
         assertTrue(ingredients.containsKey("sauces"));
         assertTrue(ingredients.containsKey("sidesAndMisc"));
         Map<Integer, Ingredient> mains = ingredients.get("mains");
         assertTrue(mains.size() > 0);
     }
     
//     @Test
//     public void testGetIngredientNotEmpty() {
//         Map<String, Map<String, Ingredient>> ingredients = pi.getIngredients();
//         Map<String, Ingredient> mains = ingredients.get("mains");
//         assertTrue(mains.size() > 0);
//     }
}
