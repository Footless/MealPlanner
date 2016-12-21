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

/**
 *
 * @author kari
 */
public class FoodNameReader extends SCVReader {

    public FoodNameReader(String fileName) {
        super(fileName);
    }

    public void search(String s) {
        String line = null;
        Scanner scanner = null;
        try {
            while ((line = super.reader.readLine()) != null) {
                scanner = new Scanner(line);
                scanner.useDelimiter(";");
                int i = 0;
                while (scanner.hasNext()) {
                    String next = scanner.next();
                    if (i == 1 && next.contains(s)) {
                        System.out.println(next);
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
