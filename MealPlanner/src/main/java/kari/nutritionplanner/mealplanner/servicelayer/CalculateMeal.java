package kari.nutritionplanner.mealplanner.servicelayer;

import java.io.IOException;
import kari.nutritionplanner.mealplanner.util.ProcessIngredients;
import java.util.List;
import java.util.Map;
import java.util.Random;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.domain.Meal;
import kari.nutritionplanner.mealplanner.util.MacroCalculator;
import kari.nutritionplanner.mealplanner.util.MealTweaker;

/**
 * Käyttöliittymän ja logiikkaluokkien väliin sijoittuva luokka. Tarkoitettu
 * halutun aterian muodostamiseen ja tarjoaa apumetodeja sen muodostamiseen.
 *
 * @author kari
 */
public class CalculateMeal {

    private final Meal meal;
    private final ProcessIngredients ingredientProcessor;
    private final Map<String, Map<Integer, Ingredient>> ingredients;
    private final MacroCalculator mc = new MacroCalculator();

    /**
     * Konstruktori, ei mitään ihmeellistä. Voi heittää poikkeuksen.
     *
     * @throws IOException
     */
    public CalculateMeal() throws IOException {
        this.ingredientProcessor = new ProcessIngredients();
        this.ingredients = ingredientProcessor.getIngredients();
        meal = new Meal();
    }

    /**
     * Palauttaa Map-olion, jossa sisällä eri Mapeissa kaikki raaka-aineet.
     * Raaka-aineet koodattu merkkijonoilla seuraavasti: "mains" = pääraaka-aine
     * "sauces" = kastike "sidesAndMisc" = lisuke, esim. lämmin lisäke tai
     * salaatti "sides" = varsinainen lisäke, esim. peruna tai pasta
     *
     * @return Map, jossa jossa sisällä neljä eri Map-oliota, joissa kaikki
     * raaka-aineet aterian rakennusta varten.
     */
    public Map<String, Map<Integer, Ingredient>> getIngredients() {
        return ingredients;
    }

    /**
     * Luokan päämetodi, joka annettujen makrojen perusteella muodostaa aterian.
     * Jos annetut arvot ovat sallittujen rajojen sisällä ja realistisia (esim.
     * rasvaa ateriassa ei voi olla vähemmän kuin pääraaka-aineesta tulee
     * ateriaan) tekee aterian ja palauttaa true.
     *
     * @param mainId pääraaka-aineen id-numero
     * @param sideId lisäkkeen id-numero
     * @param calories halutut kalorit
     * @param protein haluttu proteiinin määrä grammoina
     * @param fat haluttu rasvan määrä grammoina
     * @return onnistuiko aterian luonti annetuilla arvoilla
     * @see Ingredient
     */
    public boolean calculateAllMeal(int mainId, int sideId, double calories, double protein, double fat) {
        
        if (protein < 10 || fat < 5 || calories < 50) {
            return false;
        }
        if (!setSideIngredient(sideId)) {
            return false;
        }
        if (setMainIngredient(mainId, protein, fat)) {
            setMisc();
            setSauce(fat);
            setSideIngredientAmount(calories);
            MealTweaker mw = new MealTweaker(meal, calories, protein, fat);
            mw.tweakMeal();
            return true;
        }
        return false;
    }

    private boolean setMainIngredient(int id, double protein, double fat) {
        Ingredient main = ingredients.get("mains").get(id);
        double proteinToAdd = protein - meal.getProtein();
        double mainAmount = mc.calculateAmountForProtein(proteinToAdd, main);
        if (mainAmount < 0.5) {
            mainAmount = 0.5;
        }
        if (mainAmount * main.getFat() <= fat) {
            meal.setMainIngredientAmount(mainAmount);
            meal.setMainIngredient(main);
            return true;
        } else {
            return false;
        }
    }

    public boolean setSideIngredient(int sideId) {
        if (sideId == 99999) {
            randomSide();
            return true;
        } else if (ingredients.get("sides").containsKey(sideId)) {
            meal.setSideIngredient(ingredients.get("sides").get(sideId));
            return true;
        }
        return false;
    }

    private void setSauce(double fat) {
        double fatAmountInMeal = meal.getFat();
        if (fatAmountInMeal < fat) {
            Ingredient sauce = ingredients.get("sauces").get(5009);
            meal.setSauce(sauce);
            meal.setSauceAmount(mc.calculateAmountForFat((fat - fatAmountInMeal), sauce));
        }
    }

    private void setMisc() {
        Ingredient misc = ingredients.get("sidesAndMisc").get(33182);
        meal.setMisc(misc);
        if (meal.getSideIngredient().getProtein() >= 3 || meal.getMainIngredient().getProtein() >= 20) {
            meal.setMiscAmount(1);
        } else {
            meal.setMiscAmount(0.5);
        }
    }

    private void setSideIngredientAmount(double calories) {
        if (meal.getCalories() < calories) {
            double caloriesToAdd = calories - meal.getCalories();
            double sideIngredientAmount = mc.calculateAmountForCalories(caloriesToAdd, meal.getSideIngredient());
            meal.setSideIngredientAmount(sideIngredientAmount);
        }
    }

    private void randomSide() {
        int seed = new Random().nextInt(ingredients.get("sides").size() - 1);
        int j = 0;
        for (Integer i : ingredients.get("sides").keySet()) {
            if (seed == j) {
                meal.setSideIngredient(ingredients.get("sides").get(i));
                break;
            }
            j++;
        }
    }

    /**
     * Palauttaa aterian Meal-oliona.
     *
     * @return Meal-olio
     * @see Meal
     */
    public Meal getMeal() {
        return meal;
    }
}
