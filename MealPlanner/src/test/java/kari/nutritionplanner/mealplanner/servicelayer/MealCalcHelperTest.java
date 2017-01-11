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

import kari.nutritionplanner.mealplanner.util.ProcessIngredients;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kari
 */
public class MealCalcHelperTest {
    private CalculateMeal cm;
    private MealCalcHelper helper;

    public MealCalcHelperTest() {
    }

    @Before
    public void setUp() {
        ProcessIngredients pi = new ProcessIngredients(true);
        this.cm = new CalculateMeal(pi);
        this.helper = new MealCalcHelper(pi);
    }

    @Test
    public void testDesiredSetters() {
        helper.setDesiredCalories(10);
        helper.setDesiredFat(20);
        helper.setDesiredProtein(30);
        assertEquals(10, helper.getDesiredCalories());
        assertEquals(20, helper.getDesiredFat());
        assertEquals(30, helper.getDesiredProtein());
    }

    @Test
    public void testMainIngSetter() {
        helper.setMainIngredient("Kuha");
        assertEquals(805, helper.getMainIngredientId());
        helper.setMainIngredient("kuha");
        assertEquals(805, helper.getMainIngredientId());
        helper.setMainIngredient("Tofu, soijavalmiste, soijapapujuusto");
        assertEquals(33501, helper.getMainIngredientId());
        helper.setMainIngredient("Tofu");
        assertEquals(33501, helper.getMainIngredientId());
    }

    @Test
    public void testClear() {
        helper.setDesiredCalories(10);
        helper.setDesiredFat(20);
        helper.setDesiredProtein(30);
        assertEquals(10, helper.getDesiredCalories());
        assertEquals(20, helper.getDesiredFat());
        assertEquals(30, helper.getDesiredProtein());
        helper.setMainIngredient("Kuha");
        assertEquals(805, helper.getMainIngredientId());
        helper.clear();
        assertEquals(0, helper.getDesiredCalories());
        assertEquals(0, helper.getDesiredFat());
        assertEquals(0, helper.getDesiredProtein());
        assertEquals(0, helper.getMainIngredientId());
    }
    
    @Test
    public void testSetSideIngredient() {
        helper.setSideIngredient("peruna");
        assertEquals(204, helper.getSideIngredientId());
        helper.setSideIngredient("testijajotainvielä");
        assertEquals(204, helper.getSideIngredientId());
        assertEquals(0, helper.getIdForSideIng("kuha"));
        helper.setSideIngredient("misc");
        assertEquals(99999, helper.getSideIngredientId());
    }
    
    @Test
    public void testIngsAsMap() {
        assertEquals(5, helper.getMainIngredientsAsMap().size());
        assertEquals(4, helper.getSideIngredientsAsMap().size());
    }
    
    @Test
    public void testGetSidesList() {
        assertEquals(4, helper.getSideIngredients().size());
    }
}
 