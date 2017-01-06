package kari.nutritionplanner.mealplanner.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import kari.nutritionplanner.mealplanner.domain.Ingredient;

/**
 * SCV-tiedostojen lukemiseen tarkoitettu luokka. Konstruktori saa parametrinä
 * tiedoston nimen.
 * @author kari
 */
public class SCVReader {

    private final BufferedReader reader;
    private final Locale loc;

    public SCVReader(String fileName) {
        InputStream in = getClass().getResourceAsStream("/file/" + fileName);
        reader = new BufferedReader(new InputStreamReader(in));
        loc = new Locale("fi", "FI");
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
            throw new IOException("Tiedoston lukeminen epäonnistui: " + ex);
        }
        return ingredients;
    }
    
    public List<Ingredient> search(String s) throws IOException {
        String line = null;
        Scanner scanner = null;
        List<Ingredient> ings = new ArrayList<>();
        try {
            while ((line = reader.readLine()) != null) {
                Ingredient ing = searchForIngredient(scanner, s, line);
                if (ing != null) {
                    ings.add(ing);
                }
            }
        } catch (IOException ex) {
            throw new IOException("Tiedoston lukeminen epäonnistui: " + ex);
        }
        return ings;
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
            throw new IOException("Tiedoston lukeminen epäonnistui: " + ex);
        }
        return false;
    }
    
    private void searchIngredient(List<Ingredient> ingredients, Scanner scanner, String line) {
        scanner = new Scanner(line);
        scanner.useLocale(loc);
        scanner.useDelimiter(";");
        int i = 0;
        String id = "";
        while (scanner.hasNext()) {
            String next = scanner.next();
            if (i == 0 && next.length() < 6) {
                id = next;
            }
            if (i == 1) {
                ingredients.add(new Ingredient(Integer.parseInt(id), next));
                return;
            }
            i++;
        }
    }
    
    private Ingredient searchForIngredient(Scanner scanner, String s, String line) {
        scanner = new Scanner(line);
                scanner.useDelimiter(";");
                int i = 0;
                String id = "";
                while (scanner.hasNext()) {
                    String next = scanner.next();
                    if (i == 0 && next.length() < 6 && next.length() > 0) {
                        id = next;
                        System.out.println(id);
                    }
                    if (i == 1 && next.toLowerCase().contains(s.toLowerCase()) && id.length() > 0) {
                        return new Ingredient(Integer.parseInt(id), next);
                    }
                    i++;
                }
                return null;
    }

    private boolean setMacros(Scanner scanner, String line, Ingredient ing) {
        scanner = new Scanner(line);
        scanner.useDelimiter(";");
        scanner.useLocale(loc);
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
}
