package com.example.cateringfx2.utils;
import com.example.cateringfx2.model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class FileUtils {
    public static List<MenuElement> loadElements() {
        List<MenuElement> elements = new ArrayList<>();
        String alimentFilePath = "aliments.txt";
        String dishesFilePath = "dishes.txt";

        System.out.println("Ruta absoluta de aliments.txt: " + alimentFilePath);
        System.out.println("Ruta absoluta de dishes.txt: " + dishesFilePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(alimentFilePath))) {
            String line = reader.readLine();
            while (line != null) {
                String[] data = line.split(";");
                if (data.length >= 10) {
                    String name = data[0];
                    String description = data[1];
                    String frequency = data[2];
                    boolean nuts = Boolean.parseBoolean(data[3]);
                    boolean milk = Boolean.parseBoolean(data[4]);
                    boolean eggs = Boolean.parseBoolean(data[5]);
                    boolean gluten = Boolean.parseBoolean(data[6]);
                    double calories = Double.parseDouble(data[7]);
                    double fat = Double.parseDouble(data[8]);
                    double carbohydrates = Double.parseDouble(data[9]);

                    Aliment a = new Aliment(name, description, frequency, calories, fat, carbohydrates, nuts, milk, eggs, gluten);
                    elements.add(a);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(dishesFilePath))) {
            String line = reader.readLine();
            while (line != null) {

                String[] data = line.split(";");
                if (data.length >= 2) {
                    String name = data[0];
                    String description = data[1];
                    List<Ingredient> ingredients = new ArrayList<>();

                    for (int i = 2; i < data.length; i++) {
                        String[] ingredientData = data[i].split(":");
                        if (ingredientData.length == 2) {
                            String ingredientName = ingredientData[0];
                            double quantity = Double.parseDouble(ingredientData[1]);
                            Ingredient ingredient = new Ingredient(quantity, new Aliment(ingredientName, "", "", 0, 0, 0, false, false, false, false));
                            ingredients.add(ingredient);
                        }
                    }
                    Dish d = new Dish(name, description, ingredients);
                    elements.add(d);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return elements;
    }
    public static void storeAliment(Aliment a, String fileName) {
        // Implementación para guardar un nuevo Aliment en el archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            String alimentData = a.getName() + ";" + a.getDescription() + ";" + a.getFrequency() + ";" + a.hasNuts() + ";" +
                    a.hasMilk() + ";" + a.hasEggs() + ";" + a.hasGluten()+ ";"+a.getCalories() + ";" + a.getFat() + ";" + a.getCarbohydrates();
            writer.write(alimentData);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void storeDish(Dish d, String fileName) {
        // Implementación para guardar un nuevo Dish en el archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            String dishData = d.getName() + "," + d.getDescription();
            for (Ingredient ingredient : d.getIngredients()) {
                dishData += ";" + ingredient.getAliment().getName() + ":" + ingredient.getQuantity();
            }
            writer.write(dishData);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void storeMenu(Menu m, String fileName) {
        // Implementación para guardar un menú en el archivo con el nombre basado en la fecha del menú
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(m);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
