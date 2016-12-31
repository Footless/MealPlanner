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
    private double delta = 0.0001;
    private double bigDelta = 2.5;
    private double hugeDelta = 5.5;

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
    
    @Test
    public void testMainIngSetter() {
        boolean success = cm.setMainIngredient(805, 25, 15);
        assertTrue(success);
        double protein = cm.getMeal().getProtein();
        assertEquals(25, protein, delta);
        assertEquals(1.339907814, cm.getMeal().getMainIngredientAmount(), delta);
        assertEquals(0.58018, cm.getMeal().getFat(), delta);
    }
    
    @Test
    public void testSauceSetter() {
        cm.setMainIngredient(805, 25, 15);
        boolean success = cm.setSauce(30);
        assertTrue(success);
        assertEquals(30, cm.getMeal().getFat(), delta);
        assertEquals(0.693896408, cm.getMeal().getSauceAmount(), delta);
    }
    
    @Test 
    public void testMiscSetter() {
        cm.setMisc();
        double calories = cm.getMeal().getCalories();
        assertEquals(22.474904398, calories, delta);
    }
    
    @Test
    public void testSideSetterOnEmptyMeal() {
        cm.setSideIngredient(400);
        assertEquals(400, cm.getMeal().getCalories(), delta);
    }
    
    @Test
    public void testSideSetter() {
        cm.setMainIngredient(805, 25, 15);
        cm.setSauce(30);
        cm.setMisc();
        cm.setSideIngredient(800);
        assertEquals(800, cm.getMeal().getCalories(), delta);
    }
    
    @Test
    public void testSetWholeMeal() {
        assertTrue(cm.calculateAllMeal(805, 500, 25, 15));
        assertEquals(500, cm.getMeal().getCalories(), hugeDelta);
        assertEquals(25, cm.getMeal().getProtein(), bigDelta);
        assertEquals(15, cm.getMeal().getFat(), bigDelta);
    }
    
}
