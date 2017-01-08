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
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JPanel;
import kari.nutritionplanner.mealplanner.gui.CalcMealView;
import kari.nutritionplanner.mealplanner.servicelayer.MealCalcHelper;

/**
 * ActionListenerin toteuttava luokka. Ottaa pääraaka-aineen talteen ja 
 * vaihtaa seuraavan kortin, joka on proteiini.
 * 
 * @author kari
 */
public class SelectMainIngListener extends GetMealListener {
    private final MealCalcHelper helper;
    private final ButtonGroup bg;
    private final Container container;
    private final String nextCard;
    
    public SelectMainIngListener(CalcMealView view, CardLayout cardL, ButtonGroup bg, Container container, String nextCard) {
        super(view, cardL);
        this.helper = view.getHelper();
        this.bg = bg;
        this.container = container;
        this.nextCard = nextCard;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonModel b = bg.getSelection();
        String name = b.getActionCommand();
        helper.setMainIngredient(name);
        try {
            JPanel card = view.createSideIngredienCard(container);
            container.add(card, nextCard);
            cardL.show(container, nextCard);
        } catch (IOException ex) {
            Logger.getLogger(SelectMainIngListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}