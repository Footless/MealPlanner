/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.gui;

import kari.nutritionplanner.mealplanner.controllers.SelectCardListener;
import kari.nutritionplanner.mealplanner.controllers.SelectCalListener;
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
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
    private final Color mainColor = Color.white;
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

        JButton getMeal = createButton("Laske ateria");
        SelectCardListener snml = new SelectCardListener(cardL, container, "askMainIngredient");
        getMeal.addActionListener(snml);

        JButton addIngredients = createButton("Lisää uusia raaka-aineita");

        JButton browseIngredients = createButton("Selaa raaka-aineita");

        startCard.add(getMeal);
        startCard.add(addIngredients);
        startCard.add(browseIngredients);
        return startCard;
    }

    private void createMealCards(JPanel cards) throws IOException {
        JPanel mainIngredientCard = createMainIngredientCard(cards);
        JPanel caloriesCard = createCaloriesCard(cards);
        JPanel proteinsCard = createProteinsCard(cards);
        JPanel fatsCard = createFatsCard(cards);
//        JPanel readyMealCard = createReadyMealCard(cards);

        cards.add(mainIngredientCard, "askMainIngredient");
        cards.add(caloriesCard, "askCalories");
        cards.add(proteinsCard, "askProtein");
        cards.add(fatsCard, "askFat");
//        cards.add(readyMealCard, "readyMeal");
    }

    public JFrame getFrame() {
        return frame;
    }

    private JPanel createMainIngredientCard(JPanel cards) throws IOException {
        mealCalculator = new CalculateMeal();
        helper = new MealCalcHelper();
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea instructions = createTextArea("Valitse listasta haluamasi raaka-aine");
        card.add(instructions, BorderLayout.NORTH);
        ButtonGroup bg = createMainsButtons(card);
        SelectMainIngListener sml = new SelectMainIngListener(cardL, helper, bg, cards, "askCalories");
        addNextAndBackButtons(cards, card, "start", sml);
        return card;
    }

    private JPanel createCaloriesCard(JPanel cards) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea caloriesInstructions = createTextArea("Valitse haluamasi määrä kaloreita:");
        JSlider calorieSlider = createSlider(300, 800, 400);
        card.add(caloriesInstructions, BorderLayout.NORTH);
        card.add(calorieSlider, BorderLayout.CENTER);
        SelectCalListener scl = new SelectCalListener(helper, calorieSlider, cards, "askProtein", cardL);
        addNextAndBackButtons(cards, card, "askMainIngredient", scl);
        return card;
    }

    private JPanel createProteinsCard(JPanel cards) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea proteinInstructions = createTextArea("Valitse haluamasi proteiinin määrä:");
        JSlider proteinSlider = createSlider(10, 60, 30);
        card.add(proteinInstructions, BorderLayout.NORTH);
        card.add(proteinSlider, BorderLayout.CENTER);
        SelectProtListener spl = new SelectProtListener(helper, proteinSlider, cards, "askFat", cardL);
        addNextAndBackButtons(cards, card, "askCalories", spl);
        return card;
    }

    private JPanel createFatsCard(JPanel cards) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea fatInstructions = createTextArea("Valitse haluamasi rasvan määrä:");
        JSlider fatSlider = createSlider(5, 40, 15);
        card.add(fatInstructions, BorderLayout.NORTH);
        card.add(fatSlider, BorderLayout.CENTER);
        ActionListener sfl = new SelectFatListener(mealCalculator, this, helper, fatSlider, cards, "readyMeal", cardL);
        addNextAndBackButtons(cards, card, "askProtein", sfl);
        return card;
    }
    
    public JPanel createReadyMealCard(Container container) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea title = createTextArea("Tässä ateriasi:");
        JTextArea main = createTextArea(mealCalculator.getMeal().toString());
        
        JPanel buttons = new JPanel(new GridLayout(2, 1));
        JButton bactToStart = createButton("Valmis");
        JButton back = createButton("Takaisin");
        ActionListener start = new SelectCardListener(cardL, container, "start");
        ActionListener prevCardL = new SelectCardListener(cardL, container, "askFat");
        back.addActionListener(prevCardL);
        bactToStart.addActionListener(start);
        buttons.add(bactToStart);
        buttons.add(back);
        card.add(buttons, BorderLayout.SOUTH);
        card.add(title, BorderLayout.NORTH);
        card.add(main, BorderLayout.CENTER);
        
        
        return card;
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
        slider.setMinorTickSpacing(min);
        slider.setMajorTickSpacing(max / 4);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setLabelTable(slider.createStandardLabels(max / 10));
        slider.setBackground(mainColor);
        return slider;
    }

    private void addNextAndBackButtons(JPanel cards, JPanel card, String prevCard, ActionListener al) {
        JPanel buttons = new JPanel(new GridLayout(2, 1));
        JButton next = createButton("Seuraava");
        JButton back = createButton("Takaisin");
        SelectCardListener prevCardL = new SelectCardListener(cardL, cards, prevCard);
        back.addActionListener(prevCardL);
        ActionListener nextCardL = al;
        next.addActionListener(nextCardL);
        buttons.add(next);
        buttons.add(back);
        card.add(buttons, BorderLayout.SOUTH);
    }

    private ButtonGroup createMainsButtons(JPanel card) throws IOException {
        List<Ingredient> mains = mealCalculator.getMainIngredients();
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

}
