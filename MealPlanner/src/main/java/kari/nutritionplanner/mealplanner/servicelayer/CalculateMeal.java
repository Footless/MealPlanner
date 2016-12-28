/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.servicelayer;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;
import java.util.Map;
import java.util.Random;
import kari.nutritionplanner.mealplanner.domain.Ingredient;
import kari.nutritionplanner.mealplanner.domain.Meal;
import kari.nutritionplanner.mealplanner.util.MacroCalculator;

/**
 *
 * @author kari
 */
public class CalculateMeal {

    private Meal meal;
    private ProcessIngredients ingredientProcessor;
    private Map<String, Map<Integer, Ingredient>> ingredients;

    public CalculateMeal() {
        this.ingredientProcessor = new ProcessIngredients();
        this.ingredients = ingredientProcessor.getIngredients();
        meal = new Meal();
    }

    public Map<String, Map<Integer, Ingredient>> getIngredients() {
        return ingredients;
    }
    
    public boolean calculateAllMeal(int id, double calories, double protein, double fat) {
        if (setMainIngredient(id, protein, fat)) {
            setSauce(fat);
            setMisc();
            setSideIngredient(calories);
            evenUpIngredients(calories, protein, fat);
            roundUpIngredients();
            return true;
        }
        return false;
    }

    public boolean setMainIngredient(int id, double protein, double fat) {
        Ingredient main = ingredients.get("mains").get(id);
        MacroCalculator mc = new MacroCalculator();
        double mainAmount = mc.calculateMainIngredientAmount(protein, main); // jaettuna sadalla?
        if (mainAmount * main.getFat() <= fat) {
            meal.setMainIngredientAmount(mainAmount);
            meal.setMainIngredient(main);
            return true;
        } else {
            return false;
        }
    }

    public boolean setSauce(double fat) {
        double fatAmountInMeal = meal.getFat();
        if (fatAmountInMeal < fat) {
            Ingredient sauce = ingredients.get("sauces").get(5009);
            MacroCalculator mc = new MacroCalculator();
            double sauceAmount = mc.calculateSauceAmount((fat - fatAmountInMeal), sauce);
            meal.setSauce(sauce);
            meal.setSauceAmount(sauceAmount);
            return true;
        }
        return false;
    }

    public void setMisc() {
        Ingredient misc = ingredients.get("sidesAndMisc").get(33182);
        meal.setMiscAmount(0.5); // jako?
        meal.setMisc(misc);
    }

    public void setSideIngredient(double calories) {
        if (meal.getCalories() < calories) {
            double caloriesToAdd = calories - meal.getCalories();
            randomSide();
            MacroCalculator mc = new MacroCalculator();
            double sideIngredientAmount = mc.calculateSideAmount(caloriesToAdd, meal.getSideIngredient()); // jotain jakoja
            meal.setSideIngredientAmount(sideIngredientAmount);
        }
    }
    
    private void evenUpIngredients(double calories, double protein, double fat) {
        int i = 0;
        while (true) {            
            if (meal.getFat() > fat + 2 || meal.getFat() < fat - 2) {
                double toAddOrSub = (fat - meal.getFat()) / meal.getSauce().getFat();
                meal.setSauceAmount(meal.getSauceAmount() + toAddOrSub);
                toAddOrSub = (calories - meal.getCalories()) / meal.getSideIngredient().getCalories();
                meal.setSideIngredientAmount(meal.getSideIngredientAmount() + toAddOrSub);
            }
            if (meal.getProtein() > protein + 2 || meal.getProtein() < protein - 2) {
                double toAddOrSub = (protein - meal.getProtein()) / meal.getMainIngredient().getProtein();
                meal.setMainIngredientAmount(meal.getMainIngredientAmount() + toAddOrSub);
                toAddOrSub = (calories - meal.getCalories()) / meal.getSideIngredient().getCalories();
                meal.setSideIngredientAmount(meal.getSideIngredientAmount() + toAddOrSub);
            } 
            if ((meal.getFat() <= fat + 2 && meal.getFat() >= fat - 2) && (meal.getProtein() <= protein + 2 &&
                    meal.getProtein() >= protein - 2) && (meal.getCalories() <= calories + 25 && meal.getCalories() >= calories - 25)) {
                break;
            }
            if (i >= 10) {
                break;
            }
            i++;
        }
    }
    
    private void roundUpIngredients() {
        double jotain = Math.ceil(5.5);
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

    public Meal getMeal() {
        return meal;
    }

}
