package kari.nutritionplanner.mealplanner.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.util.database.DatabaseAccess;

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
    private final DatabaseAccess dbAccess;

    /**
     * Konstuktori luo sekä Mapit raaka-aineille, että myös täyttää ne
     * addAll()-metodilla.
     *
     * @param databaseOk ilmaisee onko tietokanta käytössä
     */
    public ProcessIngredients(boolean databaseOk) {
        this.databaseOk = databaseOk;
        if (databaseOk) {
            this.dbAccess = new DatabaseAccess();
        } else {
            this.dbAccess = null;
        }
        this.mainIgredients = new HashMap<>();
        this.sidesAndMisc = new HashMap<>();
        this.sauces = new HashMap<>();
        this.sideIgredients = new HashMap<>();
        addAll();
    }

    private void addMainIngredients() throws SQLException {
        if (databaseOk) {
            mainIgredients = dbAccess.getUserIngredients().get("mains");
        } else {
            CSVReader lfnr = new CSVReader("main_ingredients.csv");
            List<Ingredient> ingredients;
            try {
                ingredients = lfnr.getAllIngredients();
                ingredients.stream().filter((ingredient) -> (addMacrosToIngredient(ingredient))).forEach((ingredient) -> {
                    mainIgredients.put(ingredient.getId(), ingredient);
                });
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Tiedoston lukemisessa tapahtui virhe: " + ex.getMessage(), "Virhe", 0);
            }

        }
    }

    private void addSideIngredients() throws SQLException {
        if (databaseOk) {
            sideIgredients = dbAccess.getUserIngredients().get("sides");
        } else {
            try {
                CSVReader lfnr = new CSVReader("side_ingredients.csv");
                List<Ingredient> ingredients = lfnr.getAllIngredients();

                ingredients.stream().filter((ingredient) -> (addMacrosToIngredient(ingredient))).forEach((ingredient) -> {
                    sideIgredients.put(ingredient.getId(), ingredient);
                });
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Tiedoston lukemisessa tapahtui virhe: " + ex.getMessage(), "Virhe", 0);
            }
        }
    }

    private void addSauces() throws SQLException {
        if (databaseOk) {
            sauces = dbAccess.getUserIngredients().get("sauces");
        } else {
            try {
                CSVReader lfnr = new CSVReader("sauces.csv");
                List<Ingredient> ingredients = lfnr.getAllIngredients();

                ingredients.stream().filter((ingredient) -> (addMacrosToIngredient(ingredient))).forEach((ingredient) -> {
                    sauces.put(ingredient.getId(), ingredient);
                });
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Tiedoston lukemisessa tapahtui virhe: " + ex.getMessage(), "Virhe", 0);
            }
        }
    }

    private void addSidesAndStuffs() throws SQLException {
        if (databaseOk) {
            sidesAndMisc = dbAccess.getUserIngredients().get("sidesAndMisc");
        } else {
            try {
                CSVReader lfnr = new CSVReader("sidesAndStuff.csv");
                List<Ingredient> ingredients = lfnr.getAllIngredients();

                ingredients.stream().filter((ingredient) -> (addMacrosToIngredient(ingredient))).forEach((ingredient) -> {
                    sidesAndMisc.put(ingredient.getId(), ingredient);
                });
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Tiedoston lukemisessa tapahtui virhe: " + ex.getMessage(), "Virhe", 0);
            }
        }
    }

    /**
     * Lisää makrot annettuun raaka-aineeseen. Palauttaa true tai false, sen
     * mukaan onnistuiko tehtävä.
     *
     * @param ing Ingredient-olio
     * @return true tai false onnistumisen mukaan
     * @see CSVReader
     */
    public boolean addMacrosToIngredient(Ingredient ing) {
        if (databaseOk) {
            try {
                dbAccess.getIngredient(ing.getId());
                return true;
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Tietokannan lukemisessa tapahtui virhe: " + ex, "Virhe", 0);
                this.databaseOk = false;
                return false;
            }
        }
        CSVReader fMacroR = new CSVReader("component_value_stub.csv");
        try {
            return fMacroR.searchMacros(ing);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Tiedoston lukemisessa tapahtui virhe: " + ex.getMessage(), "Virhe", 0);
            return false;
        }
    }

    private void addAll() {
        try {
            addMainIngredients();
            addSideIngredients();
            addSauces();
            addSidesAndStuffs();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Tietokannan lukemisessa tapahtui virhe: " + ex, "Virhe", 0);
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
