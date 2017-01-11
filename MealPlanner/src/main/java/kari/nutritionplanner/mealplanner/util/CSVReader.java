package kari.nutritionplanner.mealplanner.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import javax.swing.JOptionPane;
import kari.nutritionplanner.mealplanner.domain.Ingredient;

/**
 * SCV-tiedostojen lukemiseen tarkoitettu luokka. Konstruktori saa parametrinä
 * tiedoston nimen.
 *
 * @author kari
 */
public class CSVReader {

    private final BufferedReader reader;
    private final Locale loc;
    private final String fileName;

    /**
     * Konstruktori, joka saa parametrinä tiedoston nimen. Luo sen perusteella
     * BufferedReaderin.
     *
     * @param fileName tiedoston nimi
     */
    public CSVReader(String fileName) {
        this.fileName = fileName;
        InputStream in = getClass().getResourceAsStream("/file/" + fileName);
        if (in != null) {
            reader = new BufferedReader(new InputStreamReader(in));
        } else {
            reader = null;
            JOptionPane.showMessageDialog(null, "Haluttua tiedostoa " + fileName + " ei löytynyt", "Virhe", 0);
        }
        loc = new Locale("fi", "FI");
    }

    /**
     * Sulkee lukijan.
     */
    public void closeReader() {
        try {
            this.reader.close();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Virhe lukijaa suljettaessa: " + ex, "Virhe", 0);
        }
    }

    /**
     * Palauttaa listan kaikista raaka-aineista, jotka konstruktorin
     * määrittelemästä tiedostosta löytyvät.
     *
     * @return List-olio, jossa kaikki kohdetiedoston raaka-aineet
     * @throws java.io.IOException Saattaa heittää poikkeuksen tiedoston luvun
     * epäonnistuessa.
     */
    public List<Ingredient> getAllIngredients() throws IOException {
        String line = null;
        Scanner scanner = null;
        List<Ingredient> ingredients = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            addIngredient(ingredients, scanner, line);
        }
        return ingredients;
    }

    /**
     * Hakee raaka-aineita konstruktorissa määritetystä tiedostosta annetun
     * hakutermin perusteella. Palauttaa listan hakutermiin täsmäävistä
     * raaka-aineista.
     *
     * @param s hakutermi
     * @return lista kaikista raaka-aineista, joihin hakutermi täsmää
     * @throws java.io.IOException Saattaa heittää poikkeuksen tiedoston luvun
     * epäonnistuessa.
     */
    public List<Ingredient> search(String s) throws IOException {
        String line = null;
        Scanner scanner = null;
        List<Ingredient> ings = new ArrayList<>();

        while ((line = reader.readLine()) != null) {
            Ingredient ing = searchForIngredientByName(scanner, s, line);
            if (ing != null) {
                ings.add(ing);
            }
        }
        return ings;
    }

    /**
     * Lisää kaikkiin listan raaka-aineisiin makrot.
     *
     * @param allIngs Lista raaka-aineista
     * @throws IOException Saattaa heittää poikkeuksen tiedoston luvun
     * epäonnistuessa.
     */
    public void searchAllMacros(List<Ingredient> allIngs) throws IOException {
        Collections.sort(allIngs, (i1, i2) -> i1.getId() - i2.getId());
        ArrayList<String> macros = new ArrayList<>();
        InputStream in = getClass().getResourceAsStream("/file/" + fileName);
        BufferedReader testReader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while ((line = testReader.readLine()) != null) {
            macros.add(line);
        }
        testReader.close();
        int start = 0;
        for (Ingredient ing : allIngs) {
            for (int i = start; i < macros.size(); i++) {
                if (setMacros(macros.get(i), ing)) {
                    start = i + 35;
                    break;
                }
            }
        }
    }

    /**
     * Etsii makroja konstruktorin määrittelemästä tiedostosta. Palauttaa true
     * tai false sen mukaan, onnistuiko makrojen lisääminen raaka-aineeseen, eli
     * löytyikö raaka-ainetta tiedostosta.
     *
     * @param ing Ingredient-olio, jolle makrot halutaan lisätä
     * @return true tai false onnistumisen mukaan
     * @throws java.io.IOException Saattaa heittää poikkeuksen tiedoston luvun
     * epäonnistuessa.
     */
    public boolean searchMacros(Ingredient ing) throws IOException {
        InputStream in = getClass().getResourceAsStream("/file/" + fileName);
        BufferedReader testReader = new BufferedReader(new InputStreamReader(in));
        if (ing == null) {
            return false;
        }
        String line = null;

        while ((line = testReader.readLine()) != null) {
            if (setMacros(line, ing)) {
                return true;
            }
        }
        return false;
    }

    private void addIngredient(List<Ingredient> ingredients, Scanner scanner, String line) {
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
                ingredients.add(new Ingredient(Integer.parseInt(id), parseString(next).toLowerCase()));
                return;
            }
            i++;
        }
    }

    private Ingredient searchForIngredientByName(Scanner scanner, String s, String line) {
        scanner = new Scanner(line);
        scanner.useDelimiter(";");
        int i = 0;
        String id = "";
        while (scanner.hasNext()) {
            String next = scanner.next();
            if (i == 0 && next.length() < 6 && next.length() > 0) {
                id = next;
            }
            if (i == 1 && next.toLowerCase().contains(s.toLowerCase()) && id.length() > 0) {
                return new Ingredient(Integer.parseInt(id), next.toLowerCase());
            }
            i++;
        }
        return null;
    }

    private boolean setMacros(String line, Ingredient ing) {
        Scanner scanner = new Scanner(line);
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

    private String parseString(String s) {
        return s.replace('\'', '´');
    }

}
