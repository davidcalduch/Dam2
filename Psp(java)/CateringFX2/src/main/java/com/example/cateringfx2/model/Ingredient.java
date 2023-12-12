package com.example.cateringfx2.model;

import com.example.cateringfx2.model.Aliment;

public class Ingredient  {
    private double quantity;
    private Aliment aliment;

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Aliment getAliment() {
        return aliment;
    }

    public void setAliment(Aliment aliment) {
        this.aliment = aliment;
    }

    /**
     *
     * @param quantity
     * @param aliment
     */
    public Ingredient(double quantity, Aliment aliment) {
        this.quantity = quantity;
        this.aliment = aliment;
    }
    public double getCalories() {
        // Verifica si el 'aliment' está definido y obtén sus calorías
        if (aliment != null) {
            return aliment.getCalories() * quantity;
        } else {
            return 0.0; // O maneja el caso donde 'aliment' es nulo
        }
    }
    public double getCarbohydrates()
    {
        if (aliment != null) {
            return aliment.getCarbohydrates() * quantity;
        } else {
            return 0.0; // O maneja el caso donde 'aliment' es nulo
        }
    }
    public double getFat()
    {
        if (aliment != null) {
            return aliment.getFat() * quantity;
        } else {
            return 0.0; // O maneja el caso donde 'aliment' es nulo
        }
    }
    @Override
    public String toString() {
        return String.format("%.1f;%s;%s;%b;%b;%b;%b;%.1f;%.1f;%.1f",
                quantity, aliment.getName(), aliment.getDescription(), aliment.getFrequency(),
                aliment.isGluten(), aliment.isMilk(), aliment.isNuts(), aliment.isEggs(),
                aliment.getCalories(), aliment.getCarbohydrates(), aliment.getFat());
    }


}
