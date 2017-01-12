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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.servicelayer.IngredientSearchHelper;

/**
 * ActionListener, joka käsittelee uuden raaka-aineen lisäämistä valittavissa
 * oleviin raaka-aineisiin. 11.1.2017 yhä keskeneräinen.
 *
 * @author kari
 */
public class AddToIngsListener implements ActionListener {

    private final JList list;
    private final IngredientSearchHelper searchHelper;
    private String category;

    public AddToIngsListener(JPanel searchFieldComp, IngredientSearchHelper searchHelper) {
        JScrollPane scroll = (JScrollPane) searchFieldComp.getComponent(0);
        this.list = (JList) scroll.getViewport().getComponent(0);
        this.searchHelper = searchHelper;
        this.category = "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setCategory(e);
        String name = (String) list.getSelectedValue();
        if (e.getActionCommand().equalsIgnoreCase("mains")) {
            int response = showConfirmDialog(e, name);
            if (response == JOptionPane.YES_OPTION) {
                addToIngredients(name, e.getActionCommand());
            }
        } else if (e.getActionCommand().equalsIgnoreCase("sides")) {
            int response = showConfirmDialog(e, name);
            if (response == JOptionPane.YES_OPTION) {
                addToIngredients(name, e.getActionCommand());
            }
        } else if (e.getActionCommand().equalsIgnoreCase("sauces")) {
            int response = showConfirmDialog(e, name);
            if (response == JOptionPane.YES_OPTION) {
                addToIngredients(name, e.getActionCommand());
            }
        }
    }
    
    private int showConfirmDialog(ActionEvent e, String name) {
        int response = JOptionPane.showConfirmDialog(null, "Haluako todella lisätä raaka-aineen " + name + " tietokantaan "
                    + category + "?", "Varmista", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return response;
    }

    private void addToIngredients(String name, String select) {
        Ingredient ing = searchHelper.getIngredientByName(name);
        boolean success = searchHelper.addIngredientToDatabase(ing, select);
        if (!success) {
            JOptionPane.showMessageDialog(null, "Raaka-aineen lisääminen tietokantaan epäonnistui.");
        } else {
            JOptionPane.showMessageDialog(null, "" + name + " lisätty tietokantaan " + category, "Raaka-aine lisätty", 1);
        }
    }

    private void setCategory(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("mains")) {
            category += "pääraaka-aineet";
        } else if (e.getActionCommand().equalsIgnoreCase("sides")) {
            category += "lisäkkeet";
        } else if (e.getActionCommand().equalsIgnoreCase("sauces")) {
            category += "kastikkeet";
        }
    }
}
