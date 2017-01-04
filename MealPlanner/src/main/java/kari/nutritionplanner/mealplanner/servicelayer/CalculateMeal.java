package kari.nutritionplanner.mealplanner.servicelayer;

import java.io.IOException;
import kari.nutritionplanner.mealplanner.util.ProcessIngredients;
import java.util.List;
import java.util.Map;
import java.util.Random;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.domain.Meal;
import kari.nutritionplanner.mealplanner.util.MacroCalculator;

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
    
    public CalculateMeal() throws IOException {
        this.ingredientProcessor = new ProcessIngredients();
        this.ingredients = ingredientProcessor.getIngredients();
        meal = new Meal();
    }
    
    /**
     * Palauttaa Map-olion, jossa sisällä eri Mapeissa kaikki raaka-aineet.
     * Raaka-aineet koodattu int-arvoilla seuraavasti:
     * 1 = pääraaka-aine
     * 2 = kastike
     * 3 = lisuke, esim. lämmin lisäke tai salaatti
     * 4 = varsinainen lisäke, esim. peruna tai pasta
     * 
     * @return Map, jossa jossa sisällä neljä eri Map-oliota, joissa kaikki raaka-aineet
     * aterian rakennusta varten.
     */
    
    public Map<String, Map<Integer, Ingredient>> getIngredients() {
        return ingredients;
    }
    
    /**
     * Luokan päämetodi, joka annettujen makrojen perusteella muodostaa aterian. Jos annetut arvot
     * ovat sallittujen rajojen sisällä ja realistisia (esim. rasvaa ateriassa ei voi olla vähemmän
     * kuin pääraaka-aineesta tulee ateriaan) tekee aterian ja palauttaa true.
     * 
     * @param id pääraaka-aineen id-numero
     * @param calories halutut kalorit
     * @param protein haluttu proteiinin määrä grammoina
     * @param fat haluttu rasvan määrä grammoina
     * @return onnistuiko aterian luonti annetuilla arvoilla
     * @see Ingredient
     */
    
    public boolean calculateAllMeal(int id, double calories, double protein, double fat) {
        randomSide();
        setMisc();
        if (setMainIngredient(id, protein, fat, calories)) {
            setSauce(fat);
            setSideIngredientAmount(calories);
            evenUpIngredients(calories, protein, fat);
            roundUpIngredients();
//            System.out.println(meal.toString());
//            System.out.println("kalorit: " + calories + " protskut: " + protein + " rasva: " + fat);
            return true;
        }
        return false;
    }
    
    private boolean setMainIngredient(int id, double protein, double fat, double calories) {
        Ingredient main = ingredients.get("mains").get(id);
        if (protein < 10 || fat < 5 || calories < 50) {
            return false;
        }
        double proteinToAdd = protein - meal.getProtein();
        MacroCalculator mc = new MacroCalculator();
        double mainAmount = mc.calculateAmountForProtein(proteinToAdd, main);
        if (mainAmount < 0.5) {
            mainAmount = 0.5;
        }
        if (mainAmount * main.getFat() <= fat && mainAmount > 0) {
            meal.setMainIngredientAmount(mainAmount);
            meal.setMainIngredient(main);
            return true;
        } else {
            return false;
        }
    }
    
    private void setSauce(double fat) {
        double fatAmountInMeal = meal.getFat();
        if (fatAmountInMeal < fat) {
            Ingredient sauce = ingredients.get("sauces").get(5009);
            MacroCalculator mc = new MacroCalculator();
            double sauceAmount = mc.calculateAmountForFat((fat - fatAmountInMeal), sauce);
            meal.setSauce(sauce);
            meal.setSauceAmount(sauceAmount);
        }
    }
    
    private void setMisc() {
        Ingredient misc = ingredients.get("sidesAndMisc").get(33182);
        meal.setMisc(misc);
        if (meal.getSideIngredient().getProtein() >= 5) {
            meal.setMiscAmount(1);
        } else {
            meal.setMiscAmount(0.5);
        }
    }
    
    private void setSideIngredientAmount(double calories) {
        if (meal.getCalories() < calories) {
            double caloriesToAdd = calories - meal.getCalories();
            MacroCalculator mc = new MacroCalculator();
            double sideIngredientAmount = mc.calculateAmountForCalories(caloriesToAdd, meal.getSideIngredient());
            meal.setSideIngredientAmount(sideIngredientAmount);
        }
    }
    
    private void proteinSubber(double protein) {
        if (protein < meal.getProtein()) {
            double proteinToSub = protein - meal.getProtein();
            if (proteinToSub < 0) {
                subFromMain(proteinToSub);
            }
        }
    }
    
    private void subFromMain(double proteinToSub) {
        MacroCalculator mc = new MacroCalculator();
        double mainAmount = mc.calculateAmountForProtein(proteinToSub, meal.getMainIngredient());
        if (meal.getMainIngredientAmount() + mainAmount > 0.8) {
            meal.setMainIngredientAmount(meal.getMainIngredientAmount() + mainAmount);
        } else if (meal.getMainIngredient().getProtein() > 23) {
            meal.setMainIngredientAmount(0.5);
        } else {
            meal.setMainIngredientAmount(0.8);
        }
    }
    
    private void caloriesAdder(double calories) {
        if (calories > meal.getCalories()) {
            MacroCalculator mc = new MacroCalculator();
            double caloriesToAdd = calories - meal.getCalories();
            double caloriesToAddSide = (caloriesToAdd / 8) * 6;
            double caloriesToAddMain = (caloriesToAdd / 8) * 2;
            if (caloriesToAdd > 0) {
                meal.setSideIngredientAmount(meal.getSideIngredientAmount() + mc.calculateAmountForCalories(caloriesToAddSide, meal.getSideIngredient()));
                meal.setMainIngredientAmount(meal.getMainIngredientAmount() + mc.calculateAmountForCalories(caloriesToAddMain, meal.getMainIngredient()));
            }
            
        }
    }
    
    private void fatSubber(double fat) {
        if (fat < meal.getFat() && meal.getSauceAmount() > 0) {
            MacroCalculator mc = new MacroCalculator();
            double fatToSub = fat - meal.getFat();
            meal.setSauceAmount(meal.getSauceAmount() + mc.calculateAmountForFat(fatToSub, meal.getSauce()));
            if (meal.getSauceAmount() < 0.1) {
                meal.setSauceAmount(0);
            }
        }
    }
    
    private void evenUpIngredients(double calories, double protein, double fat) {
        for (int i = 0; i < 10; i++) {
            if (allOk(calories, protein, fat)) {
                break;
            }
            proteinSubber(protein);
            fatSubber(fat);
            caloriesAdder(calories);
        }
    }
    
    private boolean allOk(double calories, double protein, double fat) {
        if ((meal.getCalories() <= calories + 10 && meal.getCalories() >= calories - 10)
                && (meal.getProtein() <= protein + 2 && meal.getProtein() >= protein - 2)
                && (meal.getFat() <= fat + 2 && meal.getFat() >= fat - 2)) {
            return true;
        }
        return false;
    }
    
    private void roundUpIngredients() {
        double main = Math.ceil(meal.getMainIngredientAmount() * 100) / 100;
        double side = Math.ceil(meal.getSideIngredientAmount() * 100) / 100;
        double sauce = Math.ceil(meal.getSauceAmount() * 100) / 100;
        meal.setMainIngredientAmount(main);
        meal.setSideIngredientAmount(side);
        meal.setSauceAmount(sauce);
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
    
    /**
     * Käyttöliittymälle tarjottu metodi, joka palauttaa pääraaka-aineen
     * id-numeron nimen perusteella.
     * 
     * @param name haettavan raaka-aineen nimi
     * @return haetun raaka-aineen id
     * @see Ingredient
     */
    
    public int getMainIngId(String name) {
        Map<Integer, Ingredient> mains = ingredients.get("mains");
        for (Integer i : mains.keySet()) {
            if (mains.get(i).getName().toLowerCase().contains(name.toLowerCase())) {
                return i;
            }
        }
        return 0;
    }
    
    /**
     * Käyttöliittymälle tarjottu metodi, joka palauttaa listan kaikista valittavissa
     * olevista pääraaka-aineista.
     * 
     * @return listan pääraaka-aineista, jotka ovat käyttäjän valittavissa.
     * @throws IOException 
     * @see Ingredient
     */
    public List<Ingredient> getMainIngredients() throws IOException {
        return ingredientProcessor.getMainIngredients();
    }
    
}
