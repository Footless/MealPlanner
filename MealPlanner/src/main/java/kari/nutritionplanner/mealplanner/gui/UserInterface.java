/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.gui;

import kari.nutritionplanner.mealplanner.controllers.SelectCardListener;
import kari.nutritionplanner.mealplanner.controllers.SelectCaloriesListener;
import kari.nutritionplanner.mealplanner.controllers.SelectProtListener;
import kari.nutritionplanner.mealplanner.controllers.SelectMainIngListener;
import kari.nutritionplanner.mealplanner.controllers.SelectFatListener;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.domain.Meal;
import kari.nutritionplanner.mealplanner.servicelayer.CalculateMeal;
import kari.nutritionplanner.mealplanner.servicelayer.MealCalcHelper;

/**
 * Graafinen käyttöliittymä Meal Plannerille.
 * 
 * @author kari
 */
public class UserInterface implements Runnable {

    private JFrame frame;
    private CardLayout cardL;
    private CalculateMeal mealCalculator;
    private final ComponentFactory compFactory = new ComponentFactory();
    private MealCalcHelper helper;

    @Override
    public void run() {
        frame = new JFrame("Meal Planner");
        frame.setPreferredSize(new Dimension(650, 500));
        cardL = new CardLayout();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        try {
            createComponents(frame.getContentPane());
        } catch (IOException ex) {
            Logger.getLogger(UserInterface.class.getName()).log(Level.SEVERE, null, ex);
        }

        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
    }

    private void createComponents(Container contentPane) throws IOException {
        JPanel cards = new JPanel(cardL);
        JPanel startCard = createStartCard(cards);
        cards.add(startCard, "start");
        createMealCards(cards);

        contentPane.add(cards);
    }

    private JPanel createStartCard(Container container) {
        JPanel startCard = new JPanel();
        startCard.setBackground(Color.black);
        startCard.setLayout(new GridLayout(3, 1, 2, 2));

        JButton getMeal = compFactory.createButton("Laske ateria");
        SelectCardListener snml = new SelectCardListener(this, cardL, container, "askMainIngredient");
        getMeal.addActionListener(snml);

        JButton addIngredients = compFactory.createButton("Lisää uusia raaka-aineita");

        JButton browseIngredients = compFactory.createButton("Selaa raaka-aineita");

        startCard.add(getMeal);
        startCard.add(addIngredients);
        startCard.add(browseIngredients);
        return startCard;
    }

    private void createMealCards(JPanel cards) throws IOException {
        JPanel mainIngredientCard = createMainIngredientCard(cards);
        cards.add(mainIngredientCard, "askMainIngredient");
    }

    public JFrame getFrame() {
        return frame;
    }

    private JPanel createMainIngredientCard(JPanel cards) throws IOException {
        mealCalculator = new CalculateMeal();
        helper = new MealCalcHelper(mealCalculator);
        
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea instructions = compFactory.createTextArea("Valitse listasta haluamasi raaka-aine");
        card.add(instructions, BorderLayout.NORTH);
        ButtonGroup mainsButtonGroup = compFactory.createMainsButtons(card, mealCalculator);
        ActionListener sml = new SelectMainIngListener(this, cardL, mainsButtonGroup, cards, "askCalories");
        addNextAndBackButtons(cards, card, "start", sml);
        return card;
    }

    public JPanel createCaloriesCard(Container container) throws IOException {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea caloriesInstructions = compFactory.createTextArea("Valitse haluamasi määrä kaloreita:");
        Ingredient ing = mealCalculator.getIngredients().get("mains").get(helper.getMainIngredientId());
        double mul = getMultiplier(ing);
        int min = (int) Math.ceil(ing.getCalories() * mul);
        JSlider calorieSlider = compFactory.createSlider(min, 800, 800 / 2);
        card.add(caloriesInstructions, BorderLayout.NORTH);
        card.add(calorieSlider, BorderLayout.CENTER);
        ActionListener scl = new SelectCaloriesListener(this, calorieSlider, container, "askProtein", cardL);
        addNextAndBackButtons(container, card, "askMainIngredient", scl);
        return card;
    }

    public JPanel createProteinsCard(Container container) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea proteinInstructions = compFactory.createTextArea("Valitse haluamasi proteiinin määrä:");
        Ingredient ing = mealCalculator.getIngredients().get("mains").get(helper.getMainIngredientId());
        double mul = getMultiplier(ing);
        int min = (int) Math.ceil(ing.getProtein() * mul);
        JSlider proteinSlider = compFactory.createSlider(min, min * 5, min * 5 / 2);
        card.add(proteinInstructions, BorderLayout.NORTH);
        card.add(proteinSlider, BorderLayout.CENTER);
        ActionListener spl = new SelectProtListener(this, proteinSlider, container, "askFat", cardL);
        addNextAndBackButtons(container, card, "askCalories", spl);
        return card;
    }

    public JPanel createFatsCard(Container container) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea fatInstructions = compFactory.createTextArea("Valitse haluamasi rasvan määrä:");
        Ingredient ing = mealCalculator.getIngredients().get("mains").get(helper.getMainIngredientId());
        double mul = getMultiplier(ing);
        int min = (int) Math.ceil(ing.getFat() * mul);
        JSlider fatSlider = compFactory.createSlider(min, 40, 40 / 2);
        card.add(fatInstructions, BorderLayout.NORTH);
        card.add(fatSlider, BorderLayout.CENTER);
        ActionListener sfl = new SelectFatListener(mealCalculator, this, fatSlider, container, "readyMeal", cardL);
        addNextAndBackButtons(container, card, "askProtein", sfl);
        return card;
    }
    
    public JPanel createReadyMealCard(Container container) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea title = compFactory.createTextArea("Tässä ateriasi:");
        compFactory.createMealText(card, mealCalculator, helper);
        JPanel buttons = new JPanel(new GridLayout(2, 1));
        JButton backToStart = compFactory.createButton("Valmis");
        backToStart.setActionCommand("clear");
        JButton back = compFactory.createButton("Takaisin");
        ActionListener start = new SelectCardListener(this, cardL, container, "start");
        ActionListener prevCardL = new SelectCardListener(this, cardL, container, "askFat");
        back.addActionListener(prevCardL);
        backToStart.addActionListener(start);
        buttons.add(backToStart);
        buttons.add(back);
        card.add(buttons, BorderLayout.SOUTH);
        card.add(title, BorderLayout.NORTH);
        return card;
    }

    private void addNextAndBackButtons(Container container, JPanel card, String prevCard, ActionListener al) {
        JPanel buttons = new JPanel(new GridLayout(2, 1));
        JButton next = compFactory.createButton("Seuraava");
        JButton back = compFactory.createButton("Takaisin");
        SelectCardListener prevCardL = new SelectCardListener(this, cardL, container, prevCard);
        back.addActionListener(prevCardL);
        ActionListener nextCardL = al;
        next.addActionListener(nextCardL);
        buttons.add(next);
        buttons.add(back);
        card.add(buttons, BorderLayout.SOUTH);
    }

    private double getMultiplier(Ingredient ing) {
        if (ing.getProtein() > 23) {
            return 0.5;
        } else {
            return 1;
        }
    }

    public MealCalcHelper getHelper() {
        return this.helper;
    }
}
