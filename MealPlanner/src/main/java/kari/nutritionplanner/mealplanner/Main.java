/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner;

import java.lang.reflect.Field;
import java.nio.charset.Charset;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import kari.nutritionplanner.mealplanner.gui.UserInterface;

/**
 * Pääluokka, joka käynnistää graafisen käyttöliittymän.
 *
 * @author kari
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        Field charset;
        try {
            charset = Charset.class.getDeclaredField("defaultCharset");
            charset.setAccessible(true);
            charset.set(null, null);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException | SecurityException ex) {
            throw new ExceptionInInitializerError(ex);
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException ex) {
            throw new ExceptionInInitializerError(ex);
        }
        UserInterface ui = new UserInterface();
        SwingUtilities.invokeLater(ui);
    }
}
