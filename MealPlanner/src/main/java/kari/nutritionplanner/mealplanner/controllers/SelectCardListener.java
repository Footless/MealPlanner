/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.controllers;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import kari.nutritionplanner.mealplanner.gui.UserInterface;

/**
 * ActionListenerin toteuttava luokka graafista käyttöliittymää varten. Vaihtaa
 * cardLayoutin korttia annettujen parametrien mukaisesti.
 * 
 * @author kari
 */
public class SelectCardListener implements ActionListener {
    private final UserInterface ui;
    private final CardLayout cards;
    private final Container container;
    private final String card;
    
    public SelectCardListener(UserInterface ui, CardLayout cards, Container container, String card) {
        this.ui = ui;
        this.cards = cards;
        this.container = container;
        this.card = card;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() != null && e.getActionCommand().contains("clear")) {
            ui.getHelper().clear();
        }
        cards.show(container, card);
    }
    
}
