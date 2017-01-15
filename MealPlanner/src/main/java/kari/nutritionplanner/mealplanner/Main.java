/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.gui.UserInterface;
import kari.nutritionplanner.mealplanner.util.database.DatabaseAccessRead;
import kari.nutritionplanner.mealplanner.util.database.DatabaseAccessWrite;
import kari.nutritionplanner.mealplanner.util.database.DatabaseSetup;

/**
 * Pääluokka, joka käynnistää graafisen käyttöliittymän. Tarkistaa tietokannan
 * ja ensimmäisellä käynnistyksellä myös alustaa sen.
 *
 * @author kari
 */
public class Main {

    /**
     * Käynnistää graafisen käyttöliittymän. Asettaa ensin UTF-8:n merkistöksi,
     * hakee järjestelmän graafiset asetukset ja lopulta tarkastaa ja
     * tarvittaessa alustaa tietokannan.
     *
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
            JOptionPane.showMessageDialog(null, "Tapahtui seuraava virhe ohjelmaa käynnistäessä: " + ex, "Virhe", 0);
            throw new Error(ex);
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException | IllegalAccessException ex) {
            JOptionPane.showMessageDialog(null, "Tapahtui seuraava virhe ohjelmaa käynnistäessä: " + ex, "Virhe", 0);
            throw new Error(ex);
        }
        boolean dataBaseOk = false;
        try {
            new DatabaseSetup();
            DatabaseAccessRead dbAccess = new DatabaseAccessRead();
            DatabaseAccessWrite dbWrite = new DatabaseAccessWrite();
            // seuraavat rivit poistavat PIT-testien läpipäästämät raaka-aineet, jotka muuten rikkoisivat vähän kaikkea.
            dbWrite.removeUserIngredient(new Ingredient(99999, "test"), "mains");
            dbWrite.removeUserIngredient(new Ingredient(99999, "test"), "misc");
            dataBaseOk = dbAccess.databaseOk();
        } catch (SQLException | IOException ex) {
            showErrorMessage(ex);
            dataBaseOk = false;
        }
        if (!dataBaseOk) {
            showErrorMessage();
        }
        UserInterface ui = new UserInterface(dataBaseOk);
        SwingUtilities.invokeLater(ui);
    }

    private static void showErrorMessage() {
        JOptionPane.showMessageDialog(null, "Tapahtui virhe tietokantaa ladattaessa, ohjelma käyttää "
                + "ainoastaan supistettua tietokantaa.", "Virhe tietokantayhteydessä", 0);
    }
    
    private static void showErrorMessage(Exception ex) {
        JOptionPane.showMessageDialog(null, "Tapahtui virhe tietokantaa ladattaessa, ohjelma käyttää "
                + "ainoastaan supistettua tietokantaa. Virhekoodi: " + ex.toString(), "Virhe tietokantayhteydessä", 0);
    }
}
