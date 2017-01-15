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
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.gui.controllers.RemoveIngListener;
import kari.nutritionplanner.mealplanner.gui.controllers.SelectCardListener;
import kari.nutritionplanner.mealplanner.gui.controllers.ShowIngMacrosListener;

/**
 * Luokka käyttäjän määrittelemien raaka-aineiden hallinnalle.
 *
 * @author kari
 */
public class BrowseIngredientsView {

    private final ComponentFactory compFactory;
    private final static Color MAINCOLOR = Color.white;

    /**
     * Konstruktori saa paremetrina ComponentFactoryn komponenttien lisäämistä
     * auttamaan.
     *
     * @param compFactory ComponentFactory-olio
     */
    public BrowseIngredientsView(ComponentFactory compFactory) {
        this.compFactory = compFactory;
    }

    /**
     * Metodi luo kortin, jossa kaikki raaka-aineryhmät omana listanaan sekä
     * painikkeet joilla raaka-aineita voi poistaa sekä tarkastella
     * raaka-aineiden ravintotietoja.
     *
     * @param cards CardLayout, jota käyttöliittymä käyttää
     * @param container Kaikki "kortit" sisältävä JPanel
     */
    public void createBrowseIngredientsCard(CardLayout cards, Container container) {
        JPanel panel = new JPanel(new BorderLayout());
        //lista raaka-aineista
        createListsForUserIngredients(panel);
        // takaisin painike, ainakin
        JButton readyButton = compFactory.createButton("Valmis");
        ActionListener readyBListener = new SelectCardListener(cards, container, "start");
        readyButton.addActionListener(readyBListener);
        panel.add(readyButton, BorderLayout.SOUTH);

        container.add(panel, "browseIngredients");
    }

    private void createListsForUserIngredients(JPanel mainPanel) {
        JPanel panel = new JPanel();
        BoxLayout boxL = new BoxLayout(panel, BoxLayout.Y_AXIS);

        panel.setLayout(boxL);

        JLabel mains = compFactory.createLabel("Pääraaka-aineet");
        mains.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(mains);
        createListForIngs(panel, "mains");

        JLabel sides = compFactory.createLabel("Lisäkkeet");
        panel.add(sides);
        createListForIngs(panel, "sides");

        JLabel sauces = compFactory.createLabel("Kastikkeet");
        panel.add(sauces);
        createListForIngs(panel, "sauces");

        JLabel misc = compFactory.createLabel("Lisukkeet");
        panel.add(misc);
        createListForIngs(panel, "misc");

        mainPanel.add(panel, BorderLayout.CENTER);
    }

    private void createListForIngs(JPanel panel, String select) {
        DefaultListModel listModel = new DefaultListModel();
        JList ingredients = new JList(listModel);
        ingredients.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ingredients.setLayoutOrientation(JList.VERTICAL);
        ingredients.setVisibleRowCount(-1);
        JScrollPane scrollableIngs = new JScrollPane(ingredients);
        scrollableIngs.getVerticalScrollBar().setUnitIncrement(10);
        scrollableIngs.getHorizontalScrollBar().setUnitIncrement(10);
        Font f2 = new Font("Arial", 1, 15);
        ingredients.setFont(f2);
        ingredients.setCursor(new Cursor(0));
        ingredients.setBackground(MAINCOLOR);
        upgradeIngList(listModel, select);
        JPanel buttons = new JPanel();
        BoxLayout buttonLayout = new BoxLayout(buttons, BoxLayout.X_AXIS);
        buttons.setLayout(buttonLayout);

        JButton removeButton = compFactory.createButton("Poista raaka-aineista");
        ActionListener removeBListener = new RemoveIngListener(compFactory, ingredients, select, listModel, this);
        removeButton.addActionListener(removeBListener);
        removeButton.setEnabled(false);
        buttons.add(removeButton);

        JButton viewMacrosButton = new JButton("Katso ravintotiedot");
        viewMacrosButton.setEnabled(false);
        ActionListener viewMacrosListener = new ShowIngMacrosListener(compFactory, ingredients);
        viewMacrosButton.addActionListener(viewMacrosListener);
        buttons.add(viewMacrosButton);
        ingredients.addListSelectionListener((ListSelectionEvent e) -> {
            if (e.getValueIsAdjusting() == false) {
                if (ingredients.getSelectedIndex() == -1) {
                    enableButton(removeButton, false);
                    enableButton(viewMacrosButton, false);
                } else if (listModel.getSize() <= 1) {
                    enableButton(removeButton, false);
                    enableButton(viewMacrosButton, true);
                } else {
                    enableButton(removeButton, true);
                    enableButton(viewMacrosButton, true);
                }
            }
        });
        panel.add(scrollableIngs);
        panel.add(buttons);
    }

    private void enableButton(JButton removeButton, boolean b) {
        removeButton.setEnabled(b);
    }

    /**
     * Metodi joka päivittää listan näyttämään oikeita raaka-aineita
     * raaka-aineiden poiston jälkeen.
     *
     * @param listModel DefaultListModel, jossa raaka-aineet sijaitsevat
     * @param select String, jolla kuvataan mitä listoista päivitetään, "mains",
     * "sides", "sauces" ja "misc".
     */
    public void upgradeIngList(DefaultListModel listModel, String select) {
        listModel.clear();
        if (select.contains("misc")) {
            select = "sidesAndMisc";
        }
        Map<Integer, Ingredient> ings = compFactory.getSearchHelper().getIngredientProcessor().getIngredients().get(select);
        for (Ingredient ing : ings.values()) {
            listModel.addElement(ing.getName());
        }
    }

}
