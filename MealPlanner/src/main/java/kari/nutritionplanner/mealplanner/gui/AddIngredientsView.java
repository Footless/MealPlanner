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
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import kari.nutritionplanner.mealplanner.gui.controllers.SelectCardListener;
import kari.nutritionplanner.mealplanner.gui.controllers.SearchIngListener;

/**
 * Uusien ruoka-aineiden lisäämiselle oma luokkansa, osa graafista
 * käyttöliittymää.
 *
 * @author kari
 */
public class AddIngredientsView {

    private final CardLayout cardL;
    private final ComponentFactory compFactory;
    private final static Color MAINCOLOR = Color.white;

    /**
     * Konstuktori. Saa parametrinä käyttöliittymän käyttämän CardLayout-olion.
     *
     * @param cardL CardLayout-olio, jota käyttöliittymä käyttää näkymien
     * vaihtamiseen
     * @param compFactory Komponenttien tekoa varten ComponentFactory
     */
    public AddIngredientsView(CardLayout cardL, ComponentFactory compFactory) {
        this.cardL = cardL;
        this.compFactory = compFactory;
    }

    protected JPanel createSearchIngCard(JPanel cards) {
        JPanel card = new JPanel(new BorderLayout());
        JLabel searchIngs = compFactory.createLabel("Hae raaka-aineita");
        card.add(searchIngs, BorderLayout.NORTH);

        JPanel searchFieldComp = new JPanel(new BorderLayout());

        // hakutulokset esittävä kenttä. Samassa metodissa tehdään myös yläreunan hakukenttä ja nappula
        createSearchResultsScrollPane(searchFieldComp);

        // alareunan napit, joilla raaka-aineita voi lisäillä tietokantaan
        compFactory.createAddIngButtons(searchFieldComp);

        card.add(searchFieldComp, BorderLayout.CENTER);

        JButton readyButton = new JButton("Valmis");
        ActionListener ready = new SelectCardListener(cardL, cards, "start");
        readyButton.addActionListener(ready);
        card.add(readyButton, BorderLayout.SOUTH);

        return card;
    }

    private void createFieldAndButton(JPanel searchFieldComp, DefaultListModel listModel) {
        JPanel searchFieldAndButton = new JPanel();
        BoxLayout boxL = new BoxLayout(searchFieldAndButton, BoxLayout.LINE_AXIS);
        searchFieldAndButton.setLayout(boxL);
        JTextField searchField = compFactory.createSearchField();

        JButton searchButton = compFactory.createButton("Hae");
        ActionListener searchIngListener = new SearchIngListener(searchField, listModel, compFactory.getSearchHelper());
        searchButton.addActionListener(searchIngListener);
        searchField.addActionListener(searchIngListener);
        searchFieldAndButton.add(searchField);
        searchFieldAndButton.add(searchButton);
        searchFieldComp.add(searchFieldAndButton, BorderLayout.NORTH);
    }

    private void createSearchResultsScrollPane(JPanel searchFieldComp) {
        DefaultListModel listModel = new DefaultListModel();
        JList searchResults = new JList(listModel);
        searchResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        searchResults.setLayoutOrientation(JList.VERTICAL);
        searchResults.setVisibleRowCount(-1);
        JScrollPane scrollResults = new JScrollPane(searchResults);
        scrollResults.getVerticalScrollBar().setUnitIncrement(10);
        scrollResults.getHorizontalScrollBar().setUnitIncrement(10);
        Font f2 = new Font("Arial", 1, 15);
        searchResults.setFont(f2);
        searchResults.setCursor(new Cursor(0));
        searchResults.setBackground(MAINCOLOR);
        searchFieldComp.add(scrollResults, BorderLayout.CENTER);
        searchResults.addListSelectionListener((ListSelectionEvent e) -> {
            if (e.getValueIsAdjusting() == false) {
                if (searchResults.getSelectedIndex() == -1) {
                    compFactory.enableIngButtons(searchFieldComp, false);
                } else {
                    compFactory.enableIngButtons(searchFieldComp, true);
                }
            }
        });
        createFieldAndButton(searchFieldComp, listModel);
    }
}
