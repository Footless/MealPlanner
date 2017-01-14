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
import kari.nutritionplanner.mealplanner.gui.BrowseIngredientsView;
import kari.nutritionplanner.mealplanner.gui.ComponentFactory;

/**
 *
 * @author kari
 */
public class BrowseIngsListener implements ActionListener {
    private final CardLayout cards;
    private final Container container;
    private final ComponentFactory compFactory;
    private final String nextCard;

    public BrowseIngsListener(CardLayout cards, Container container, ComponentFactory compFactory, String nextCard) {
        this.cards = cards;
        this.container = container;
        this.compFactory = compFactory;
        this.nextCard = nextCard;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        BrowseIngredientsView biView = new BrowseIngredientsView(cards, container, compFactory);
        biView.createBrowseIngredientsCard(cards, container);
        cards.show(container, nextCard);
    }
    
}
