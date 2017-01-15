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
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.gui.BrowseIngredientsView;
import kari.nutritionplanner.mealplanner.gui.ComponentFactory;
import kari.nutritionplanner.mealplanner.servicelayer.IngredientSearchHelper;

/**
 * ActionListenerin toteuttava luokka joka poistaa valitun raaka-aineen
 * tietokannasta.
 *
 * @author kari
 */
public class RemoveIngListener implements ActionListener {

    private final IngredientSearchHelper searchHelper;
    private final JList ingList;
    private final String select;
    private String category;
    private final DefaultListModel listModel;
    private final BrowseIngredientsView view;

    /**
     * Konstruktori saa parametrina JListin josta poistettava raaka-aine
     * valitaan, String select joka kuvaa kategoriaa mistä raaka-aine poistetaan
     * sekä DefaulListModelin jossa raaka-aineiden nimet sijaitsevat sekä useita
     * apuluokkia.
     *
     * @param compFactory ComponentFactory, jonka kautta saadaan IngredientSearchHelper
     * @param ingList JList, josta raaka-aineet valitaan
     * @param select Kategoria raaka-aineelle
     * @param listModel DefaultListModel, missä raaka-aineiden nimet ovat
     * @param view BrowseIngredientsView, luokka missä raaka-ainelista sijaitsee.
     */
    public RemoveIngListener(ComponentFactory compFactory, JList ingList, String select, DefaultListModel listModel, BrowseIngredientsView view) {
        this.searchHelper = compFactory.getSearchHelper();
        this.ingList = ingList;
        this.select = select;
        this.category = "";
        this.listModel = listModel;
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setCategory(select);
        String name = (String) ingList.getSelectedValue();
        Ingredient ing = searchHelper.getIngredientByName(name);
        int response = JOptionPane.showConfirmDialog(null, "Haluako todella poistaa raaka-aineen " + ing.getName() + " tietokannasta "
                + category + "?", "Varmista", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            if (searchHelper.removeIngredientFromDB(ing, select)) {
                JOptionPane.showMessageDialog(null, "Raaka-aine " + ing.getName() + " poistettu tietokannasta " + category);
                searchHelper.getIngredientProcessor().updateIngs();
                view.upgradeIngList(listModel, select);
            } else {
                JOptionPane.showMessageDialog(null, "Raaka-aineen " + ing.getName() + " poistaminen tietokannasta " + category + " epäonnistui.");
            }
        }
    }

    private void setCategory(String select) {
        if (select.equalsIgnoreCase("mains")) {
            category += "pääraaka-aineet";
        } else if (select.equalsIgnoreCase("sides")) {
            category += "lisäkkeet";
        } else if (select.equalsIgnoreCase("sauces")) {
            category += "kastikkeet";
        } else if (select.equalsIgnoreCase("sidesAndMisc")) {
            category += "lisukkeet";
        }
    }
}
