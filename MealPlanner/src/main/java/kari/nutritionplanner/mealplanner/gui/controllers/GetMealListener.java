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
import java.awt.event.ActionListener;
import kari.nutritionplanner.mealplanner.gui.CalcMealView;

/**
 * Abstrakti luokka kaikille aterian lisäämiseen liittyville
 * ActionListenereille.
 *
 * @author kari
 */
public abstract class GetMealListener implements ActionListener {

    protected CalcMealView view;
    protected CardLayout cardL;

    /**
     * Konstruktori saa parametrina CalcMealView:n sekä koko käyttöliittymän
     * alla pyörivän CardLayoutin.
     *
     * @param view CalcMealView, emoluokka kaikille aterian laskemiseen liittyville "korteille"
     * @param cardL CardLayout, koko käyttöliittymää pyörittävä layout
     */
    public GetMealListener(CalcMealView view, CardLayout cardL) {
        this.view = view;
        this.cardL = cardL;
    }

}
