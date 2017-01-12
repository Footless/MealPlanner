



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
package kari.nutritionplanner.mealplanner.util.database;

import java.sql.SQLException;
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
public class DatabaseAccessReadTest {

    private DatabaseAccessRead dbAccess;

    public DatabaseAccessReadTest() {
    }

    @Before
    public void setUp() {
        this.dbAccess = new DatabaseAccessRead();
    }

    @Test
    public void testConstructor() throws SQLException {
        assertTrue(dbAccess != null);
        assertTrue(dbAccess.databaseOk());
    }

    @Test
    public void testGetIngredientById() throws SQLException {
        if (dbAccess.databaseOk()) {
            Ingredient ing = dbAccess.getIngredient(805);
            assertEquals("Kuha", ing.getName());
            assertTrue(ing.getCalories() > 0);
            assertTrue(ing.getProtein() > 0);
        }

    }

    @Test
    public void testGetIngredientByName() throws SQLException {
        if (dbAccess.databaseOk()) {
            int id = dbAccess.getIngredientIdByName("Kuha");
            assertEquals(805, id);
        }
    }

    @Test
    public void testSearch() throws SQLException {
        if (dbAccess.databaseOk()) {
            List<Ingredient> ings = dbAccess.searchIngredients("Kana");
            assertEquals(165, ings.size());
            for (Ingredient ing : ings) {
                assertTrue(ing.getName().toLowerCase().contains("kana"));
            }
        }
    }

    @Test
    public void testGetUserIngs() throws SQLException {
        if (dbAccess.databaseOk()) {
            Map<String, Map<Integer, Ingredient>> ingsMap = dbAccess.getUserIngredients();
            assertTrue(ingsMap.size() > 3);
            assertTrue(ingsMap.get("mains").size() > 4);
            assertTrue(ingsMap.get("sides").size() > 3);
            assertTrue(ingsMap.get("sauces").size() > 0);
            assertTrue(ingsMap.get("sidesAndMisc").size() > 0);
            for (Object object : ingsMap.values()) {
                Map<Integer, Ingredient> ings = (Map) object;
                for (Ingredient ing : ings.values()) {
                    assertTrue(ing != null);
                }
            }
        }
    }
}