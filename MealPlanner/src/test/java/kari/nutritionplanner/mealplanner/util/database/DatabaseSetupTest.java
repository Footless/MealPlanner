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

import java.io.IOException;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kari
 */
public class DatabaseSetupTest {
    
    public DatabaseSetupTest() {
    }
    
    @Test
    public void testConstructor() throws SQLException, IOException {
        DatabaseSetup db = new DatabaseSetup();
        DatabaseAccess dbAccess = new DatabaseAccess();
        assertTrue(db != null);
        assertTrue(dbAccess.databaseOk());
        assertTrue(dbAccess.getUserIngredients() != null);
        assertTrue(dbAccess.getUserIngredients().size() == 4);
    }
}
 