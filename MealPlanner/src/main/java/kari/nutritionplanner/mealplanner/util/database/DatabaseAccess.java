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
package kari.nutritionplanner.mealplanner.util.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kari.nutritionplanner.mealplanner.domain.Ingredient;

/**
 * Ottaa yhteyden tietokantaan ja suorittaa hakuoperaatioita sieltä.
 *
 * @author kari
 */
public class DatabaseAccess {

    private final static String CONNECTIONADDRESS = "jdbc:derby:/home/kari/dev/MealPlanner/MealPlanner/src/main/resources/components;create=false";
    private Connection conn;

    /**
     * Tarkistaa onko tietokanta olemassa ja onko siellä halutut taulut.
     * Tarkistaa sekä ensin luotavan taulun että viimeisenä luotavan taulun.
     *
     * @return true tai false sen mukaan onko haluttuja tauluja olemassa.
     * @throws SQLException jos tietokantaa ei löydy ja yhteys epäonnistuu.
     */
    public boolean databaseOk() throws SQLException {
        conn = DriverManager.getConnection(CONNECTIONADDRESS);
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "MAINS", null);
        if (tables.next()) {
            tables = dbm.getTables(null, null, "MACROS", null);
            if (tables.next()) {
                conn.close();
                return true;
            }
        }
        conn.close();
        return false;
    }

    /**
     * Hakee tietokannasta id-numeroa vastaavan raaka-aineen, lisää nimen sekä
     * makrot.
     *
     * @param id Raaka-aineen id-numero
     * @return Ingredient-olio, jossa nimi ja makrot asetettuna, haun
     * epäonnistuessa Ingredient on null
     * @throws SQLException jos yhteys tietokantaan epäonnistuu
     */
    public Ingredient getIngredient(int id) throws SQLException {
        conn = DriverManager.getConnection(CONNECTIONADDRESS);
        Statement stmt = conn.createStatement();
        Ingredient ing = null;
        ResultSet rs = stmt.executeQuery("SELECT * FROM INGREDIENTS WHERE id = " + id);
        while (rs.next()) {
            ing = new Ingredient(id, rs.getString("name"));
        }
        addMacros(stmt, ing);

        conn.close();
        return ing;
    }

    private void addMacros(Statement stmt, Ingredient ing) throws SQLException {
        ResultSet macros = stmt.executeQuery("SELECT * FROM MACROS WHERE id = " + ing.getId());
        while (macros.next()) {
            ing.setCalories(macros.getDouble(2));
//            System.out.println("id: " + ing.getId() + "nimi: " + ing.getName() + ": calories: " + macros.getDouble(2));
            ing.setProtein(macros.getDouble(3));
            ing.setFat(macros.getDouble(4));
            ing.setCarb(macros.getDouble(5));
            ing.setFiber(macros.getDouble(6));
        }
    }

    /**
     * Hakee raaka-ainetta nimellä ja palauttaa id-numeron kokonaislukuna.
     * Käytetään vain tapauksissa joissa hakuparametri on varmasti
     * yksiselitteinen.
     *
     * @param s Raaka-aineen nimi
     * @return Raaka-aineen id-numero kokonaislukuna
     * @throws SQLException jos yhteys tietokantaan epäonnistuu
     */
    public int getIngredientIdByName(String s) throws SQLException {
        conn = DriverManager.getConnection(CONNECTIONADDRESS);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id FROM INGREDIENTS WHERE name = '" + s + "'");
        while (rs.next()) {
            int id = rs.getInt("id");
            conn.close();
            return id;
        }
        conn.close();
        return 0;
    }

    /**
     * Etsii raaka-aineita tietokannasta.
     *
     * @param s hakutermi
     * @return Lista hakutermiin täsmäävistä raaka-aineista
     * @throws SQLException jos yhteys epäonnistuu
     */
    public List<Ingredient> searchIngredients(String s) throws SQLException {
        conn = DriverManager.getConnection(CONNECTIONADDRESS);
        Statement stmt = conn.createStatement();
        List<Ingredient> ings = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM INGREDIENTS WHERE name LIKE '%" + s.toLowerCase() + "%'");
        while (rs.next()) {
            Ingredient ing = new Ingredient(rs.getInt("id"), rs.getString("name"));
            ings.add(ing);
        }
        for (Ingredient ing : ings) {
            addMacros(stmt, ing);
        }
        conn.close();
        return ings;
    }

    /**
     * Hakee kaikki käyttäjän käyttöön määrittelemät raaka-aineet.
     *
     * @return Map, jossa sisällä oma Map kaikille eri komponenteille. Avaimet
     * sisällä oleville mapeille seuraavasti: mains = pääraaka-aineet, sides =
     * lisäkkeet, sauces = kastikkeet ja misc = lisukkeet yms. Varsinaisissa
     * mapeissa avaimena toimii raaka-aineen id-numero.
     * @throws SQLException jos yhteys tietokantaan epäonnistuu
     */
    public Map<String, Map<Integer, Ingredient>> getUserIngredients() throws SQLException {
        conn = DriverManager.getConnection(CONNECTIONADDRESS);
        Statement stmt = conn.createStatement();
        Map<String, Map<Integer, Ingredient>> ings = new HashMap<>();
        Map<Integer, Ingredient> mains = addIngredientsToMap(stmt, "MAINS");
        Map<Integer, Ingredient> sides = addIngredientsToMap(stmt, "SIDES");
        Map<Integer, Ingredient> sauces = addIngredientsToMap(stmt, "SAUCES");
        Map<Integer, Ingredient> misc = addIngredientsToMap(stmt, "MISC");
        ings.put("mains", mains);
        ings.put("sides", sides);
        ings.put("sauces", sauces);
        ings.put("sidesAndMisc", misc);
        conn.close();
        return ings;
    }

    private Map<Integer, Ingredient> addIngredientsToMap(Statement stmt, String select) throws SQLException {
        Map<Integer, Ingredient> ings = new HashMap<>();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + select);
        while (rs.next()) {
            int id = rs.getInt("id");
            ings.put(id, getIngredient(id));
        }
        return ings;
    }
}
