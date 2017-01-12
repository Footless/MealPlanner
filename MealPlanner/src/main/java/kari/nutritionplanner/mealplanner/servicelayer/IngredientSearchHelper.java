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
package kari.nutritionplanner.mealplanner.servicelayer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.util.CSVReader;
import kari.nutritionplanner.mealplanner.util.ProcessIngredients;
import kari.nutritionplanner.mealplanner.util.database.DatabaseAccessRead;
import kari.nutritionplanner.mealplanner.util.database.DatabaseAccessWrite;

/**
 * Apuluokka tietokannan ja CSV-tiedostojen käsittelyyn. Suorittaa hakuja
 * tietokannasta ja lisää raaka-aineita käyttäjän omiin raaka-aineisiin.
 *
 * @author kari
 */
public class IngredientSearchHelper {

    private final CSVReader reader;
    private boolean databaseOk;
    private final DatabaseAccessRead dbAccess;
    private final DatabaseAccessWrite dbWriter;
    private final ProcessIngredients ingredientProcessor;

    /**
     * Konstruktori luo Readerin, joka lukee tiedostoa food_utf.csv joka
     * sisältää kaikkien Finelin tietokannan elintarvikkeiden nimet ja id:t sekä
     * tarjoaa yhteyden tietokantaan, josta saatavilla sekä nimet, id:t että
     * makrot.
     *
     * @param ingredientProcessor ProcessIngredients-olio raaka-aineiden hakua
     * varten.
     */
    public IngredientSearchHelper(ProcessIngredients ingredientProcessor) {
        this.ingredientProcessor = ingredientProcessor;
        this.reader = new CSVReader("food_utf.csv");
        this.databaseOk = ingredientProcessor.getDatabaseOk();
        this.dbAccess = new DatabaseAccessRead();
        this.dbWriter = new DatabaseAccessWrite();
    }

    /**
     * Hakee hakutermillä joko tietokannasta tai sen ollessa pois käytöstä
     * CSV-tiedostoista.
     *
     * @param s hakutermi, jonka käyttäjä on syöttänyt käyttöliittymässä
     * @return listan hakua vastaavista raaka-aineista
     */
    public List<Ingredient> search(String s) {
        if (databaseOk) {
            try {
                return dbAccess.searchIngredients(s);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Tietokannan haussa tapahtui virhe: " + ex.getLocalizedMessage(), "Virhe", 0);
                this.databaseOk = false;
                return null;
            }
        }
        try {
            return reader.search(s);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Tiedoston luvussa tapahtui virhe: " + ex.getLocalizedMessage(), "Virhe", 0);
            return null;
        }
    }
    
    public Ingredient getIngredientByName(String name) {
        if (databaseOk) {
            try {
                int id = dbAccess.getIngredientIdByName(name);
                return dbAccess.getIngredient(id);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Tietokannan haussa tapahtui virhe: " + ex.getLocalizedMessage(), "Virhe", 0);
            }
        }
        return null;
    }

    /**
     * Yrittää lisätä annetun raaka-aineen käyttäjän raaka-aineisiin.
     *
     * @param ing Raaka-aine joka halutaan lisätä
     * @param select Määrittää mihin raaka-aineisiin lisätään, mains, sides vai
     * sauces. Raaka-ainetta ei voi lisätä jos se on jo ennestään tietokannassa.
     * @return boolean riippuen onnistumisesta. Jos tietokanta ei ole käytössä,
     * automaattinen false.
     */
    public boolean addIngredientToDatabase(Ingredient ing, String select) {
        if (databaseOk) {
            return dbWriter.addIntoUserIngredients(ing, select);
        }
        return false;
    }
}
