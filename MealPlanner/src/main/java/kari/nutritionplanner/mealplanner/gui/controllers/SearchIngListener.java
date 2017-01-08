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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.gui.ComponentFactory;
import kari.nutritionplanner.mealplanner.servicelayer.IngredientSearchHelper;

/**
 * ActionListener joka suorittaa hakusanan perusteella haun kaikista Finelin
 * tietokannan raaka-aineista, ja tulostaa JListina hakusanan sisältävät raaka-aineet.
 *
 * @author kari
 */
public class SearchIngListener implements ActionListener {

    private final JTextField searchField;
    private JList searchResults;
    private final ComponentFactory compFactory;
    private final JPanel searchFieldComp;
    private IngredientSearchHelper helper;

    public SearchIngListener(JPanel searchFieldComp, JTextField searchField) throws IOException {
        this.searchFieldComp = searchFieldComp;
        this.searchField = searchField;
        this.compFactory = new ComponentFactory();
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        String searchTerm = searchField.getText();
        if (searchFieldComp.getComponentCount() > 2) {
            searchFieldComp.remove(2);
            searchFieldComp.validate();
            searchFieldComp.repaint();
        }
        searchField.setText("");
        try {
            this.helper = new IngredientSearchHelper();
            List<Ingredient> ings = helper.search(searchTerm);
            if (ings.isEmpty()) {
                JOptionPane.showMessageDialog(searchField, "Raaka-aineita ei löytynyt");
            } else {
                String[] ingNames = new String[ings.size()];
                for (int i = 0; i < ings.size(); i++) {
                    ingNames[i] = ings.get(i).getName();
                }
                searchResults = compFactory.createSearchIngList(ingNames, searchFieldComp);
                JScrollPane scrollResults = new JScrollPane(searchResults);
                scrollResults.getVerticalScrollBar().setUnitIncrement(10);
                scrollResults.getHorizontalScrollBar().setUnitIncrement(10);
                searchFieldComp.add(scrollResults, BorderLayout.CENTER);
                compFactory.createActionListenersForButtons(searchFieldComp);
                searchFieldComp.validate();
                searchFieldComp.repaint();
            }
        } catch (IOException ex) {
            Logger.getLogger(SearchIngListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
