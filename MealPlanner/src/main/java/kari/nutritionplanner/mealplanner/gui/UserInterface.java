/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.gui;

import kari.nutritionplanner.mealplanner.gui.controllers.SelectCardListener;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import kari.nutritionplanner.mealplanner.gui.controllers.BrowseIngsListener;
import kari.nutritionplanner.mealplanner.gui.controllers.StartNewMealListener;
import kari.nutritionplanner.mealplanner.servicelayer.CalculateMeal;
import kari.nutritionplanner.mealplanner.servicelayer.IngredientSearchHelper;
import kari.nutritionplanner.mealplanner.servicelayer.MealCalcHelper;
import kari.nutritionplanner.mealplanner.util.ProcessIngredients;

/**
 * Graafinen käyttöliittymä Meal Plannerille. Luo aloituskortin sekä
 * CalcMealView'n ja AddIngsView'n joissa varsinainen työ tapahtuu.
 *
 * @author kari
 */
public class UserInterface implements Runnable {

    private JFrame frame;
    private CardLayout cardL;
    private CalculateMeal mealCalculator;
    private MealCalcHelper helper;
    private ComponentFactory compFactory;
    private final boolean databaseOk;
    private IngredientSearchHelper searchHelper;

    public UserInterface(boolean databaseOk) {
        this.databaseOk = databaseOk;
    }
    @Override
    public void run() {
        frame = new JFrame("Meal Planner");
        frame.setPreferredSize(new Dimension(650, 500));
        cardL = new CardLayout();

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createComponents(frame.getContentPane());

        frame.setLocationByPlatform(true);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    private void createComponents(Container contentPane) {
        ProcessIngredients ingredientProcessor = new ProcessIngredients(databaseOk);
        mealCalculator = new CalculateMeal(ingredientProcessor);
        helper = new MealCalcHelper(ingredientProcessor);
        this.searchHelper = new IngredientSearchHelper(ingredientProcessor);
        compFactory = new ComponentFactory(cardL, helper, searchHelper);
        JPanel cards = new JPanel(cardL);

        JPanel startCard = createStartCard(cards);
        cards.add(startCard, "start");

        CalcMealView calcMealView = new CalcMealView(cardL, mealCalculator, helper, compFactory);
        createMealCards(cards, calcMealView);

        AddIngredientsView addIngsView = new AddIngredientsView(cardL, compFactory);
        createAddIngCards(cards, addIngsView);

        contentPane.add(cards);
    }

    private JPanel createStartCard(Container container) {
        JPanel startCard = new JPanel();
        startCard.setBackground(Color.DARK_GRAY);
        startCard.setLayout(new GridLayout(3, 1, 2, 2));

        CalcMealView calcMealView = new CalcMealView(cardL, mealCalculator, helper, compFactory);
        
        JButton getMeal = compFactory.createButton("Laske ateria");
        ActionListener startNewMealListener = new StartNewMealListener(this, cardL, container, "askMainIngredient", calcMealView);
//        SelectCardListener snml = new SelectCardListener(cardL, container, "askMainIngredient");
        getMeal.addActionListener(startNewMealListener);

        JButton addIngredients = compFactory.createButton("Lisää uusia raaka-aineita");
        SelectCardListener addIngListener = new SelectCardListener(cardL, container, "addIngredient");
        addIngredients.addActionListener(addIngListener);

        JButton browseIngredients = compFactory.createButton("Selaa raaka-aineita");
        BrowseIngsListener browseIngsL = new BrowseIngsListener(cardL, container, compFactory, "browseIngredients");
        browseIngredients.addActionListener(browseIngsL);

        startCard.add(getMeal);
        startCard.add(addIngredients);
        startCard.add(browseIngredients);
        return startCard;
    }

    public void createMealCards(Container container, CalcMealView calcMealView) {
        JPanel mainIngredientCard = calcMealView.createMainIngredientCard(container);
        container.add(mainIngredientCard, "askMainIngredient");
    }

    private void createAddIngCards(JPanel cards, AddIngredientsView addIngsView) {
        JPanel searchIngCard = addIngsView.createSearchIngCard(cards);
        cards.add(searchIngCard, "addIngredient");
    }

    public MealCalcHelper getHelper() {
        return this.helper;
    }
    
    public boolean databaseOk() {
        return this.databaseOk;
    }
}
