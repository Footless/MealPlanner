/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.domain;

/**
 * Raaka-ainetta kuvaava luokka. Sisältää raaka-aineen nimen ja id-numeron, sekä
 * raaka-aineen makrot.
 *
 * @author kari
 */
public class Ingredient {

    private final int id;
    private final String name;
    private double calories;
    private double protein;
    private double fat;
    private double carb;
    private double fiber;

    /**
     * Luo raaka-aineen asettamalla sille id-numeron ja nimen. Makrojen arvot
     * alustetaan nollaan.
     *
     * @param id raaka-aineen id-numero kokonaislukuna
     * @param name raaka-aineen nimi
     */
    public Ingredient(int id, String name) {
        this.id = id;
        this.name = name;
        this.calories = 0;
        this.carb = 0;
        this.fat = 0;
        this.fiber = 0;
        this.protein = 0;
    }

    public int getId() {
        return id;
    }

    /**
     * Palauttaa raaka-aineen nimen. Nimeen vaihdetaan iso alkukirjain, koska
     * tietokannassa kaikki raaka-aineet ovat pienillä kirjaimilla.
     *
     * @return Raaka-aineen nimi.
     */
    public String getName() {
        return makeFirstLetterCapital(name);
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public void setCarb(double carb) {
        this.carb = carb;
    }

    public void setFiber(double fiber) {
        this.fiber = fiber;
    }

    public double getCalories() {
        return calories;
    }

    public double getProtein() {
        return protein;
    }

    public double getFat() {
        return fat;
    }

    public double getCarb() {
        return carb;
    }

    public double getFiber() {
        return fiber;
    }

    @Override
    public String toString() {
        return "" + makeFirstLetterCapital(name);
    }

    private String makeFirstLetterCapital(String s) {
        String ret = s.replaceFirst(s.substring(0, 1), s.substring(0, 1).toUpperCase());
        return ret;
    }
}
