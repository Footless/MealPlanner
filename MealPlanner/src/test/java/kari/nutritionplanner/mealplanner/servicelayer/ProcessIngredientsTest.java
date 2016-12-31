/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.servicelayer;

import kari.nutritionplanner.mealplanner.util.ProcessIngredients;
import java.util.Map;
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
public class ProcessIngredientsTest {
    private double delta = 0.0001;
    private ProcessIngredients pi;
    
    public ProcessIngredientsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.pi = new ProcessIngredients();
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
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
