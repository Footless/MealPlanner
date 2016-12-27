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
    }

    public Map<String, Map<Integer, Ingredient>> getIngredients() {
        return ingredients;
    }

    public boolean setMainIngredient(int id, double proteinAmount, double fat) {
        Ingredient main = ingredients.get("mains").get(id);
        MacroCalculator mc = new MacroCalculator();
        double mainAmount = mc.calculateMainIngredientAmount(proteinAmount, main); // jaettuna sadalla?
        if (meal.getFat() <= fat) {
            meal.setMainIngredientAmount(mainAmount);
            meal.setMainIngredient(main);
            return true;
        } else {
            return false;
        }
    }

    public void setSauce(double fat) {
        double fatAmountInMeal = meal.getFat();
        if (fatAmountInMeal < fat) {
            Ingredient sauce = ingredients.get("sauces").get(5009);
            double sauceAmount = (fatAmountInMeal - fat) / sauce.getFat(); // joku jako kans?
            meal.setSauce(sauce);
            meal.setSauceAmount(sauceAmount);
        }
    }

    private void setMisc() {
        Ingredient misc = ingredients.get("sidesAndMisc").get(33182);
        meal.setMiscAmount(50); // jako?
        meal.setMisc(misc);
    }

    public void setSideIngredient(double calories) {
        if (calories < meal.getCalories()) {
            int seed = new Random().nextInt(ingredients.get("sides").size() - 1);
            int j = 0;
            for (Integer i : ingredients.get("sides").keySet()) {
                if (seed == j) {
                    meal.setSideIngredient(ingredients.get("sides").get(i));
                    return;
                }
                j++;
            }
            double sideIngredientAmount = (calories - meal.getCalories()) / meal.getSideIngredient().getCalories(); // jotain jakoja
            meal.setSideIngredientAmount(sideIngredientAmount);
        }
    }
}
