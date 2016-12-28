/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.domain;

/**
 *
 * @author kari
 */
public class Meal {

    private Ingredient mainIngredient;
    private Ingredient sideIngredient;
    private Ingredient sauce;
    private Ingredient misc;
    private double mainIngredientAmount;
    private double sideIngredientAmount;
    private double sauceAmount;
    private double miscAmount;

    public void setMainIngredient(Ingredient mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public void setSideIngredient(Ingredient sideIngredient) {
        this.sideIngredient = sideIngredient;
    }

    public void setSauce(Ingredient sauce) {
        this.sauce = sauce;
    }

    public void setMisc(Ingredient misc) {
        this.misc = misc;
    }

    public void setMainIngredientAmount(double mainIngredientAmount) {
        this.mainIngredientAmount = mainIngredientAmount;
    }

    public void setSideIngredientAmount(double sideIngredientAmount) {
        this.sideIngredientAmount = sideIngredientAmount;
    }

    public void setSauceAmount(double sauceAmount) {
        this.sauceAmount = sauceAmount;
    }

    public void setMiscAmount(double miscAmount) {
        this.miscAmount = miscAmount;
    }

    public double getProtein() {
        double protein = 0;
        if (mainIngredient != null) {
            protein += mainIngredientAmount * mainIngredient.getProtein();
        }
        if (sauce != null) {
            protein += sauceAmount * sauce.getProtein();
        }
        if (sideIngredient != null) {
            protein += sideIngredientAmount * sideIngredient.getProtein();
        }
        if (misc != null) {
            protein += miscAmount * misc.getProtein();
        }
        return protein;
    }

    public double getFat() {
        double fat = 0;
        if (mainIngredient != null) {
            fat += mainIngredientAmount * mainIngredient.getFat();
        }
        if (sauce != null) {
            fat += sauceAmount * sauce.getFat();
        }
        if (sideIngredient != null) {
            fat += sideIngredientAmount * sideIngredient.getFat();
        }
        if (misc != null) {
            fat += miscAmount * misc.getFat();
        }
        return fat;
    }

    public double getCalories() {
        double calories = 0;
        if (mainIngredient != null) {
            calories += mainIngredientAmount * mainIngredient.getCalories();
        }
        if (sauce != null) {
            calories += sauceAmount * sauce.getCalories();
        }
        if (sideIngredient != null) {
            calories += sideIngredientAmount * sideIngredient.getCalories();
        }
        if (misc != null) {
            calories += miscAmount * misc.getCalories();
        }
        return calories;
    }

    public double getCarbs() {
        double carbs = 0;
        if (mainIngredient != null) {
            carbs += mainIngredientAmount * mainIngredient.getCarb();
        }
        if (sauce != null) {
            carbs += sauceAmount * sauce.getCarb();
        }
        if (sideIngredient != null) {
            carbs += sideIngredientAmount * sideIngredient.getCarb();
        }
        if (misc != null) {
            carbs += miscAmount * misc.getCarb();
        }
        return carbs;
    }

    public double getFiber() {
        double fibers = 0;
        if (mainIngredient != null) {
            fibers += mainIngredientAmount * mainIngredient.getFiber();
        }
        if (sauce != null) {
            fibers += sauceAmount * sauce.getFiber();
        }
        if (sideIngredient != null) {
            fibers += sideIngredientAmount * sideIngredient.getFiber();
        }
        if (misc != null) {
            fibers += miscAmount * misc.getFiber();
        }
        return fibers;
    }

    public Ingredient getSideIngredient() {
        return sideIngredient;
    }

    public double getMainIngredientAmount() {   //kerrotaan sadalla tulostusvaiheessa
        return mainIngredientAmount;
    }

    public double getSauceAmount() {   //kerrotaan sadalla tulostusvaiheessa
        return sauceAmount;
    }

    public Ingredient getSauce() {
        return sauce;
    }

    public Ingredient getMainIngredient() {
        return mainIngredient;
    }

    public double getMiscAmount() {   //kerrotaan sadalla tulostusvaiheessa
        return miscAmount;
    }

    public double getSideIngredientAmount() {   //kerrotaan sadalla tulostusvaiheessa
        return sideIngredientAmount;
    }

    @Override
    public String toString() {
        return "Ainekset:\n"
                + mainIngredient + ": " + getMainIngredientAmount() * 100 + "gr\n"
                + sideIngredient + ": " + getSideIngredientAmount() * 100 + "gr\n"
                + sauce + ": " + getSauceAmount() * 100 + "gr\n"
                + misc + ": " + getMiscAmount() * 100 + "gr\n\n"
                + "Ravintoarvot: \n"
                + "Kilokalorit: " + getCalories() + "kcal\n"
                + "Proteiini: " + getProtein() + "gr\n"
                + "Rasva: " + getFat() + "gr\n"
                + "Hiilihydraatit: " + getCarbs() + "gr\n"
                + "Kuidut: " + getFiber() + "gr\n";
    }

}
