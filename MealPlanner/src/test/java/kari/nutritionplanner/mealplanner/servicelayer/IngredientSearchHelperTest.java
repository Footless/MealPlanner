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

import java.io.IOException;
import java.util.List;
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
public class IngredientSearchHelperTest {

    private IngredientSearchHelper helper;
    private double delta = 0.0001;

    public IngredientSearchHelperTest() {
    }

    @Before
    public void setUp() throws IOException {
        this.helper = new IngredientSearchHelper();
    }

    @Test
    public void testConstructor() {
        assertTrue(this.helper != null);
    }
    
    @Test
    public void testConstructorWithFileName() throws IOException {
        this.helper = new IngredientSearchHelper("sauces.csv");
        assertTrue(this.helper != null);
    }
    
    @Test
    public void testSearch() throws IOException {
        List<Ingredient> ings = helper.search("kuha");
        assertEquals(1, ings.size());
    }
    
    @Test
    public void testAddMacros() throws IOException {
        List<Ingredient> ings = helper.search("kuha");
        Ingredient ing = ings.get(0);
        assertTrue(helper.addMacros(ing));
        assertEquals(79.641491396, ing.getCalories(), delta);
        assertEquals(18.658, ing.getProtein(), delta);
        assertEquals(0.433, ing.getFat(), delta);
        assertEquals(0, ing.getCarb(), delta);
        assertEquals(0, ing.getFiber(), delta);
    }
}
