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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import kari.nutritionplanner.mealplanner.gui.UserInterface;
import kari.nutritionplanner.mealplanner.servicelayer.CalculateMeal;
import kari.nutritionplanner.mealplanner.servicelayer.MealCalcHelper;

/**
 * ActionListeneri toteuttava luokka. Ottaa rasva-arvon talteen ja suorittaa
 * aterian laskemisen, jos mahdollista ja näyttää valmiin aterian.
 * 
 * @author kari
 */
public class SelectFatListener implements ActionListener {

    private final MealCalcHelper helper;
    private final JSlider slider;
    private final Container container;
    private final String nextCard;
    private final CardLayout cardL;
    private final UserInterface ui;
    private final CalculateMeal cm;

    public SelectFatListener(CalculateMeal cm, UserInterface ui, MealCalcHelper helper, JSlider slider, Container container, String nextCard, CardLayout cardL) {
        this.helper = helper;
        this.slider = slider;
        this.container = container;
        this.nextCard = nextCard;
        this.cardL = cardL;
        this.ui = ui;
        this.cm = cm;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        helper.setDesiredFat(slider.getValue());
        if (cm.calculateAllMeal(helper.getMainIngredientId(), helper.getDesiredCalories(), helper.getDesiredProtein(), helper.getDesiredFat())) {
            JPanel card = ui.createReadyMealCard(container);
            container.add(card, nextCard);
            cardL.show(container, nextCard);
        } else {
            JOptionPane.showMessageDialog(container, "Aterian luonti epäonnistui, yritä säätää proteiinin ja/tai rasvan määrää.");
        }
    }
}
