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
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import kari.nutritionplanner.mealplanner.domain.Ingredient;

/**
 * Hallinnoi käyttäjän määrittelemiä raaka-aineita tietokannassa. Mahdollistaa
 * raaka-aineiden lisäämisen sekä poistamisen.
 *
 * @author kari
 */
public class DatabaseAccessWrite {

    private final static String CONNECTIONADDRESS = "jdbc:derby:/home/kari/dev/MealPlanner/MealPlanner/src/main/resources/components;create=false";
    private Connection conn;

    /**
     * Lisää annetun raaka-aineen käyttäjän omiin raaka-aineisiin. Tarkistaa
     * ensin onko raaka-aine jo lisättynä.
     *
     * @param ing Lisättävä raaka-aine
     * @param select Osoittaa mihin tauluun raaka-aine lisätään, mains, sides tai sauce
     * @return boolean sen mukaan onnistuiko raaka-aineen lisääminen
     */
    public boolean addIntoUserIngredients(Ingredient ing, String select) {
//        if (!select.equalsIgnoreCase("mains") || !select.equalsIgnoreCase("sides") || !select.equalsIgnoreCase("sauces")) {
//            System.out.println("hä?");
//            return false;
//        }
        try {
            conn = DriverManager.getConnection(CONNECTIONADDRESS);
            boolean check = checkIfIngredientExistsInDatabase(ing, select);
            if (!check) {
                return addIngredientToDatabase(ing, select);
            }
            return false;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Tietokannan käsittelyssä tapahtui virhe: " + ex.getLocalizedMessage(), "Virhe", 0);
        }
        return false;
    }

    private boolean checkIfIngredientExistsInDatabase(Ingredient ing, String select) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM " + select + " WHERE id =" + ing.getId());
        while (rs.next()) {
            if (rs.getInt("id") == ing.getId()) {
                System.out.println(rs.getInt("id"));
                return true;
            }
        }
        return false;
    }

    private boolean addIngredientToDatabase(Ingredient ing, String select) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO " + select + " VALUES(" + ing.getId() + ", '" + ing.getName() + "')");
        return true;
    }
}
