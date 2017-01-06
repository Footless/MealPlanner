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
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
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
    private MealCalcHelper helper;
    private ComponentFactory compFactory;

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
        mealCalculator = new CalculateMeal();
        helper = new MealCalcHelper(mealCalculator);
        compFactory = new ComponentFactory(cardL, helper);
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

        JButton getMeal = compFactory.createButton("Laske ateria");
        SelectCardListener snml = new SelectCardListener(cardL, container, "askMainIngredient");
        getMeal.addActionListener(snml);

        JButton addIngredients = compFactory.createButton("Lisää uusia raaka-aineita");
        SelectCardListener addIngListener = new SelectCardListener(cardL, container, "addIngredient");
        addIngredients.addActionListener(addIngListener);

        JButton browseIngredients = compFactory.createButton("Selaa raaka-aineita");

        startCard.add(getMeal);
        startCard.add(addIngredients);
        startCard.add(browseIngredients);
        return startCard;
    }

    private void createMealCards(JPanel cards, CalcMealView calcMealView) throws IOException {
        JPanel mainIngredientCard = calcMealView.createMainIngredientCard(cards);
        cards.add(mainIngredientCard, "askMainIngredient");
    }
    
    private void createAddIngCards(JPanel cards, AddIngredientsView addIngsView) {
        JPanel searchIngCard = addIngsView.createSearchIngCard(cards);
        cards.add(searchIngCard, "addIngredient");
    }

    public JFrame getFrame() {
        return frame;
    }

    public MealCalcHelper getHelper() {
        return this.helper;
    }
}
