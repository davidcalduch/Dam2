package com.example.cateringfx2.model;

public interface MenuElement {
    double getCalories();
    double getCarbohydrates();
    double getFat();
    boolean hasMilk();
    boolean hasNuts();
    boolean hasEggs();
    boolean hasGluten();


    String getName();
}
