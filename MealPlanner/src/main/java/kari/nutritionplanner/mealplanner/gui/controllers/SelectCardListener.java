/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.gui.controllers;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ActionListenerin toteuttava luokka graafista käyttöliittymää varten. Vaihtaa
 * cardLayoutin korttia annettujen parametrien mukaisesti.
 *
 * @author kari
 */
public class SelectCardListener implements ActionListener {

    private final CardLayout cards;
    private final Container container;
    private final String nextCard;

    /**
     * Konstruktori saa parametrina koko käyttöliittymää pyörittävän
     * CardLayoutin, containerin joka sisältää kaikki "kortit" sekä seuraavan
     * kortin "osoitteen" Stringinä. 
     * @param cards CardLayout joka pyörittää koko käyttöliittymää
     * @param container Container Sisältää kaikki kortit
     * @param nextCard String Seuraavan kortin osoite
     */
    public SelectCardListener(CardLayout cards, Container container, String nextCard) {
        this.cards = cards;
        this.container = container;
        this.nextCard = nextCard;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        cards.show(container, nextCard);
    }

}
