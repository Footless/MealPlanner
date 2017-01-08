package kari.nutritionplanner.mealplanner.util;

import kari.nutritionplanner.mealplanner.domain.Ingredient;

/**
 * Apumetodeja makrojen laskuun tarjoava luokka.
 *
 * @author kari
 */
public class MacroCalculator {

    /**
     * Laskee paljonko annettua raaka-ainetta tarvitaan, jotta saadaan annettu
     * määrä proteiinia.
     *
     * @param protein haluttu proteiinin määrä
     * @param ing Ingredient-olio, raaka-aine jota käsitellään
     * @return tarvittava määrä raaka-ainetta liukulukuna, esim. 1.23 joka
     * tarkoittaa 123grammaa.
     */
    public double calculateAmountForProtein(double protein, Ingredient ing) {
        if (ing.getProtein() > 0) {
            return protein / ing.getProtein(); // kertaa sata jos haluaa grammoina
        }
        return 0;
    }

    /**
     * Laskee tarvittavan määrän raaka-ainetta, joka tarvitaan tuottamaan
     * haluttu määrä rasvaa raaka-aineesta.
     *
     * @param fat Haluttu määrä rasvaa.
     * @param ing Raaka-aine josta rasvan määrä lasketaan.
     * @return Tarvittavan määrän raaka-ainetta muodossa 1 = 100g.
     */
    public double calculateAmountForFat(double fat, Ingredient ing) {
        if (ing.getFat() > 0) {
            return fat / ing.getFat(); // kertaa sata jos haluaa grammoina
        }
        return 0;
    }

    /**
     * Laskee paljonko annettua raaka-ainetta pitää olla, jotta se tarjoaa
     * annetun määrän kaloreita.
     *
     * @param calories Haluttu määrä kaloreita
     * @param ing Ingredient-olio, raaka-aine jota käsitellään
     * @return tarvittava määrä raaka-ainetta liukulukuna, esim. 1.23 joka
     * tarkoittaa 123grammaa.
     */
    public double calculateAmountForCalories(double calories, Ingredient ing) {
        if (ing.getCalories() > 0) {
            return calories / ing.getCalories(); // kertaa sata jos haluaa grammoina
        }
        return 0;
    }
}
