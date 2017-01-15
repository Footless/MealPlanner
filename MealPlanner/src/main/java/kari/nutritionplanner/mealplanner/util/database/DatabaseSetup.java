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

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.util.CSVReader;

/**
 * Tietokannan luomiseen ja alustamiseen tarkoitettu luokka.
 *
 * @author kari
 */
public class DatabaseSetup {

    private Connection conn;
    private final static String CONNADDRESS = "jdbc:derby:components;create=true";

    /**
     * Konstruktori avaa tietokannan ja luo sen tarvittaessa, sekä siirtää
     * tiedot CSV-tiedostoista tietokantaan.
     *
     * @throws SQLException Jos yhteys epäonnistuu tai jos joku
     * tietokantaoperaatioista epäonnistuu.
     * @throws java.io.IOException Jos tiedostojen luku epäonnistuu.
     */
    public DatabaseSetup() throws SQLException, IOException {
        conn = null;
        try {
            setDBSystemDir();
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection(CONNADDRESS);
            setupDatabase();
            conn.close();
        } catch (ClassNotFoundException ex) {
            System.err.print("Tapahtui virhe tietokantaa määrittäessä: " + ex.getLocalizedMessage());
        }
    }

    private void setDBSystemDir() {
        System.setProperty("derby.system.home", "db");
    }

    private void setupDatabase() throws SQLException, IOException {
        Statement stmt = conn.createStatement();
        // tarkastaa onko tauluja olemassa
        DatabaseMetaData dbm = conn.getMetaData();
        ResultSet tables = dbm.getTables(null, null, "MAINS", null);
        // jos ei ole, tehdään ne
        if (!tables.next()) {
            // luodaan taulut käyttäjän raaka-aineille, jos niitä ei vielä ole luotu
            stmt.executeUpdate("Create table mains(id int primary key, name varchar(100))");
            stmt.executeUpdate("Create table sides(id int primary key, name varchar(100))");
            stmt.executeUpdate("Create table misc(id int primary key, name varchar(100))");
            stmt.executeUpdate("Create table sauces(id int primary key, name varchar(100))");
            // luodaan taulut haettavissa oleville raaka-aineille
            stmt.executeUpdate("Create table ingredients(id int primary key, name varchar(100))");
            // sekä makroille
            stmt.executeUpdate("Create table macros(id int primary key, calories float, protein float, fat float, carb float, fiber float)");
            addIngredientsToDB();
        }
    }

    private void addIngredientsToDB() throws SQLException, IOException {
        Statement stmt = conn.createStatement();
        addMains(stmt);
        addSides(stmt);
        addSauces(stmt);
        addMiscs(stmt);
        addAllIngredients(stmt);
    }

    private void addAllIngredients(Statement stmt) throws SQLException, IOException {
        CSVReader reader = new CSVReader("food_utf.csv");
        List<Ingredient> allIngs = reader.getAllIngredients();
        reader.closeReader();
        for (Ingredient ing : allIngs) {
            
            stmt.execute("INSERT INTO INGREDIENTS VALUES(" + ing.getId() + ", '" + ing.getName().toLowerCase() + "')");
        }
        addMacros(allIngs, stmt);
    }

    private void addMacros(List<Ingredient> allIngs, Statement stmt) throws SQLException, IOException {
        CSVReader reader = new CSVReader("component_value.csv");
        reader.searchAllMacros(allIngs);
        for (Ingredient ing : allIngs) {
            stmt.executeUpdate("INSERT INTO MACROS VALUES(" + ing.getId() + "," + ing.getCalories() + ","
                    + ing.getProtein() + "," + ing.getFat() + "," + ing.getCarb() + ", " + ing.getFiber() + ")");
        }
        reader.closeReader();
    }

    private void addMiscs(Statement stmt) throws SQLException, IOException {
        CSVReader reader = new CSVReader("sidesAndStuff.csv");
        List<Ingredient> misc = reader.getAllIngredients();
        reader.closeReader();
        for (Ingredient ing : misc) {
            stmt.executeUpdate("INSERT INTO MISC VALUES(" + ing.getId() + ", '" + ing.getName() + "')");
        }
    }

    private void addSauces(Statement stmt) throws SQLException, IOException {
        CSVReader reader = new CSVReader("sauces.csv");
        List<Ingredient> sauces = reader.getAllIngredients();
        reader.closeReader();
        for (Ingredient ing : sauces) {
            stmt.executeUpdate("INSERT INTO SAUCES VALUES(" + ing.getId() + ", '" + ing.getName() + "')");
        }
    }

    private void addSides(Statement stmt) throws SQLException, IOException {
        CSVReader reader = new CSVReader("side_ingredients.csv");
        List<Ingredient> sides = reader.getAllIngredients();
        reader.closeReader();
        for (Ingredient ing : sides) {
            stmt.executeUpdate("INSERT INTO SIDES VALUES(" + ing.getId() + ", '" + ing.getName() + "')");
        }
    }

    private void addMains(Statement stmt) throws SQLException, IOException {
        CSVReader reader = new CSVReader("main_ingredients.csv");
        List<Ingredient> mains = reader.getAllIngredients();
        reader.closeReader();
        for (Ingredient ing : mains) {
            stmt.executeUpdate("INSERT INTO MAINS VALUES(" + ing.getId() + ", '" + ing.getName() + "')");
        }
    }
}
