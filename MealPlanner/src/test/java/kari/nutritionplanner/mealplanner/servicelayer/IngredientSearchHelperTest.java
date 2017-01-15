/*
 * Copyright (C) 2017 kari
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package kari.nutritionplanner.mealplanner.servicelayer;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.util.ProcessIngredients;
import kari.nutritionplanner.mealplanner.util.database.DatabaseAccessRead;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kari
 */
public class IngredientSearchHelperTest {

    private IngredientSearchHelper helper;
    private double delta = 0.0001;

    public IngredientSearchHelperTest() {
    }

    @Before
    public void setUp() {
        DatabaseAccessRead dbAccess = new DatabaseAccessRead();
        try {
            this.helper = new IngredientSearchHelper(new ProcessIngredients(dbAccess.databaseOk()));
        } catch (SQLException ex) {
            Logger.getLogger(IngredientSearchHelperTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testConstructor() {
        assertTrue(this.helper != null);
    }

    @Test
    public void testConstructorClosedDatabase() {
        this.helper = new IngredientSearchHelper(new ProcessIngredients(false));
        assertEquals(null, helper.getIngredientByName("kuha"));
        assertFalse(helper.removeIngredientFromDB(new Ingredient(0, "test"), "test"));
        assertFalse(helper.addIngredientToDatabase(new Ingredient(0, "test"), "test"));
        helper.getReader().closeReader();
        assertEquals(null, helper.search("kana"));
    }

    @Test
    public void testGetProcessIngredients() {
        assertTrue(helper.getIngredientProcessor() instanceof ProcessIngredients);
    }

    @Test
    public void testSearch() {
        List<Ingredient> ings = helper.search("Kuha");
        assertEquals(1, ings.size());
        List<Ingredient> ings2 = helper.search("4334");
        assertEquals(0, ings2.size());
        List<Ingredient> ings4 = helper.search(";");
        assertTrue(ings4 != null);
    }

    @Test
    public void testSearchDatabaseOff() {
        helper = new IngredientSearchHelper(new ProcessIngredients(false));
        List<Ingredient> ings = helper.search("Kuha");
        assertEquals(1, ings.size());
        List<Ingredient> ings2 = helper.search("4334");
        assertEquals(0, ings2.size());
        List<Ingredient> ings3 = helper.search("4334");
        assertTrue(ings3 != null);
        List<Ingredient> ings4 = helper.search(";");
        assertTrue(ings4 != null);
    }

    @Test
    public void testSearchOnClosedConnection() {
        helper.getDbAccess().closeConnection();
        assertEquals(null, helper.search("kuha"));
    }

    @Test
    public void testGetIngredientByName() {
        assertEquals(new Ingredient(805, "Kuha").getId(), helper.getIngredientByName("kuha").getId());
    }

    @Test
    public void testGetIngredientByNameDatabaseClosed() {
        helper.getDbAccess().closeConnection();
        assertEquals(null, helper.getIngredientByName("kuha"));
    }

    @Test
    public void testAddAndRemoveIngredient() {
        if (helper.getIngredientProcessor().getIngredients().get("sides").containsKey(34110)) {
            helper.removeIngredientFromDB(new Ingredient(34110, "Maizena suuruste ruskea/vaalea"), "sides");
        }
        assertTrue(helper.addIngredientToDatabase(new Ingredient(34110, "Maizena suuruste ruskea/vaalea"), "sides"));
        assertEquals(34110, helper.getIngredientByName("Maizena suuruste ruskea/vaalea").getId());
        assertEquals("Maizena suuruste ruskea/vaalea", helper.getIngredientProcessor().getIngredients().get("sides").get(34110).getName());
        assertTrue(helper.removeIngredientFromDB(new Ingredient(34110, "Maizena suuruste ruskea/vaalea"), "sides"));
        assertFalse(helper.getIngredientProcessor().getIngredients().get("mains").containsKey(34110));
    }
    
    @Test
    public void testAddIngClosedConnection() {
        helper.getDbWriter().closeConnection();
        assertFalse(helper.addIngredientToDatabase(new Ingredient(34110, "Maizena suuruste ruskea/vaalea"), "sides"));
    }
    
    @Test
    public void testRemoveIngClosedConnection() {
        helper.getDbWriter().closeConnection();
        assertFalse(helper.removeIngredientFromDB(new Ingredient(34110, "Maizena suuruste ruskea/vaalea"), "mains"));
    }
}
