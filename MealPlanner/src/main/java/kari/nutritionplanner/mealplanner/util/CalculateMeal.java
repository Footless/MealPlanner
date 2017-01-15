package kari.nutritionplanner.mealplanner.util;

import java.util.Map;
import java.util.Random;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.domain.Meal;

/**
 * Ohjelman sydän ja aivot, joka muodostaa annetuista arvoista halutunlaisen aterian.
 *
 * @author kari
 */
public class CalculateMeal {

    private final Meal meal;
    private final MacroCalculator mc;
    private final ProcessIngredients ingredientProcessor;

    /**
     * Konstruktori. Saa parametrinä ProcessIngredients-olion jonka metodilla
     * luokka saa raaka-aineet käyttöönsä. Luo uuden tyhjän Meal-olion virheitä
     * ja nulleja estämään.
     *
     * @param ingredientProcessor ProcessIngredients, josta myös saadaan tieto
     * tietokannan käytettävyydestä.
     */
    public CalculateMeal(ProcessIngredients ingredientProcessor) {
        meal = new Meal();
        this.mc = new MacroCalculator();
        this.ingredientProcessor = ingredientProcessor;
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
        return ingredientProcessor.getIngredients();
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
        Ingredient main = ingredientProcessor.getIngredients().get("mains").get(id);
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

    private boolean setSideIngredient(int sideId) {
        if (sideId == 99999) {
            randomSide();
            return true;
        } else if (ingredientProcessor.getIngredients().get("sides").containsKey(sideId)) {
            meal.setSideIngredient(ingredientProcessor.getIngredients().get("sides").get(sideId));
            return true;
        }
        return false;
    }

    private void setSauce(double fat) {
        double fatAmountInMeal = meal.getFat();
        if (fatAmountInMeal < fat + 5) {
            int seed = new Random().nextInt(ingredientProcessor.getIngredients().get("sauces").size());
            int j = 0;
            for (Integer i : ingredientProcessor.getIngredients().get("sauces").keySet()) {
                if (seed == j) {
                    meal.setSauce(ingredientProcessor.getIngredients().get("sauces").get(i));
                    break;
                }
                j++;
            }
            meal.setSauceAmount(mc.calculateAmountForFat((fat - fatAmountInMeal), meal.getSauce()));
        }
    }

    private void setMisc() {
        int seed = new Random().nextInt(ingredientProcessor.getIngredients().get("sidesAndMisc").size());
        int j = 0;
        for (Integer i : ingredientProcessor.getIngredients().get("sidesAndMisc").keySet()) {
            if (seed == j) {
                meal.setMisc(ingredientProcessor.getIngredients().get("sidesAndMisc").get(i));
                break;
            }
            j++;
        }
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
        int seed = new Random().nextInt(ingredientProcessor.getIngredients().get("sides").size());
        int j = 0;
        for (Integer i : ingredientProcessor.getIngredients().get("sides").keySet()) {
            if (seed == j) {
                meal.setSideIngredient(ingredientProcessor.getIngredients().get("sides").get(i));
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