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
package kari.nutritionplanner.mealplanner.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import kari.nutritionplanner.mealplanner.controllers.SelectCardListener;
import kari.nutritionplanner.mealplanner.controllers.SearchIngListener;

/**
 * Uusien ruoka-aineiden lisäämiselle oma luokkansa, osa GUI:ta.
 *
 * @author kari
 */
public class AddIngredientsView {

    private final CardLayout cardL;
    private final ComponentFactory compFactory;

    public AddIngredientsView(CardLayout cardL, ComponentFactory compFactory) {
        this.cardL = cardL;
        this.compFactory = compFactory;
    }

    public JPanel createSearchIngCard(JPanel cards) {
        JPanel card = new JPanel(new BorderLayout());
        JLabel searchIngs = compFactory.createLabel("Hae raaka-aineita");
        card.add(searchIngs, BorderLayout.NORTH);
        
        JPanel searchFieldComp = new JPanel(new BorderLayout());

        createFieldAndButton(searchFieldComp);

        JButton addIng = compFactory.createButton("Lisää pääraaka-aineisiin");
        searchFieldComp.add(addIng, BorderLayout.SOUTH);

        card.add(searchFieldComp, BorderLayout.CENTER);

        ActionListener al = new SelectCardListener(cardL, card, "jotain");
        compFactory.addNextAndBackButtons(cards, card, "start", al);

        return card;
    }

    private void createFieldAndButton(JPanel searchFieldComp) {
        JPanel searchFieldAndButton = new JPanel();
        BoxLayout boxL = new BoxLayout(searchFieldAndButton, BoxLayout.LINE_AXIS);
        searchFieldAndButton.setLayout(boxL);
        JTextField searchField = compFactory.createSearchField();
        searchFieldAndButton.add(searchField);
        JButton searchButton = compFactory.createButton("Hae");
        ActionListener SearchIngListener = new SearchIngListener(searchFieldComp, searchField, searchButton);
        searchButton.addActionListener(SearchIngListener);
        searchFieldAndButton.add(searchButton);
        searchFieldComp.add(searchFieldAndButton, BorderLayout.NORTH);
    }
}