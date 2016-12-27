/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.servicelayer;

import java.util.Map;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.domain.Meal;
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
public class CalculateMealTest {

    private CalculateMeal cm;
    private Meal meal;

    public CalculateMealTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.cm = new CalculateMeal();
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void testGetIngredientsCreated() {
        Map<String, Map<Integer, Ingredient>> ings = cm.getIngredients();
        assertTrue(ings.size() > 0);
    }
    
    @Test
    public void testGetIngredientsNotEmpty() {
        Map<String, Map<Integer, Ingredient>> ings = cm.getIngredients();
        Map<Integer, Ingredient> mains = ings.get("mains");
        assertTrue(mains.size() > 0);
        assertEquals("Kana", mains.get(750).getName());
    }
}
