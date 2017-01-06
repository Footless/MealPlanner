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
package kari.nutritionplanner.mealplanner.controllers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.gui.ComponentFactory;
import kari.nutritionplanner.mealplanner.util.SCVReader;

/**
 *
 * @author kari
 */
public class SearchIngListener implements ActionListener {

    private JTextField searchField;
    private JButton searchButton;
    private JList searchResults;
    private SCVReader reader;
    private final ComponentFactory compFactory;
    private JPanel searchFieldComp;

    public SearchIngListener(JPanel searchFieldComp, JTextField searchField, JButton searchButton) {
        this.searchFieldComp = searchFieldComp;
        this.searchField = searchField;
        this.searchButton = searchButton;
        this.compFactory = new ComponentFactory();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.reader = new SCVReader("food_utf.csv");
        String searchTerm = searchField.getText();
        if (searchFieldComp.getComponentCount() > 2) {
            searchFieldComp.remove(2);
        }
        searchField.setText("");
        try {
            List<Ingredient> ings = reader.search(searchTerm);
            if (ings.isEmpty()) {
                JOptionPane.showMessageDialog(searchField, "Raaka-aineita ei löytynyt");
            } else {
                String[] ingNames = new String[ings.size()];
                for (int i = 0; i < ings.size(); i++) {
                    ingNames[i] = ings.get(i).getName();
                }
                searchResults = compFactory.createSearchIngList(ingNames);
                JScrollPane scrollResults = new JScrollPane(searchResults);
                scrollResults.getVerticalScrollBar().setUnitIncrement(10);
                scrollResults.getHorizontalScrollBar().setUnitIncrement(10);
//                searchResults.setAutoscrolls(true);
                searchFieldComp.add(scrollResults, BorderLayout.CENTER);
                searchFieldComp.validate();
                searchFieldComp.repaint();
            }
        } catch (IOException ex) {
            Logger.getLogger(SearchIngListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
