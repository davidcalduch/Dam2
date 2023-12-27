package com.borja.ficheros_01;

import java.io.Serializable;
import java.util.List;

public class Personaje implements Serializable{
		private String name;
		private String height;
	    private String mass;
	    private String hair_color;
	    private String skin_color;
	    private String eye_color;
	    private String birth_year;
	    private String gender;
	    private String homeworld;
	    private List<String> films;
	    private List<String> species;
	    private List<String> vehicles;
	    private List<String> starships;
	    private String created;
	    private String edited;
	    private String url;
	    
	    
	    
	    @Override
		public String toString() {
			return "Personaje [name=" + name + "]";
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getHeight() {
			return height;
		}
		public String getMass() {
			return mass;
		}
		public String getHair_color() {
			return hair_color;
		}
		public String getSkin_color() {
			return skin_color;
		}
		public String getEye_color() {
			return eye_color;
		}
		public String getBirth_year() {
			return birth_year;
		}
		public String getGender() {
			return gender;
		}
		public String getHomeworld() {
			return homeworld;
		}
		public List<String> getFilms() {
			return films;
		}
		public List<String> getSpecies() {
			return species;
		}
		public List<String> getVehicles() {
			return vehicles;
		}
		public List<String> getStarships() {
			return starships;
		}
		public String getCreated() {
			return created;
		}
		public String getEdited() {
			return edited;
		}
		public String getUrl() {
			return url;
		}
		public void setHeight(String height) {
			this.height = height;
		}
		public void setMass(String mass) {
			this.mass = mass;
		}
		public void setHair_color(String hair_color) {
			this.hair_color = hair_color;
		}
		public void setSkin_color(String skin_color) {
			this.skin_color = skin_color;
		}
		public void setEye_color(String eye_color) {
			this.eye_color = eye_color;
		}
		public void setBirth_year(String birth_year) {
			this.birth_year = birth_year;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public void setHomeworld(String homeworld) {
			this.homeworld = homeworld;
		}
		public void setFilms(List<String> films) {
			this.films = films;
		}
		public void setSpecies(List<String> species) {
			this.species = species;
		}
		public void setVehicles(List<String> vehicles) {
			this.vehicles = vehicles;
		}
		public void setStarships(List<String> starships) {
			this.starships = starships;
		}
		public void setCreated(String created) {
			this.created = created;
		}
		public void setEdited(String edited) {
			this.edited = edited;
		}
		public void setUrl(String url) {
			this.url = url;
		}
	    
	    
}
