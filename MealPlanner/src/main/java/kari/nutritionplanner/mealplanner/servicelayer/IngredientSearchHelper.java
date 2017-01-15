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

    /**
     * Palauttaa Ingredient-olion annetun nimen perusteella. Vaatii tarkan,
     * oikean nimen toimiakseen.
     *
     * @param name Raaka-aineen koko nimi.
     * @return Ingredient, jossa kaikki arvot asetettuna.
     */
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
     * Poistaa annetun raaka-aineen tietokannasta, käyttäjän omista
     * raaka-aineista.
     *
     * @param ing Poistettava raaka-aine
     * @param select Määrittää mistä raaka-aineista poistetaan, mains, sides,
     * sidesAndMisc vai sauces.
     * @return palauttaa true jos poistaminen onnistui, false kaikissa muissa
     * tapauksissa.
     */
    public boolean removeIngredientFromDB(Ingredient ing, String select) {
        if (databaseOk) {
            boolean result = dbWriter.removeUserIngredient(ing, select);
            if (result) {
                ingredientProcessor.updateIngs();
            }
            return result;
        }
        return false;
    }

    /**
     * Yrittää lisätä annetun raaka-aineen käyttäjän raaka-aineisiin.
     *
     * @param ing Raaka-aine joka halutaan lisätä
     * @param select Määrittää mihin raaka-aineisiin lisätään, mains, sides,
     * sidesAndMisc vai sauces. Raaka-ainetta ei voi lisätä jos se on jo
     * ennestään tietokannassa.
     * @return boolean riippuen onnistumisesta. Jos tietokanta ei ole käytössä,
     * automaattinen false.
     */
    public boolean addIngredientToDatabase(Ingredient ing, String select) {
        if (databaseOk) {
            boolean macroCheck = checkMacroBoundaries(ing, select);
            if (!macroCheck) {
                return false;
            }
            boolean result = dbWriter.addIntoUserIngredients(ing, select);
            if (result) {
                ingredientProcessor.updateIngs();
            }
            return result;
        }
        return false;
    }

    public ProcessIngredients getIngredientProcessor() {
        return ingredientProcessor;
    }

    public DatabaseAccessRead getDbAccess() {
        return dbAccess;
    }

    public DatabaseAccessWrite getDbWriter() {
        return dbWriter;
    }

    public CSVReader getReader() {
        return reader;
    }

    private boolean checkMacroBoundaries(Ingredient ing, String select) {
        if (select.contains("mains") && ing.getProtein() < 5) {
            return false;
        } else if (select.contains("misc") && ing.getCalories() > 100) {
            return false;
        }
        return true;
    }

}
