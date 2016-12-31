/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.gui;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import kari.nutritionplanner.mealplanner.domain.Meal;
import kari.nutritionplanner.mealplanner.servicelayer.CalculateMeal;

/**
 *
 * @author kari
 */
public class SelectMainIngListener implements ActionListener {
    
    private final CardLayout cards;
    private final Container container;
    private final JComboBox box;
    private final JSlider proteinSlider;
    private final JSlider fatSlider;
    private final JSlider calorieSlider;
    private CalculateMeal cm;
    private UserInterface ui;

    public SelectMainIngListener(UserInterface ui, CardLayout cards, Container container, JComboBox box, JSlider proteinSlider, 
            JSlider fatSlider, JSlider calorieSlider, CalculateMeal cm) {
        this.cards = cards;
        this.container = container;
        this.box = box;
        this.proteinSlider = proteinSlider;
        this.fatSlider = fatSlider;
        this.cm = cm;
        this.calorieSlider = calorieSlider;
        this.ui = ui;
    }
    
    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String mainIng = (String)box.getSelectedItem();
        int id = cm.getMainIngId(mainIng);
        if (cm.calculateAllMeal(id, calorieSlider.getValue() , proteinSlider.getValue(), fatSlider.getValue())) {
            ui.createMealCard(container);
            cards.show(container, "readyMeal");
        } else {
            JOptionPane.showMessageDialog(container, "Aterian luonti epäonnistui. Valitse vähempirasvainen pääraaka-aine tai lisää"
                    + " rasvan määrää");
        }
    }
    
}
