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
package kari.nutritionplanner.mealplanner.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import kari.nutritionplanner.mealplanner.servicelayer.IngredientSearchHelper;

/**
 *
 * @author kari
 */
public class AddToIngsListener implements ActionListener {

    private final JList list;

    public AddToIngsListener(JPanel searchFieldComp) throws IOException {
        if (searchFieldComp.getComponentCount() > 2) {
            JScrollPane scroll = (JScrollPane) searchFieldComp.getComponent(2);
            this.list = (JList) scroll.getViewport().getComponent(0);
        } else {
            this.list = new JList();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("main")) {
            String name = (String) list.getSelectedValue();
            int response = JOptionPane.showConfirmDialog(null, "Haluako todella lisätä raaka-aineen " + name + " "
                    + "pääraaka-aineisiin?", "Varmista", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                showErrorMessage();
            }
        } else if (e.getActionCommand().equalsIgnoreCase("side")) {
            showErrorMessage();
        } else {
            showErrorMessage();
        }
    }
    
    private void showErrorMessage() {
        JOptionPane.showMessageDialog(null, "Valitettavasti tässä vaiheessa kehitystä raaka-aineiden lisääminen ei"
                        + " vielä onnistu.");
    }
}
