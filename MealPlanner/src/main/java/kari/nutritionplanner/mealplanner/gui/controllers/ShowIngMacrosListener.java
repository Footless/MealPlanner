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
package kari.nutritionplanner.mealplanner.gui.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.JOptionPane;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.gui.ComponentFactory;

/**
 * Näyttää valitun raaka-aineen ravintoarvot avautuvassa ikkunassa.
 *
 * @author kari
 */
public class ShowIngMacrosListener implements ActionListener {

    private Ingredient ing;
    private final ComponentFactory compFactory;
    private final JList ingredients;

    /**
     * Konstruktori saa parametreinä ComponenFactoryn ja listan jossa
     * raaka-aineet ovat.
     *
     * @param compFactory ComponentFactory, josta saadaan apumetodit.
     * @param ingredients JList, jossa raaka-aineet joiden tietoja halutaan
     * näyttää sijaitsevat.
     */
    public ShowIngMacrosListener(ComponentFactory compFactory, JList ingredients) {
        this.ing = ing;
        this.compFactory = compFactory;
        this.ingredients = ingredients;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = ingredients.getSelectedValue().toString();
        ing = compFactory.getSearchHelper().getIngredientByName(name);
        String macros = "" + ing.getName() + ", arvot per 100g\n\n"
                + "Kalorit: " + Math.ceil(ing.getCalories()) + "kcal\n"
                + "Proteiini: " + ing.getProtein() + "g\n"
                + "Rasva: " + ing.getFat() + "g\n"
                + "Hiilihydraatit: " + ing.getCarb() + "g\n"
                + "Kuidut: " + ing.getFiber() + "g\n";
        JOptionPane.showMessageDialog(null, macros, "Ravintotiedot", 1);
    }
}
