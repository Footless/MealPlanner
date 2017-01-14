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

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import kari.nutritionplanner.mealplanner.gui.CalcMealView;
import kari.nutritionplanner.mealplanner.gui.UserInterface;

/**
 *
 * @author kari
 */
public class StartNewMealListener implements ActionListener {

    private final UserInterface ui;
    private final CardLayout cards;
    private final Container container;
    private final String card;
    private final CalcMealView cmv;

    public StartNewMealListener(UserInterface ui, CardLayout cards, Container container, String card, CalcMealView cmv) {
        this.ui = ui;
        this.cards = cards;
        this.container = container;
        this.card = card;
        this.cmv = cmv;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ui.createMealCards(container, cmv);
//        startCard.validate();
//        startCard.repaint();
        cards.show(container, card);
    }

}
