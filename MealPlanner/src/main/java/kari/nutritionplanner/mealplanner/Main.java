/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner;

import kari.nutritionplanner.mealplanner.util.FoodNameReader;

/**
 *
 * @author kari
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FoodNameReader scvr = new FoodNameReader("food_utf.csv");
        scvr.search("nauta");
    }

}
