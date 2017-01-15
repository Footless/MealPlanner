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
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import kari.nutritionplanner.mealplanner.gui.controllers.SelectSideIngListener;
import kari.nutritionplanner.mealplanner.gui.controllers.SelectCaloriesListener;
import kari.nutritionplanner.mealplanner.gui.controllers.SelectCardListener;
import kari.nutritionplanner.mealplanner.gui.controllers.SelectFatListener;
import kari.nutritionplanner.mealplanner.gui.controllers.SelectMainIngListener;
import kari.nutritionplanner.mealplanner.gui.controllers.SelectProtListener;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.logic.CalculateMeal;
import kari.nutritionplanner.mealplanner.servicelayer.MealCalcHelper;

/**
 * Luokka aterian laskemiselle, sisältää kaikki osat varsinaista aterian
 * laskemista varten.
 *
 * @author kari
 */
public class CalcMealView {

    private final CardLayout cardL;
    private final CalculateMeal mealCalculator;
    private final MealCalcHelper helper;
    private final ComponentFactory compFactory;

    /**
     * Konstruktori. Saa parametreinä käyttöliittymän käyttämän
     * CardLayout-olion, CalculateMeal-olion, MealCalcHelper-olion sekä
     * ComponentFactory-olion.
     *
     * @param cardL CardLayout-olio jota koko käyttöliittymä käyttää näkymien
     * vaihtamiseen
     * @param mealCalculator MealCalculator-olio
     * @param helper MealCalcHelper-olio auttamaan ja säilömään
     * @param compFactory ComponentFactory tekemään komponentteja
     */
    public CalcMealView(CardLayout cardL, CalculateMeal mealCalculator, MealCalcHelper helper, ComponentFactory compFactory) {
        this.cardL = cardL;
        this.mealCalculator = mealCalculator;
        this.helper = helper;
        this.compFactory = compFactory;
    }

    /**
     * Luo näkymän, jossa valitaan pääraaka-aine.
     *
     * @param container Kaikki "kortit" sisältävä JPanel
     * @return palauttaa valmiin näkymän
     */
    public JPanel createMainIngredientCard(Container container) {
        JPanel card = new JPanel(new BorderLayout());
        JLabel instructions = compFactory.createLabel("Valitse listasta haluamasi raaka-aine");
        card.add(instructions, BorderLayout.NORTH);
        ButtonGroup mainsButtonGroup = compFactory.createIngButtons(card, "main");
        ActionListener sml = new SelectMainIngListener(this, cardL, mainsButtonGroup, container, "askSideIngredient");
        compFactory.addNextAndBackButtons(container, card, "start", sml);
        return card;
    }

    /**
     * Luo näkymän jossa valitaan lisäke.
     *
     * @param container Kaikki "kortit" sisältävä JPanel
     * @return palauttaa valmiin näkymän
     */
    public JPanel createSideIngredienCard(Container container) {
        JPanel card = new JPanel(new BorderLayout());
        JLabel instructions = compFactory.createLabel("Valitse listasta haluamasi raaka-aine");
        card.add(instructions, BorderLayout.NORTH);
        ButtonGroup sideButtonsGroup = compFactory.createIngButtons(card, "side");
        ActionListener al = new SelectSideIngListener(this, cardL, sideButtonsGroup, container, "askCalories");
        compFactory.addNextAndBackButtons(container, card, "askMainIngredient", al);
        return card;
    }

    /**
     * Luo näkymän jossa valitaan halutut kalorit.
     *
     * @param container Kaikki "kortit" sisältävä JPanel
     * @return palauttaa valmiin näkymän
     */
    public JPanel createCaloriesCard(Container container) {
        JPanel card = new JPanel(new BorderLayout());
        JLabel caloriesInstructions = compFactory.createLabel("Valitse haluamasi määrä kaloreita:");
        Ingredient ing = compFactory.getSearchHelper().getIngredientProcessor().getIngredients().get("mains").get(helper.getMainIngredientId());
        double mul = getMultiplier(ing);
        int min = (int) Math.ceil(ing.getCalories() * mul);
        JSlider calorieSlider = compFactory.createSlider(min, 800, 400);
        card.add(caloriesInstructions, BorderLayout.NORTH);
        card.add(calorieSlider, BorderLayout.CENTER);
        ActionListener scl = new SelectCaloriesListener(this, cardL, calorieSlider, container, "askProtein");
        compFactory.addNextAndBackButtons(container, card, "askMainIngredient", scl);
        return card;
    }

    /**
     * Luo näkymän jossa valitaan haluttu proteiinin määrä.
     *
     * @param container Kaikki "kortit" sisältävä JPanel
     * @return palauttaa valmiin näkymän
     */
    public JPanel createProteinsCard(Container container) {
        JPanel card = new JPanel(new BorderLayout());
        JLabel proteinInstructions = compFactory.createLabel("Valitse haluamasi proteiinin määrä:");
        Ingredient ing = compFactory.getSearchHelper().getIngredientProcessor().getIngredients().get("mains").get(helper.getMainIngredientId());
        double mul = getMultiplier(ing);
        int min = (int) Math.ceil(ing.getProtein() * mul);
        JSlider proteinSlider = compFactory.createSlider(min, min * 3, min * 2);
        card.add(proteinInstructions, BorderLayout.NORTH);
        card.add(proteinSlider, BorderLayout.CENTER);
        ActionListener spl = new SelectProtListener(this, cardL, proteinSlider, container, "askFat");
        compFactory.addNextAndBackButtons(container, card, "askCalories", spl);
        return card;
    }

    /**
     * Luo näkymän jossa valitaan haluttu rasvan määrä.
     *
     * @param container Kaikki "kortit" sisältävä JPanel
     * @return palauttaa valmiin näkymän
     */
    public JPanel createFatsCard(Container container) {
        JPanel card = new JPanel(new BorderLayout());
        JLabel fatInstructions = compFactory.createLabel("Valitse haluamasi rasvan määrä:");
        Ingredient ing = compFactory.getSearchHelper().getIngredientProcessor().getIngredients().get("mains").get(helper.getMainIngredientId());
        double mul = getMultiplier(ing);
        int min = (int) Math.ceil(ing.getFat() * mul) + 5;
        JSlider fatSlider = compFactory.createSlider(min, min * 4, min * 2);
        card.add(fatInstructions, BorderLayout.NORTH);
        card.add(fatSlider, BorderLayout.CENTER);
        ActionListener sfl = new SelectFatListener(this, cardL, mealCalculator, fatSlider, container, "readyMeal");
        compFactory.addNextAndBackButtons(container, card, "askProtein", sfl);
        return card;
    }

    /**
     * Luo näkymän jossa näytetään luotu ateria sekä sen sisältämät makrot.
     *
     * @param container Kaikki "kortit" sisältävä JPanel
     * @return palauttaa valmiin näkymän
     */
    public JPanel createReadyMealCard(Container container) {
        JPanel card = new JPanel(new BorderLayout());
        JLabel title = compFactory.createLabel("Tässä ateriasi:");
        compFactory.createMealText(card, mealCalculator, helper);
        JPanel buttons = new JPanel(new GridLayout(2, 1));
        JButton backToStart = compFactory.createButton("Valmis");
        backToStart.setActionCommand("clear");
        JButton back = compFactory.createButton("Takaisin");
        ActionListener start = new SelectCardListener(cardL, container, "start");
        ActionListener prevCardL = new SelectCardListener(cardL, container, "askFat");
        back.addActionListener(prevCardL);
        backToStart.addActionListener(start);
        buttons.add(backToStart);
        buttons.add(back);
        card.add(buttons, BorderLayout.SOUTH);
        card.add(title, BorderLayout.NORTH);
        return card;
    }

    private double getMultiplier(Ingredient ing) {
        if (ing.getProtein() > 23 | ing.getFat() > 10) {
            return 0.5;
        } else {
            return 1;
        }
    }

    /**
     * Helppo tapa tarjota helper ActionListenerien käyttöön, jokainen
     * GetMealListener-luokan toteuttava olio saa konstruktorissa tämän luokan
     * parametrinä.
     *
     * @return MealCalculateHelper-olio
     */
    public MealCalcHelper getHelper() {
        return this.helper;
    }
}
