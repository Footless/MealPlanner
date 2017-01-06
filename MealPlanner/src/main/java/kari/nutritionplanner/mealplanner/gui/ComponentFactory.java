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
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import kari.nutritionplanner.mealplanner.controllers.AddToIngsListener;
import kari.nutritionplanner.mealplanner.controllers.SelectCardListener;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.domain.Meal;
import kari.nutritionplanner.mealplanner.servicelayer.CalculateMeal;
import kari.nutritionplanner.mealplanner.servicelayer.MealCalcHelper;

/**
 * Apuluokka GUI:lle, jossa metodit joilla luodaan erilaisia komponentteja,
 * kuten buttoneja, tekstikenttiä ja slidereita.
 *
 * @author kari
 */
public class ComponentFactory {

    private final Color mainColor = Color.white;
    private final CardLayout cardL;
    private final MealCalcHelper helper;
    private final Font f = new Font("Arial", 1, 18);

    public ComponentFactory(CardLayout cardL, MealCalcHelper helper) {
        this.cardL = cardL;
        this.helper = helper;
    }

    public ComponentFactory() {
        this.cardL = null;
        this.helper = null;
    }

    private JTextArea createIngText(Ingredient ing, double amount) {
        JTextArea jta = createTextArea(ing + ": " + Math.ceil(amount * 100) + "gr");
        return jta;
    }

    private JTextArea createMacroText(String macroName, double macros, int desiredMacros, String unit) {
        JTextArea jta = createTextArea(macroName + ": " + Math.ceil(macros) + unit + " (" + desiredMacros + unit + ")");
        return jta;
    }

    private JTextArea createMacroText(String macroName, double macros, String unit) {
        JTextArea jta = createTextArea(macroName + ": " + Math.ceil(macros) + unit);
        return jta;
    }

    private void createAllMacros(CalculateMeal cm, JPanel pane) {
        Meal meal = cm.getMeal();
        JTextArea subTitle = createTextArea("Ravintosisältö (suluissa halutut makrot)");
        pane.add(subTitle);
        JTextArea calories = createMacroText("Kalorit", meal.getCalories(), helper.getDesiredCalories(), "kcal");
        pane.add(calories);
        JTextArea protein = createMacroText("Proteiini", meal.getProtein(), helper.getDesiredProtein(), "gr");
        pane.add(protein);
        JTextArea fat = createMacroText("Rasva", meal.getFat(), helper.getDesiredFat(), "gr");
        pane.add(fat);
        JTextArea carb = createMacroText("Hiilihydraatit", meal.getCarbs(), "gr");
        pane.add(carb);
        JTextArea fiber = createMacroText("Kuidut", meal.getFiber(), "gr");
        pane.add(fiber);
    }

    private void createAllIngs(CalculateMeal cm, JPanel pane) {
        Meal meal = cm.getMeal();
        JTextArea title = createTextArea("Komponentit ja määrät:");
        pane.add(title);
        pane.add(Box.createRigidArea(new Dimension(0, 5)));
        JTextArea main = createIngText(meal.getMainIngredient(), meal.getMainIngredientAmount());
        main.setSize(400, 10);
        pane.add(main);
        JTextArea side = createIngText(meal.getSideIngredient(), meal.getSideIngredientAmount());
        side.setSize(400, 10);
        pane.add(side);
        JTextArea sauce = createIngText(meal.getSauce(), meal.getSauceAmount());
        pane.add(sauce);
        JTextArea misc = createIngText(meal.getMisc(), meal.getMiscAmount());
        pane.add(misc);
    }

    public void createMealText(JPanel card, CalculateMeal cm, MealCalcHelper helper) {
        JPanel pane = new JPanel();
        BoxLayout boxL = new BoxLayout(pane, BoxLayout.PAGE_AXIS);
        pane.setLayout(boxL);
        pane.add(Box.createRigidArea(new Dimension(0, 10)));
        createAllIngs(cm, pane);

        pane.add(Box.createRigidArea(new Dimension(0, 5)));
        createAllMacros(cm, pane);
        card.add(pane, BorderLayout.CENTER);
    }

    public JTextArea createTextArea(String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(f);
        return textArea;
    }

    public JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(f);
        return label;
    }

    public JButton createButton(String text) {
        JButton button = new JButton(text);
//        button.setBackground(Color.cyan);
        return button;
    }

    public JSlider createSlider(int min, int max, int set) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, set);
        slider.setMinorTickSpacing(min);
        slider.setMajorTickSpacing(max / 4);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setLabelTable(slider.createStandardLabels(max / 10));
        slider.setBackground(mainColor);
        return slider;
    }

    public ButtonGroup createMainsButtons(JPanel card, CalculateMeal mealCalculator) throws IOException {
        List<Ingredient> mains = helper.getMainIngredients();
        JPanel mainButtons = new JPanel(new GridLayout(mains.size(), 1));
        mainButtons.setAutoscrolls(true);
        ButtonGroup buttons = new ButtonGroup();
        for (Ingredient main : mains) {
            JRadioButton button = new JRadioButton(main.getName());
            button.setBackground(mainColor);
            button.setActionCommand(main.getName());
            button.setSelected(true);
            buttons.add(button);
            mainButtons.add(button);

        }
        mainButtons.setBackground(mainColor);
        card.add(mainButtons, BorderLayout.CENTER);
        return buttons;
    }

    public void addNextAndBackButtons(Container container, JPanel card, String prevCard, ActionListener al) {
        JPanel buttons = new JPanel(new GridLayout(2, 1));
        JButton next = createButton("Seuraava");
        JButton back = createButton("Takaisin");
        SelectCardListener prevCardL = new SelectCardListener(cardL, container, prevCard);
        back.addActionListener(prevCardL);
        ActionListener nextCardL = al;
        next.addActionListener(nextCardL);
        buttons.add(next);
        buttons.add(back);
        card.add(buttons, BorderLayout.SOUTH);
    }

    public JTextField createSearchField() {
        JTextField field = new JTextField("");
        field.setActionCommand("search");
        field.setRequestFocusEnabled(true);
        return field;
    }

    public JList createSearchIngList(String[] ings) {
        JList list = new JList(ings);
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        Font f2 = new Font("Arial", 1, 15);
        list.setFont(f2);
        list.setCursor(new Cursor(0));
        list.setBackground(mainColor);
        return list;
    }

    void createAddIngButtons(JPanel searchFieldComp) throws IOException {
        JList list = null;
        JPanel panel = new JPanel(new GridLayout(1, 3));
        JButton addToMains = createButton("Lisää pääraaka-aineisiin");
        addToMains.setActionCommand("main");
        JButton addToSides = createButton("Lisää lisäkkeisiin");
        addToSides.setActionCommand("side");
        JButton addToSauces = createButton("Lisää kastikkeisiin");
        addToSauces.setActionCommand("sauce");
        panel.add(addToMains);
        panel.add(addToSides);
        panel.add(addToSauces);
        searchFieldComp.add(panel, BorderLayout.SOUTH);
        enableIngButtons(searchFieldComp);
        panel.validate();
        panel.repaint();
    }

    public void enableIngButtons(JPanel searchFieldComp) throws IOException {
        JPanel panel = (JPanel) searchFieldComp.getComponent(1);
        Component[] buttons = panel.getComponents();
        ActionListener btnListener = new AddToIngsListener(searchFieldComp);
        for (Component button1 : buttons) {
            JButton button = (JButton) button1;
            if (searchFieldComp.getComponentCount() > 2) {
                button.setEnabled(true);
                button.addActionListener(btnListener);
            } else {
                button.setEnabled(false);
            }
        }
    }
}
