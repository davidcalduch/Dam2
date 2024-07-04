package com.fran.practica6.models.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "people")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "person_seq")
    @SequenceGenerator(name = "person_seq", sequenceName = "person_seq", allocationSize = 1)
    private Long codigo;
    
    private String name;
    private String birthYear;
    private String eyeColor;
    private String height;
    private String gender;
    private String hairColor;
    private String skinColor;
    private String mass;
    private int homeworld;
    private String edited;
    private String created;

    // Getters and setters
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getHeight() {
        return height;
    }
    public void setHeight(String height) {
        this.height = height;
    }

    
    public String getGender() {
        return gender;
    }
    public void setGender(String gender){
        this.gender= gender;
    }
    public String getHairColor() {
        return hairColor;
    }
    public void setHairColor(String hairColor){
        this.hairColor= hairColor;
    }

    public String getSkinColor() {
        return skinColor;
    }
    public void setSkinColor(String skinColor){
        this.skinColor= skinColor;
    }
    public String getMass() {
        return mass;
    }

    public int getHomeworld() {
        return homeworld;
    }
    public void setHomeworld(int homeworld) {
        this.homeworld = homeworld;
    }
    public void setMass(String mass) {
        this.mass = mass;
    }

    public String getEdited() {
        return edited;
    }

    public void setEdited(String edited) {
        this.edited = edited;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
    
}