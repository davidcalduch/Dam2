package com.example.cateringfx2.model;

import java.io.Serializable;
import java.util.List;

public class Dish implements MenuElement, Serializable {
    private String name;
    private String description;
    private List<Ingredient> ingredients;
    private boolean nuts;
    private boolean milk;
    private boolean eggs;
    private boolean gluten;
    /**
     * Constructor of Dish
     */
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

    public List<Ingredient> getIngredients() {
        return ingredients;
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
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    public Dish(String name, String description, List<Ingredient> ingredients) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
    }

    @Override
    public double getCalories() {
        double totalCalories = 0;
        for (Ingredient ingredient : ingredients) {
            totalCalories += ingredient. getCalories();
        }
        return totalCalories;
    }
    @Override
    public double getCarbohydrates() {
        double totalCarbohydrates = 0;
        for (Ingredient ingredient : ingredients) {
            totalCarbohydrates += ingredient.getCarbohydrates();
        }
        return totalCarbohydrates;
    }

    @Override
    public double getFat() {
        double totalFat = 0;
        for (Ingredient ingredient : ingredients) {
            totalFat += ingredient.getFat();
        }
        return totalFat;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(';').append(description);

        for (Ingredient ingredient : ingredients) {
            sb.append(';').append(ingredient.toString());
        }

        return sb.toString();
    }

}
