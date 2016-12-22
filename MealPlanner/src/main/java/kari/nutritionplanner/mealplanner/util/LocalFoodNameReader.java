/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import kari.nutritionplanner.mealplanner.domain.Ingredient;

/**
 *
 * @author kari
 */
public class LocalFoodNameReader extends SCVReader {

    public LocalFoodNameReader(String fileName) {
        super(fileName);
    }

    public List<Ingredient> searchAll() {
        String line = null;
        Scanner scanner = null;
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            while ((line = super.reader.readLine()) != null) {
                scanner = new Scanner(line);
                scanner.useDelimiter(";");
                int i = 0;
                String id = "";
                while (scanner.hasNext()) {
                    String next = scanner.next();
                    if (i == 0) {
                        id = next;
                    }
                    if (i == 1) {
                        ingredients.add(new Ingredient(Integer.parseInt(id), next));
                    }
                    i++;
                }
                i = 0;
            }
        } catch (IOException ex) {
            Logger.getLogger(SCVReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ingredients;
    }
}
