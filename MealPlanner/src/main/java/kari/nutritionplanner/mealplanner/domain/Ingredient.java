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
public class Ingredient {

    private int id;
    private String name;
    private String category;

    public Ingredient(int id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
    }

    public Ingredient(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
