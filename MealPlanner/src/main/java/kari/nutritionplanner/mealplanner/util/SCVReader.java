    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
public class SCVReader {

    private BufferedReader reader;

    public SCVReader(String fileName) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("file/" + fileName).getFile());
        try {
            this.reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException(fileName);
        }
    }

    public List<Ingredient> getAllIngredients() throws IOException {
        String line = null;
        Scanner scanner = null;
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null) {
                searchIngredient(ingredients, scanner, line);
            }
        } catch (IOException ex) {
            throw new IOException("joku meni vituiksi: " + ex);
        }
        return ingredients;
    }

    public boolean searchMacros(Ingredient ing) throws IOException {
        if (ing == null) {
            return false;
        }
        String line = null;
        Scanner scanner = null;
        try {
            while ((line = reader.readLine()) != null) {
                if (setMacros(scanner, line, ing)) {
                    return true;
                }
            }
        } catch (IOException ex) {
            throw new IOException("joku meni vituiksi: " + ex);
        }
        return false;
    }

//    public Ingredient search(String s) {
//        String line = null;
//        Scanner scanner = null;
//        Ingredient returnIng = null;
//        try {
//            while ((line = reader.readLine()) != null) {
//                returnIng = searchIngredient(scanner, s, line);
//                
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(SCVReader.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
    private void searchIngredient(List<Ingredient> ingredients, Scanner scanner, String line) {
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
                return;
            }
            i++;
        }
    }

    private boolean setMacros(Scanner scanner, String line, Ingredient ing) {
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
                    return true;
                }
            }
            i++;
        }
        return false;
    }

//    private Ingredient searchIngredient(Scanner scanner, String s, String line) {
//        scanner = new Scanner(line);
//                scanner.useDelimiter(";");
//                int i = 0;
//                while (scanner.hasNext()) {
//                    String id = "";
//                    String next = scanner.next();
//                    if (i == 0) {
//                        id = next;
//                    }
//                    if (i == 1 && next.toLowerCase().contains(s)) {
//                        return new Ingredient(Integer.parseInt(id), next);
//                    }
//                    i++;
//                }
//                i = 0;
//                return null;
//    }
}
