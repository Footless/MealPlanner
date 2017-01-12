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
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.servicelayer.IngredientSearchHelper;

/**
 * ActionListener joka suorittaa hakusanan perusteella haun kaikista Finelin
 * tietokannan raaka-aineista, ja tulostaa JListina hakusanan sisältävät
 * raaka-aineet.
 *
 * @author kari
 */
public class SearchIngListener implements ActionListener {

    private final JTextField searchField;
    private final IngredientSearchHelper searchHelper;
    private final DefaultListModel listModel;

    public SearchIngListener(JTextField searchField, DefaultListModel listModel, IngredientSearchHelper searchHelper) {
        this.searchField = searchField;
        this.listModel = listModel;
        this.searchHelper = searchHelper;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String searchTerm = searchField.getText();
        searchField.setText("");

        List<Ingredient> ings = searchHelper.search(searchTerm);
        if (!listModel.isEmpty()) {
            listModel.clear();
        }
        if (ings.isEmpty()) {
            JOptionPane.showMessageDialog(searchField, "Raaka-aineita ei löytynyt");
        } else {
            for (int i = 0; i < ings.size(); i++) {
                listModel.addElement(ings.get(i).getName());
            }
        }
    }
}
