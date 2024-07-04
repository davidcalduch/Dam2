package com.fran.practica6.models.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "starships")
public class Starship {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "starship_seq")
    @SequenceGenerator(name = "starship_seq", sequenceName = "starship_seq", allocationSize = 1)
    private Long codigo; // Cambiar de "id" a "codigo"
    
    private String name;
    private String model;
    private String starshipClass;
    private String manufacturer;
    private String costInCredits;
    private String length; // Agregando el campo length
    private String crew;
    private String passengers;
    private String maxAtmospheringSpeed;
    private String hyperdriveRating;
    private String mglt;
    private String cargoCapacity;
    private String consumables;
    private String edited;
    private String created;

    // @OneToMany(mappedBy = "pilot")
    // private List<Person> peoples;


    public Long getCodigo() { // Cambiar de "getId" a "getCodigo"
        return codigo;
    }
    public void setCodigo(Long codigo) { // Cambiar de "setId" a "setCodigo"
        this.codigo = codigo;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getStarshipClass() {
        return starshipClass;
    }
    public void setStarshipClass(String starshipClass) {
        this.starshipClass = starshipClass;
    }
    public String getManufacturer() {
        return manufacturer;
    }
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
    public String getCostInCredits() {
        return costInCredits;
    }
    public void setCostInCredits(String costInCredits) {
        this.costInCredits = costInCredits;
    }
    public String getLength() {
        return length;
    }
    public void setLength(String length) {
        this.length = length;
    }
    public String getCrew() {
        return crew;
    }
    public void setCrew(String crew) {
        this.crew = crew;
    }
    public String getPassengers() {
        return passengers;
    }
    public void setPassengers(String passengers) {
        this.passengers = passengers;
    }
    public String getMaxAtmospheringSpeed() {
        return maxAtmospheringSpeed;
    }
    public void setMaxAtmospheringSpeed(String maxAtmospheringSpeed) {
        this.maxAtmospheringSpeed = maxAtmospheringSpeed;
    }
    public String getHyperdriveRating() {
        return hyperdriveRating;
    }
    public void setHyperdriveRating(String hyperdriveRating) {
        this.hyperdriveRating = hyperdriveRating;
    }
    public String getMglt() {
        return mglt;
    }
    public void setMglt(String mglt) {
        this.mglt = mglt;
    }
    public String getCargoCapacity() {
        return cargoCapacity;
    }
    public void setCargoCapacity(String cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
    }
    public String getConsumables() {
        return consumables;
    }
    public void setConsumables(String consumables) {
        this.consumables = consumables;
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
