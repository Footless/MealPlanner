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
package kari.nutritionplanner.mealplanner.servicelayer;

import java.io.IOException;
import java.util.List;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.util.CSVReader;
import kari.nutritionplanner.mealplanner.util.ProcessIngredients;

/**
 * Apuluokka raaka-aineiden etsimiseen.
 *
 * @author kari
 */
public class IngredientSearchHelper {

    private final CSVReader reader;
    private final ProcessIngredients ingredientProcessor;

    /**
     * Konstruktori luo Readerin, joka lukee tiedostoa food_utf.csv joka
     * sisältää kaikkien Finelin tietokannan elintarvikkeiden nimet ja id:t.
     *
     * @throws IOException heittää poikkeuksen jos ProcessIngredients heittää semmoisen
     */
    public IngredientSearchHelper() throws IOException {
        this.reader = new CSVReader("food_utf.csv");
        this.ingredientProcessor = new ProcessIngredients();
    }

    /**
     * Konstruktori antaa mahdollisuuden luoda readerin myös muille tiedostoille
     * tarpeen tullen. Tuskin tulee ja tämä poistetaan ennen pitkää.
     *
     * @param fileName halutun tiedoston nimi
     * @throws IOException heittää poikkeuksen jos ProcessIngredients heittää semmoisen
     */
    public IngredientSearchHelper(String fileName) throws IOException {
        this.reader = new CSVReader(fileName);
        this.ingredientProcessor = new ProcessIngredients();
    }

    /**
     * Hakee raaka-aineita readerin määrittelemästä tiedostosta annetun
     * hakutermin perusteella.
     *
     * @param s hakutermi, jonka käyttäjä on syöttänyt käyttöliittymässä
     * @return listan hakua vastaavista raaka-aineista
     * @throws IOException heittää poikkeuksen jos tiedoston luku epäonnistuu
     */
    public List<Ingredient> search(String s) throws IOException {
        return reader.search(s);
    }

    /**
     * Lisää makrot parametrina annettuun raaka-aineeseen.
     *
     * @param ing raaka-aine Ingredient-oliona
     * @return palauttaa true tai false, riippuen onnistuiko makrojen lisääminen
     * @throws IOException heittää poikkeuksen jos ProcessIngredients heittää semmoisen
     */
    public boolean addMacros(Ingredient ing) throws IOException {
        return ingredientProcessor.addMacrosToIngredient(ing);
    }
}
