/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kari.nutritionplanner.mealplanner.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import kari.nutritionplanner.mealplanner.domain.Ingredient;

/**
 *
 * @author kari
 */
public abstract class SCVReader {

    protected BufferedReader reader;

    public SCVReader(String fileName) {
        String path = "assets/Fineli_Rel17_Fine74_open/";
        String fileToLoad = path + fileName;
        try {
            this.reader = new BufferedReader(new FileReader(fileToLoad));
            System.out.println("success");
        } catch (FileNotFoundException ex) {
            System.out.println("Tiedostoa ei löytynyt");
        }
    }
}
