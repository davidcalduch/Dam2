package com.example.cateringfx2.model;
import java.time.LocalDate;
import java.util.List;
import java.io.Serializable;

public class Menu implements Serializable {
    private LocalDate date;
    private List<MenuElement> elements;
    private double calories;
    private double carbohydrates;
    private double fat;

    public Menu(LocalDate date, List<MenuElement> elements) {
        this.date = date;
        this.elements = elements;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<MenuElement> getElements() {
        return elements;
    }

    public void setElements(List<MenuElement> elements) {
        this.elements = elements;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(date);
        for (MenuElement element : elements) {
            sb.append(';').append(element.toString());
        }
        return sb.toString();
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }
}
