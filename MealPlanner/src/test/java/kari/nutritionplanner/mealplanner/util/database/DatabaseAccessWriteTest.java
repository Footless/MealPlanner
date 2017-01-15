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
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kari
 */
public class DatabaseAccessWriteTest {

    private DatabaseAccessWrite dbWriter;

    public DatabaseAccessWriteTest() {
    }

    @Before
    public void setUp() {
        this.dbWriter = new DatabaseAccessWrite();
    }

    @Test
    public void testAddIngredientWithSomethingAlreadyThere() throws SQLException {
        dbWriter.removeUserIngredient(new Ingredient(34256, "Kirjolohi, kasvatettu"), "mains");
        assertTrue(dbWriter.addIntoUserIngredients(new Ingredient(34256, "Kirjolohi, kasvatettu"), "mains"));
        assertFalse(dbWriter.addIntoUserIngredients(new Ingredient(34256, "Kirjolohi, kasvatettu"), "mains"));
        assertTrue(dbWriter.removeUserIngredient(new Ingredient(34256, "Kirjolohi, kasvatettu"), "mains"));
        assertFalse(dbWriter.removeUserIngredient(new Ingredient(34256, "Kirjolohi, kasvatettu"), "mains"));
    }

    @Test
    public void testAddAndRemoveClosedDatabase() {
        dbWriter.closeConnection();
        assertFalse(dbWriter.addIntoUserIngredients(new Ingredient(34256, "Kirjolohi, kasvatettu"), "mains"));
        assertFalse(dbWriter.removeUserIngredient(new Ingredient(34256, "Kirjolohi, kasvatettu"), "mains"));
    }

    @Test
    public void testCloser() {
        assertTrue(dbWriter.closeConnection());
    }
}
