package kari.nutritionplanner.mealplanner.logic;

import java.util.Map;
import java.util.Random;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.domain.Meal;
import kari.nutritionplanner.mealplanner.util.MacroCalculator;
import kari.nutritionplanner.mealplanner.util.ProcessIngredients;

/**
 * Ohjelman sydän ja aivot, joka muodostaa annetuista arvoista halutunlaisen
 * aterian.
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
            setRandomSauce();
            meal.setSauceAmount(mc.calculateAmountForFat((fat - fatAmountInMeal), meal.getSauce()));
        }
    }

    private void setRandomSauce() {
        setRandomIng(3);
    }
    
    private void setRandomMisc() {
        setRandomIng(4);
    }
    
    private void randomSide() {
        setRandomIng(2);
    }
    
    private void setRandomIng(int select) {
        String category = setCategory(select);
        int seed = new Random().nextInt(ingredientProcessor.getIngredients().get(category).size());
        int j = 0;
        for (Integer i : ingredientProcessor.getIngredients().get(category).keySet()) {
            if (seed == j) {
                Ingredient ing = ingredientProcessor.getIngredients().get(category).get(i);
                setRandomIng(ing, select);
                break;
            }
            j++;
        }
    }
    
    private void setRandomIng(Ingredient ing, int select) {
        switch (select) {
            case 2:
                meal.setSideIngredient(ing);
                break;
            case 3:
                meal.setSauce(ing);
                break;
            case 4:
                meal.setMisc(ing);
                break;
            default:
                break;
        }
    }
    
    private String setCategory(int select) {
        String category = "";
        switch (select) {
            case 2:
                category += "sides";
                break;
            case 3:
                category += "sauces";
                break;
            case 4:
                category = "sidesAndMisc";
                break;
            default:
                break;
        }
        return category;
    }

    private void setMisc() {
        setRandomMisc();
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
