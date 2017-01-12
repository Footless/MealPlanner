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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import kari.nutritionplanner.mealplanner.gui.controllers.AddToIngsListener;
import kari.nutritionplanner.mealplanner.gui.controllers.SelectCardListener;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.domain.Meal;
import kari.nutritionplanner.mealplanner.servicelayer.CalculateMeal;
import kari.nutritionplanner.mealplanner.servicelayer.IngredientSearchHelper;
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
    private final IngredientSearchHelper searchHelper;
    private final Font f = new Font("Arial", 1, 18);

    /**
     * Konstuktori saa sekä CardLayoutin että MealCalcHelperin parametreinä,
     * jotta se pystyy paremmin toimimaan CalcMealView:n kanssa.
     *
     * @param cardL CardLayout, jonka päällä käyttöliittymä pyörii
     * @param searchHelper Raaka-aineiden hakemiseen tarkoitettu pikkuluokka
     * @param helper MealCalcHelper, auttaa käyttöliittymää aterian laskemisessa
     */
    public ComponentFactory(CardLayout cardL, MealCalcHelper helper, IngredientSearchHelper searchHelper) {
        this.cardL = cardL;
        this.helper = helper;
        this.searchHelper = searchHelper;
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
        JLabel subTitle = createLabel("Ravintosisältö (suluissa halutut makrot)");
        subTitle.setBackground(mainColor);
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
        JLabel title = createLabel("Komponentit ja määrät:");
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

    protected void createMealText(JPanel card, CalculateMeal cm, MealCalcHelper helper) {
        JPanel pane = new JPanel();
        BoxLayout boxL = new BoxLayout(pane, BoxLayout.PAGE_AXIS);
        pane.setLayout(boxL);
        pane.add(Box.createRigidArea(new Dimension(0, 10)));
        createAllIngs(cm, pane);

        pane.add(Box.createRigidArea(new Dimension(0, 5)));
        createAllMacros(cm, pane);
        card.add(pane, BorderLayout.CENTER);
    }

    protected JTextArea createTextArea(String text) {
        JTextArea textArea = new JTextArea(text);
        textArea.setFont(f);
        return textArea;
    }

    protected JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setFont(f);
        return label;
    }

    protected JButton createButton(String text) {
        JButton button = new JButton(text);
        return button;
    }

    protected JSlider createSlider(int min, int max, int set) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, set);
        slider.setMinorTickSpacing(min);
        slider.setMajorTickSpacing(max / 4);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setLabelTable(slider.createStandardLabels(max / 10));
        slider.setBackground(mainColor);
        return slider;
    }

    protected ButtonGroup createIngButtons(JPanel card, String select) {
        List<Ingredient> ings;
        if (select.contains("main")) {
            ings = helper.getMainIngredients();
        } else if (select.contains("side")) {
            ings = helper.getSideIngredients();
        } else {
            ings = new ArrayList<>();
        }
        JPanel ingButtons = new JPanel(new GridLayout(ings.size() + 1, 1));
        ingButtons.setAutoscrolls(true);
        ButtonGroup buttons = new ButtonGroup();
        if (select.contains("side")) {
            JRadioButton randomButton = new JRadioButton("Satunnainen lisäke");
            randomButton.setActionCommand("misc");
            randomButton.setSelected(true);
            buttons.add(randomButton);
            ingButtons.add(randomButton);
        }
        for (Ingredient ing : ings) {
            JRadioButton button = new JRadioButton(ing.getName());
            button.setBackground(mainColor);
            button.setName(ing.getName());
            button.setActionCommand(ing.getName());
            addToolTipForButton(button, select);
            if (select.contains("main")) {
                button.setSelected(true);
            }
            buttons.add(button);
            ingButtons.add(button);
        }
        ingButtons.setBackground(mainColor);
        card.add(ingButtons, BorderLayout.CENTER);
        return buttons;
    }

    protected void addNextAndBackButtons(Container container, JPanel card, String prevCard, ActionListener al) {
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

    protected JTextField createSearchField() {
        JTextField field = new JTextField("");
        field.setActionCommand("search");
        field.setRequestFocusEnabled(true);
        return field;
    }

    /**
     * Käytetään luomaan JList annetuista raaka-aineista. Sisältää myös
     * ListSelecionListenerin luonnin.
     *
     * @param searchFieldComp JPanel jonne valmis JList asetetaan
     * @param results
     * @see enableIngButtons
     */
    protected void createSearchIngList(JPanel searchFieldComp, JList results) {
        results.addListSelectionListener((ListSelectionEvent e) -> {
            if (e.getValueIsAdjusting() == false) {
                if (results.getSelectedIndex() == -1) {
                    enableIngButtons(searchFieldComp, false);
                } else {
                    enableIngButtons(searchFieldComp, true);
                }
            }
        });
    }

    /**
     * Tekee ActionListenerit raaka-ainehaun nappuloille.
     *
     * @param searchFieldComp JPanel jossa nappulat sijaitsevat.
     * @param panel Panel jossa nappulat sijaitsevat.
     */
    public void createActionListenersForButtons(JPanel searchFieldComp, JPanel panel) {
        Component[] buttons = panel.getComponents();
        ActionListener btnListener = new AddToIngsListener(searchFieldComp, searchHelper);
        for (Component button : buttons) {
            JButton jbutton = (JButton) button;
            jbutton.addActionListener(btnListener);
        }
    }

    protected void createAddIngButtons(JPanel searchFieldComp) {
        JPanel panel = new JPanel(new GridLayout(1, 3));
        JButton addToMains = createButton("Lisää pääraaka-aineisiin");
        addToMains.setActionCommand("mains");
        JButton addToSides = createButton("Lisää lisäkkeisiin");
        addToSides.setActionCommand("sides");
        JButton addToSauces = createButton("Lisää kastikkeisiin");
        addToSauces.setActionCommand("sauces");
//        JButton addToMiscs = createButton("Lisää lisukkeisiin");
//        addToMiscs.setActionCommand("miscs");
        addToMains.setEnabled(false);
        addToSides.setEnabled(false);
        addToSauces.setEnabled(false);
        panel.add(addToMains);
        panel.add(addToSides);
        panel.add(addToSauces);
        createActionListenersForButtons(searchFieldComp, panel);
        searchFieldComp.add(panel, BorderLayout.SOUTH);
        panel.validate();
        panel.repaint();
    }

    protected void enableIngButtons(JPanel searchFieldComp, boolean value) {
        JPanel panel = (JPanel) searchFieldComp.getComponent(2);
        Component[] buttons = panel.getComponents();
        for (Component button1 : buttons) {
            JButton button = (JButton) button1;
            button.setEnabled(value);
        }
    }

    private void addToolTipForButton(JComponent component, String select) {
        Ingredient ing;
        if (select.contains("main")) {
            ing = helper.getMainIngredientsAsMap().get(helper.getIdForMainIng(component.getName()));
        } else if (select.contains("side")) {
            ing = helper.getSideIngredientsAsMap().get(helper.getIdForSideIng(component.getName()));
        } else {
            ing = null;
        }

        component.setToolTipText("Ravintoarvot per 100g: \n"
                + "Kalorit: " + Math.ceil(ing.getCalories()) + "kcal \n"
                + "Proteiini: " + Math.ceil(ing.getProtein()) + "g \n"
                + "Rasva: " + Math.ceil(ing.getFat()) + "g \n"
                + "Hiilihydraatit: " + Math.ceil(ing.getCarb()) + "g \n"
                + "Kuidut: " + Math.ceil(ing.getFiber()) + "g");
    }

    public MealCalcHelper getHelper() {
        return this.helper;
    }

    public IngredientSearchHelper getSearchHelper() {
        return this.searchHelper;
    }
}
