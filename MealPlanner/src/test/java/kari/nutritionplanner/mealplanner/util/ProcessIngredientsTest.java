/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.util;

import java.util.List;
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
    public void setUp() {
        this.pi = new ProcessIngredients(true);
    }

    @Test
    public void testIngsDatabaseClosed() {
        this.pi = new ProcessIngredients(false);
        assertFalse(pi.getDatabaseOk());
        Map<String, Map<Integer, Ingredient>> ingredients = pi.getIngredients();
        assertTrue(ingredients.containsKey("mains"));
        assertTrue(ingredients.containsKey("sides"));
        assertTrue(ingredients.containsKey("sauces"));
        assertTrue(ingredients.containsKey("sidesAndMisc"));
        Map<Integer, Ingredient> mains = ingredients.get("mains");
        Map<Integer, Ingredient> sides = ingredients.get("sides");
        Map<Integer, Ingredient> sauces = ingredients.get("sauces");
        Map<Integer, Ingredient> misc = ingredients.get("sidesAndMisc");
        assertTrue(mains.size() > 0);
        assertTrue(sides.size() > 0);
        assertTrue(sauces.size() > 0);
        assertTrue(misc.size() > 0);
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

    @Test
    public void testUpdate() {
        pi.getDbAccess().closeConnection();
        pi.updateIngs();
        assertEquals(null, pi.getIngredients().get("mains"));
        assertEquals(null, pi.getIngredients().get("sides"));
        assertEquals(null, pi.getIngredients().get("sauces"));
        assertEquals(null, pi.getIngredients().get("sidesAndMisc"));
        assertFalse(pi.getDatabaseOk());
    }

    @Test
    public void testDatabaseOk() {
        assertTrue(pi.getDatabaseOk());
    }

    @Test
    public void testGetSideIngredients() {
        List<Ingredient> ings = pi.getSideIngredients();
        assertTrue(ings.size() > 3);
    }
}
