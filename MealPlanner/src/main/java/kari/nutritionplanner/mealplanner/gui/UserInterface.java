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
        SelectCardListener snml = new SelectCardListener(this, cardL, container, "askMainIngredient");
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
//        JPanel proteinsCard = createProteinsCard(cards);
//        JPanel fatsCard = createFatsCard(cards);

        cards.add(mainIngredientCard, "askMainIngredient");
//        cards.add(proteinsCard, "askProtein");
//        cards.add(fatsCard, "askFat");
    }

    public JFrame getFrame() {
        return frame;
    }

    private JPanel createMainIngredientCard(JPanel cards) throws IOException {
        mealCalculator = new CalculateMeal();
        helper = new MealCalcHelper(mealCalculator);
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea instructions = createTextArea("Valitse listasta haluamasi raaka-aine");
        card.add(instructions, BorderLayout.NORTH);
        ButtonGroup mainsButtonGroup = createMainsButtons(card);
        ActionListener sml = new SelectMainIngListener(this, cardL, helper, mainsButtonGroup, cards, "askCalories");
        addNextAndBackButtons(cards, card, "start", sml);
        return card;
    }

    public JPanel createCaloriesCard(Container container) throws IOException {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea caloriesInstructions = createTextArea("Valitse haluamasi määrä kaloreita:");
        Ingredient ing = mealCalculator.getIngredients().get("mains").get(helper.getMainIngredientId());
        double mul = getMultiplier(ing);
        int min = (int) Math.ceil(ing.getCalories() * mul);
        JSlider calorieSlider = createSlider(min, 800, 800 / 2);
        card.add(caloriesInstructions, BorderLayout.NORTH);
        card.add(calorieSlider, BorderLayout.CENTER);
        ActionListener scl = new SelectCaloriesListener(this, helper, calorieSlider, container, "askProtein", cardL);
        addNextAndBackButtons(container, card, "askMainIngredient", scl);
        return card;
    }

    public JPanel createProteinsCard(Container container) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea proteinInstructions = createTextArea("Valitse haluamasi proteiinin määrä:");
        Ingredient ing = mealCalculator.getIngredients().get("mains").get(helper.getMainIngredientId());
        double mul = getMultiplier(ing);
        int min = (int) Math.ceil(ing.getProtein() * mul);
        JSlider proteinSlider = createSlider(min, min * 5, min * 5 / 2);
        card.add(proteinInstructions, BorderLayout.NORTH);
        card.add(proteinSlider, BorderLayout.CENTER);
        ActionListener spl = new SelectProtListener(this, helper, proteinSlider, container, "askFat", cardL);
        addNextAndBackButtons(container, card, "askCalories", spl);
        return card;
    }

    public JPanel createFatsCard(Container container) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea fatInstructions = createTextArea("Valitse haluamasi rasvan määrä:");
        Ingredient ing = mealCalculator.getIngredients().get("mains").get(helper.getMainIngredientId());
        double mul = getMultiplier(ing);
        int min = (int) Math.ceil(ing.getFat() * mul);
        JSlider fatSlider = createSlider(min, 40, 40 / 2);
        card.add(fatInstructions, BorderLayout.NORTH);
        card.add(fatSlider, BorderLayout.CENTER);
        ActionListener sfl = new SelectFatListener(mealCalculator, this, helper, fatSlider, container, "readyMeal", cardL);
        addNextAndBackButtons(container, card, "askProtein", sfl);
        return card;
    }
    
    public JPanel createReadyMealCard(Container container) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.black);
        JTextArea title = createTextArea("Tässä ateriasi:");
        createMealText(card);
        JPanel buttons = new JPanel(new GridLayout(2, 1));
        JButton backToStart = createButton("Valmis");
        backToStart.setActionCommand("clear");
        JButton back = createButton("Takaisin");
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

    private void addNextAndBackButtons(Container container, JPanel card, String prevCard, ActionListener al) {
        JPanel buttons = new JPanel(new GridLayout(2, 1));
        JButton next = createButton("Seuraava");
        JButton back = createButton("Takaisin");
        SelectCardListener prevCardL = new SelectCardListener(this, cardL, container, prevCard);
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

    private double getMultiplier(Ingredient ing) {
        if (ing.getProtein() > 23) {
            return 0.5;
        } else {
            return 1;
        }
    }

    private void createMealText(JPanel card) {
        Meal meal = mealCalculator.getMeal();
        JPanel pane = new JPanel();
        pane.setBackground(Color.cyan);
        BoxLayout boxL = new BoxLayout(pane, BoxLayout.PAGE_AXIS);
        pane.setLayout(boxL);
        pane.add(Box.createRigidArea(new Dimension(0,10)));
        JTextArea title = createTextArea("Komponentit ja määrät:");
        pane.add(title);
        pane.add(Box.createRigidArea(new Dimension(0,5)));
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
        pane.add(Box.createRigidArea(new Dimension(0,5)));
        JTextArea subTitle = createTextArea("Ravintosisältö");
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
        card.add(pane, BorderLayout.CENTER);
    }

    public MealCalcHelper getHelper() {
        return this.helper;
    }

    private JTextArea createIngText(Ingredient ing, double amount) {
        JTextArea jta = createTextArea(ing + ": " + Math.ceil(amount * 100) + "gr");
        return jta;
    }

    private JTextArea createMacroText(String macroName, double macros, int desiredMacros, String unit) {
        JTextArea jta = createTextArea(macroName + ": " + Math.ceil(macros) + unit + "(" + desiredMacros + unit + ")");
        return jta;
    }
    
    private JTextArea createMacroText(String macroName, double macros, String unit) {
        JTextArea jta = createTextArea(macroName + ": " + Math.ceil(macros) + unit);
        return jta;
    }
}
