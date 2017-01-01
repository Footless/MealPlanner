/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.servicelayer.CalculateMeal;

/**
 *
 * @author kari
 */
public class UserInterface implements Runnable {

    private JFrame frame;
    private CardLayout cardL;
    private CalculateMeal mealCalculator;
    private Color mainColor;

    @Override
    public void run() {
        frame = new JFrame("Meal Planner");
        frame.setPreferredSize(new Dimension(650, 500));
        cardL = new CardLayout();
        mealCalculator = new CalculateMeal();
        this.mainColor = Color.white;

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
    }

    private void createComponents(Container contentPane) {
        JPanel cards = new JPanel(cardL);
        JPanel startCard = createStartCard(cards);
        JPanel mainIngredientCard = createMainIngredientCard(cards);
//        JPanel mealCard = createMealCard(cards);
        cards.add(startCard, "start");
        cards.add(mainIngredientCard, "askMainIngredient");
//        cards.add(CealCard, "readyMeal");
        contentPane.add(cards);
    }

    public JFrame getFrame() {
        return frame;
    }

    private JPanel createStartCard(Container container) {
        JPanel startCard = new JPanel();
        startCard.setBackground(Color.black);
        startCard.setLayout(new GridLayout(3, 1, 2, 2));
        
        JButton getMeal = createButton("Laske ateria");
        StartNewMealListener snml = new StartNewMealListener(cardL, container);
        getMeal.addActionListener(snml);
        
        JButton addIngredients = createButton("Lisää uusia raaka-aineita");
        
        JButton browseIngredients = createButton("Selaa raaka-aineita");
        
        startCard.add(getMeal);
        startCard.add(addIngredients);
        startCard.add(browseIngredients);
        return startCard;
    }

    private JPanel createMainIngredientCard(JPanel cards) {
        JPanel card = new JPanel();
        card.setBackground(Color.black);
        GridLayout layout = new GridLayout(4, 1);  // perusjako kortille
        card.setLayout(layout);
        JPanel selection = new JPanel(new GridLayout(3,1)); // ensimmäinen kolmasosa kolmeen osaan
        
        JTextArea instructions = createTextArea("Valitse listasta haluamasi raaka-aine");
        JComboBox box = createProteinComboBox();                // lisätään ensimmäisen osan komponentit
        JTextArea proteinInstructions = createTextArea("Valitse halaumasi määrä proteiinia:");
        selection.add(instructions);
        selection.add(box);
        selection.add(proteinInstructions);
        
        JPanel restMacros = new JPanel(new GridLayout(3,1));
        JSlider proteinSlider = createSlider(10, 60, 20);
        JTextArea fatInstructions = createTextArea("Valitse haluamasi rasvan määrä");
        JSlider fatSlider = createSlider(10, 50, 20);
        restMacros.add(proteinSlider);
        restMacros.add(fatInstructions);
        restMacros.add(fatSlider);
        
        JPanel calories = new JPanel(new GridLayout(2, 1));
        JTextArea caloriesInstructions = createTextArea("Valitse haluamasi määrä proteiinia:");
        JSlider calorieSlider = createSlider(200, 2000, 400);
        calories.add(caloriesInstructions);
        calories.add(calorieSlider);
        
        JPanel buttons = new JPanel(new GridLayout(2, 1));
        JButton ok = new JButton("Laske ateria");
        SelectMainIngListener sml = new SelectMainIngListener(this, cardL, cards, box, proteinSlider, fatSlider, calorieSlider, mealCalculator);
        ok.addActionListener(sml);
        JButton back = new JButton("Takaisin alkuun");
        BackButtonListener bbl = new BackButtonListener(cardL, cards);
        back.addActionListener(bbl);
        buttons.add(ok);
        buttons.add(back);
        card.add(selection);
        card.add(restMacros);
        card.add(calories);
        card.add(buttons);
        return card;
    }
    
    protected JPanel createMealCard(Container container) {
        JPanel card = new JPanel(new GridLayout(1, 1));
        JTextArea meal = createTextArea(mealCalculator.getMeal().toString());
        card.add(meal);
        container.add(card);
        container.add(card, "readyMeal");
        return card;
    }

    private JComboBox createProteinComboBox() {
        List<String> mainsAsList = new ArrayList<>();
        for (Ingredient ing : mealCalculator.getMainIngredients()) {
            mainsAsList.add(ing.getName());
        }
        String[] mains = new String[mainsAsList.size()];
        mains = mainsAsList.toArray(mains);
        JComboBox box = new JComboBox(mains);
        box.setFont(new Font("Arial", 1, 17));
        box.setBackground(mainColor);
        return box;
    }

    private JTextArea createTextArea(String text) {
        JTextArea instructions = new JTextArea(text);
        Font f = new Font("Arial", 1, 18);
        instructions.setFont(f);
        return instructions;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.cyan);
        return button;
    }

    private JSlider createSlider(int min, int max, int set) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, set);
        slider.setMinorTickSpacing(min / 5);
        slider.setMajorTickSpacing(min);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setLabelTable(slider.createStandardLabels(min));
        slider.setBackground(mainColor);
        return slider;
    }

}