package kari.nutritionplanner.mealplanner.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.util.database.DatabaseAccessRead;

/**
 * Hakee raaka-aineet joko tietokannasta tai sen ollessa poissa käytöstä
 * SCVReaderiä käyttäen SCV-tiedostoista. Raaka-aineet tallennetaan
 * Map-olioihin, tyypin mukaisesti.
 *
 * @see CSVReader
 * @author kari
 */
public class ProcessIngredients {

    private Map<Integer, Ingredient> mainIgredients;
    private Map<Integer, Ingredient> sideIgredients;
    private Map<Integer, Ingredient> sauces;
    private Map<Integer, Ingredient> sidesAndMisc;
    private boolean databaseOk;
    private final DatabaseAccessRead dbAccess;

    /**
     * Konstuktori luo sekä Mapit raaka-aineille, että myös täyttää ne
     * addAll()-metodilla.
     *
     * @param databaseOk ilmaisee onko tietokanta käytössä
     */
    public ProcessIngredients(boolean databaseOk) {
        this.databaseOk = databaseOk;
        if (databaseOk) {
            this.dbAccess = new DatabaseAccessRead();
        } else {
            this.dbAccess = null;
        }
        this.mainIgredients = new HashMap<>();
        this.sidesAndMisc = new HashMap<>();
        this.sauces = new HashMap<>();
        this.sideIgredients = new HashMap<>();
        addAll();
    }

    private void addMainIngredients() throws SQLException, IOException {
        if (databaseOk) {
            mainIgredients = dbAccess.getUserIngredients().get("mains");
        } else {
            CSVReader lfnr = new CSVReader("main_ingredients.csv");
            List<Ingredient> ingredients;
            ingredients = lfnr.getAllIngredients();
            lfnr.searchAllMacros(ingredients);
        }
    }

    private void addSideIngredients() throws SQLException, IOException {
        if (databaseOk) {
            sideIgredients = dbAccess.getUserIngredients().get("sides");
        } else {
            CSVReader lfnr = new CSVReader("side_ingredients.csv");
            List<Ingredient> ingredients = lfnr.getAllIngredients();
            lfnr.searchAllMacros(ingredients);
        }
    }

    private void addSauces() throws SQLException, IOException {
        if (databaseOk) {
            sauces = dbAccess.getUserIngredients().get("sauces");
        } else {
            CSVReader lfnr = new CSVReader("sauces.csv");
            List<Ingredient> ingredients = lfnr.getAllIngredients();
            lfnr.searchAllMacros(ingredients);
        }
    }

    private void addSidesAndStuffs() throws SQLException, IOException {
        if (databaseOk) {
            sidesAndMisc = dbAccess.getUserIngredients().get("sidesAndMisc");
        } else {
            CSVReader lfnr = new CSVReader("sidesAndStuff.csv");
            List<Ingredient> ingredients = lfnr.getAllIngredients();
            lfnr.searchAllMacros(ingredients);
        }
    }

    private void addAll() {
        try {
            addMainIngredients();
            addSideIngredients();
            addSauces();
            addSidesAndStuffs();
        } catch (SQLException | IOException ex) {
            JOptionPane.showMessageDialog(null, "Tietokannan tai tiedoston lukemisessa tapahtui virhe: " + ex.getLocalizedMessage(), "Virhe", 0);
            this.databaseOk = false;
        }
    }

    /**
     * Palauttaa listan pääraaka-aineista.
     *
     * @return List-olio, jossa kaikki saatavissa olevat raaka-aineet.
     */
    public List<Ingredient> getMainIngredients() {
        List<Ingredient> mains = new ArrayList<>();
        mainIgredients.values().stream().forEach((ing) -> {
            mains.add(ing);
        });
        return mains;
    }

    /**
     * Palauttaa listan kaikista saatavilla olevista lisäkkeistä.
     *
     * @return List jossa kaikki lisäkkeet.
     */
    public List<Ingredient> getSideIngredients() {
        List<Ingredient> sides = new ArrayList<>();
        sideIgredients.values().stream().forEach((ing) -> {
            sides.add(ing);
        });
        return sides;
    }

//    /**
//     * Palauttaa listan kaikista saatavilla olevista kastikkeista.
//     *
//     * @return List jossa kaikki kastikkeet
//     */
//    public List<Ingredient> getSauces() {
//        List<Ingredient> saucesAsList = new ArrayList<>();
//        this.sauces.values().stream().forEach(ing -> {
//            saucesAsList.add(ing);
//        });
//        return saucesAsList;
//    }
    /**
     * Palauttaa kaikki raaka-aineet, yhdessä Mapissa, jossa sisällä neljä
     * Mappia, yksi jokaiselle raaka-ainekategorialle.
     *
     * @return Map raaka-aineista.
     */
    public Map<String, Map<Integer, Ingredient>> getIngredients() {
        Map<String, Map<Integer, Ingredient>> ingredients = new HashMap<>();
        ingredients.put("mains", mainIgredients);
        ingredients.put("sides", sideIgredients);
        ingredients.put("sauces", sauces);
        ingredients.put("sidesAndMisc", sidesAndMisc);
        return ingredients;
    }

    public boolean getDatabaseOk() {
        return this.databaseOk;
    }
}
