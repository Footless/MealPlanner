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

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.JSlider;
import kari.nutritionplanner.mealplanner.gui.UserInterface;
import kari.nutritionplanner.mealplanner.servicelayer.MealCalcHelper;

/**
 * ActionListenerin toteuttava luokka. Ottaa proteiinin arvon talteen ja
 * vaihtaa näkyviin seuraavan kortin.
 *
 * @author kari
 */
public class SelectProtListener implements ActionListener {
    private final UserInterface ui;
    private final MealCalcHelper helper;
    private final JSlider slider;
    private final Container container;
    private final String nextCard;
    private final CardLayout cardL;

    public SelectProtListener(UserInterface ui, JSlider slider, Container container, String nextCard, CardLayout cardL) {
        this.ui  = ui;
        this.helper = ui.getHelper();
        this.slider = slider;
        this.container = container;
        this.nextCard = nextCard;
        this.cardL = cardL;
    }
    
    

    @Override
    public void actionPerformed(ActionEvent e) {
        JPanel card = ui.createFatsCard(container);
        container.add(card, nextCard);
        helper.setDesiredProtein(slider.getValue());
        cardL.show(container, nextCard);
    }
    
}
