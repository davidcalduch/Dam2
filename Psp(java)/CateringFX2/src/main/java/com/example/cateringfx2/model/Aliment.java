package com.example.cateringfx2.model;

import java.io.Serializable;

public class Aliment implements MenuElement, Serializable {
    private String name;
    private String description;
    private String frequency;
    private double calories;
    private double fat;
    private double carbohydrates;
    private boolean nuts;
    private boolean milk;
    private boolean eggs;
    private boolean gluten;

    /**
     * Constructor of Aliment
     * @param name
     * @param description
     * @param frequency
     * @param calories
     * @param fat
     * @param carbohydrates
     * @param nuts
     * @param milk
     * @param eggs
     * @param gluten
     */
    public Aliment(String name, String description, String frequency, double calories, double fat, double carbohydrates, boolean nuts, boolean milk, boolean eggs, boolean gluten) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.calories = calories;
        this.fat = fat;
        this.carbohydrates = carbohydrates;
        this.nuts = nuts;
        this.milk = milk;
        this.eggs = eggs;
        this.gluten = gluten;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getFrequency() {
        return frequency;
    }
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    public double getCalories() {
        return calories;
    }
    public void setCalories(double calories) {
        this.calories = calories;
    }
    public double getFat() {
        return fat;
    }
    public void setFat(double fat) {
        this.fat = fat;
    }
    public double getCarbohydrates() {
        return carbohydrates;
    }
    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }
    public boolean isNuts() {
        return nuts;
    }
    public void setNuts(boolean nuts) {
        this.nuts = nuts;
    }
    public boolean isMilk() {
        return milk;
    }
    public void setMilk(boolean milk) {
        this.milk = milk;
    }
    public boolean isEggs() {
        return eggs;
    }
    public void setEggs(boolean eggs) {
        this.eggs = eggs;
    }
    public boolean isGluten() {
        return gluten;
    }
    public void setGluten(boolean gluten) {
        this.gluten = gluten;
    }
    public boolean hasNuts() {
        return nuts;
    }
    public boolean hasMilk() {
        return milk;
    }
    public boolean hasEggs() {
        return eggs;
    }
    public boolean hasGluten() {
        return gluten;
    }
    public String toString() {
        return String.format("%s;%s;%s;%b;%b;%b;%b;%.1f;%.1f;%.1f", name, description, frequency, gluten, milk, nuts, eggs, calories, carbohydrates, fat);
    }

}
