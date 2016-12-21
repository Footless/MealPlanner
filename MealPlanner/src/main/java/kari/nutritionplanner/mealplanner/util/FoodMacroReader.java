/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.util;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import kari.nutritionplanner.mealplanner.domain.Ingredient;

/**
 *
 * @author kari
 */
public class FoodMacroReader extends SCVReader {

    public FoodMacroReader(String fileName) {
        super(fileName);
    }

    public void search(Ingredient ing) {
        String line = null;
        Scanner scanner = null;
        try {
            while ((line = super.reader.readLine()) != null) {
                scanner = new Scanner(line);
                scanner.useDelimiter(";");
                int i = 0;
                while (scanner.hasNext()) {
                    String next = scanner.next();
                    if (i == 0 && next.length() < 6 && Integer.parseInt(next) == ing.getId()) {
                        String macro = scanner.next();
                        if (macro.contains("ENERC")) {
                            ing.setCalories(Double.parseDouble(scanner.next().replace(',', '.')) / 4.184);
                        } else if (macro.contains("CHOAVL")) {
                            ing.setCarb(Double.parseDouble(scanner.next().replace(',', '.')));
                        } else if (macro.contentEquals("FAT")) {
                            ing.setFat(Double.parseDouble(scanner.next().replace(',', '.')));
                        } else if (macro.contains("PROT")) {
                            ing.setProtein(Double.parseDouble(scanner.next().replace(',', '.')));
                        } else if (macro.contains("FIBC")) {
                            ing.setFiber(Double.parseDouble(scanner.next().replace(',', '.')));
//                            return;
                        }
                    }
                    i++;
                }
                i = 0;
            }
        } catch (IOException ex) {
            Logger.getLogger(SCVReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
