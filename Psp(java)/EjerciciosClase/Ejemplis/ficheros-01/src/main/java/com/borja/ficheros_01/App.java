package com.borja.ficheros_01;
 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import com.google.gson.Gson;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws MalformedURLException
    {
    	ArrayList<Personaje> personajesOriginal = deserializarPersonajes();
    	ArrayList<Personaje> personajesActualizada = deserializarPersonajes();
    	Scanner sc = new Scanner(System.in);
    	String input = null;
    	while ((input = sc.nextLine()) != "7") {
    		mostrarMenu();
    		switch (input) {
	    		case "1":
	    			conversor(Integer.parseInt(input));
	    			break;
	    		case "2":
	    			anadirPersonaje(Integer.parseInt(input), personajesActualizada);
	    			break;
	    		case "3":
	    			salvarPersonajes(personajesActualizada);
	    			break;
	    		case "4":
	    			mostrarPersonajes(personajesOriginal, personajesActualizada);
	    			break;
	    		case "5":
	    			especieDePersonaje(Integer.parseInt(input));
    		}
    	}
    }
    
    public static void mostrarMenu() {
    	System.out.println("1.Conversor.\r\n"
    			+ "2.Añadir Personaje.\r\n"
    			+ "3.Salvar Personajes.\r\n"
    			+ "4.Mostrar Personajes.\r\n"
    			+ "5.Especie del Personaje.\r\n"
    			+ "6.Mostrar datos XML.\r\n"
    			+ "7.Salir.\r\n");
    }
    
    public static void anadirPersonaje(int codigo, ArrayList<Personaje> personajes) {
    	String url = "https://swapi.dev/api/people/"+ codigo +"/?format=json";

        Gson gson = new Gson();
		
		Personaje personaje = gson.fromJson(url, Personaje.class);
		
		if (!personajes.contains(personaje)) {
			personajes.add(personaje);
		}
		else {
			 System.out.println("Este personaje ya está en la lista");
		}
        
        
    }
    
    public static void conversor(int codigo) throws MalformedURLException {
    	URL srcUrl = new URL("https://swapi.dev/api/films/" + codigo + "/?format=json");
    	
    	try (InputStream is = srcUrl.openStream();
    		JsonReader rdr = Json.createReader(is)) {
	    	JsonObject obj = rdr.readObject();

	    	String title = obj.getString("title"),
	    			openingCrawl = obj.getString("opening_crawl"),
	    			director = obj.getString("director"),
	    			producer = obj.getString("producer"),
	    			releaseDate = obj.getString("release_date"),
	    			created = obj.getString("created"),
	    			edited = obj.getString("edited"),
	    			url = obj.getString("url");
	    	
			JsonNumber episodeId = obj.getJsonNumber("episode_id");
	    			
	    	JsonArray characters = obj.getJsonArray("characters"),
	    			planets = obj.getJsonArray("planets"),
	    			starships = obj.getJsonArray("starships"),
	    			vehicles = obj.getJsonArray("vehicles"),
	    			species = obj.getJsonArray("species");
	    	
	    	DocumentBuilderFactory fábricaCreadorDocumento = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder creadorDocumento = fábricaCreadorDocumento.newDocumentBuilder();
	    	
	    	Document documento = creadorDocumento.newDocument();
	    	
	    	Element eRaiz = documento.createElement("film");
	    	documento.appendChild(eRaiz);
	    	
	    	Element eTitle = documento.createElement("title");
	    	eRaiz.appendChild(eTitle);
	    	Text txtTitle = documento.createTextNode(title);
	    	eTitle.appendChild(txtTitle);
	    	
	    	Element eEpisodeId = documento.createElement("episode_id");
	    	eRaiz.appendChild(eEpisodeId);
	    	Text txtEpisodeId = documento.createTextNode(episodeId.toString());
	    	eEpisodeId.appendChild(txtEpisodeId);
	    	
	    	Element eOpeningCrawl = documento.createElement("opening_crawl");
	    	eRaiz.appendChild(eOpeningCrawl);
	    	Text txtOpeningCrawl = documento.createTextNode(openingCrawl);
	    	eOpeningCrawl.appendChild(txtOpeningCrawl);
	    	
	    	Element eDirector = documento.createElement("director");
	    	eRaiz.appendChild(eDirector);
	    	Text txtDirector = documento.createTextNode(director);
	    	eDirector.appendChild(txtDirector);
	    	
	    	Element eProducer = documento.createElement("producer");
	    	eRaiz.appendChild(eProducer);
	    	Text txtProducer = documento.createTextNode(producer);
	    	eProducer.appendChild(txtProducer);
	    	
	    	Element eReleaseDate = documento.createElement("release_date");
	    	eRaiz.appendChild(eReleaseDate);
	    	Text txtReleaseDate = documento.createTextNode(releaseDate);
	    	eReleaseDate.appendChild(txtReleaseDate);
	    	
	    	Element eCharacters = documento.createElement("characters");
	    	eRaiz.appendChild(eCharacters);
	    	
	    	for (JsonValue character: characters) {
	    		Element eCharacter = documento.createElement("character");
		    	eCharacters.appendChild(eCharacter);
		    	Text txtCharacter = documento.createTextNode(character.toString());
		    	eCharacter.appendChild(txtCharacter);
	    	}
	    	
	    	Element ePlanets = documento.createElement("planets");
	    	eRaiz.appendChild(ePlanets);
	    	for (JsonValue planet: planets) {
	    		Element ePlanet = documento.createElement("planet");
		    	ePlanets.appendChild(ePlanet);
		    	Text txtPlanet = documento.createTextNode(planet.toString());
		    	ePlanet.appendChild(txtPlanet);
	    	}
	    	
	    	Element eStarships = documento.createElement("starships");
	    	eRaiz.appendChild(eStarships);
	    	for (JsonValue starship: starships) {
	    		Element eStarship = documento.createElement("starship");
	    		eStarships.appendChild(eStarship);
		    	Text txtStarship = documento.createTextNode(starship.toString());
		    	eStarship.appendChild(txtStarship);
	    	}
	    	
	    	Element eVehicles = documento.createElement("vehicles");
	    	eRaiz.appendChild(eVehicles);
	    	for (JsonValue vehicle: vehicles) {
	    		Element eVehicle = documento.createElement("vehicle");
	    		eVehicles.appendChild(eVehicle);
		    	Text txtVehicle = documento.createTextNode(vehicle.toString());
		    	eVehicle.appendChild(txtVehicle);
	    	}
	    	
	    	Element eSpecies = documento.createElement("species");
	    	eRaiz.appendChild(eSpecies);
	    	for (JsonValue specie: species) {
	    		Element eSpecie = documento.createElement("specie");
	    		eSpecies.appendChild(eSpecie);
		    	Text txtSpecie = documento.createTextNode(specie.toString());
		    	eSpecie.appendChild(txtSpecie);
	    	}
	    	
	    	Element eCreated = documento.createElement("created");
	    	eRaiz.appendChild(eCreated);
	    	Text txtCreated = documento.createTextNode(created);
	    	eCreated.appendChild(txtCreated);
	    	
	    	Element eEdited = documento.createElement("edited");
	    	eRaiz.appendChild(eEdited);
	    	Text txtEdited = documento.createTextNode(edited);
	    	eEdited.appendChild(txtEdited);
	    	
	    	Element eUrl = documento.createElement("url");
	    	eRaiz.appendChild(eUrl);
	    	Text txtUrl = documento.createTextNode(url);
	    	eUrl.appendChild(txtUrl);
	    	
	    	
	    	TransformerFactory fábricaTransformador = TransformerFactory.newInstance();
	        Transformer transformador = fábricaTransformador.newTransformer();
	        
	        transformador.setOutputProperty(OutputKeys.INDENT, "yes");
	        
	        transformador.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
	        
	        Source origen = new DOMSource(documento);
	        Result destino = new StreamResult("result.xml");
	        transformador.transform(origen, destino);
    	} 
    	catch (IOException e) {
    		System.out.println("ERROR: IO Exception\n" + e.getMessage());
    		e.printStackTrace();
		}
    	catch (ParserConfigurationException e) {
    	    System.out.println("ERROR: No se ha podido crear el generador de documentos XML\n"+e.getMessage());
    	    e.printStackTrace();
    	} catch (TransformerConfigurationException e) {
    	    System.out.println("ERROR: No se ha podido crear el transformador del documento XML\n"+e.getMessage());
    	    e.printStackTrace();
    	} catch (TransformerException e) {
    	    System.out.println("ERROR: No se ha podido crear la salida del documento XML\n"+e.getMessage());
    	    e.printStackTrace();
    	}
    }
    
    public static void salvarPersonajes(List<Personaje> personajes) {
    	 try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("personajes.dat"))) {
             outputStream.writeObject(personajes);
         } catch (IOException e) {
             e.printStackTrace();
         }
    }
    
    public static ArrayList<Personaje> deserializarPersonajes() {
    	ArrayList<Personaje> personajes = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("personajes.dat"))) {
            personajes = (ArrayList<Personaje>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        return personajes;
    }
    
    public static void mostrarPersonajes(ArrayList<Personaje> personajesOriginal, ArrayList<Personaje> personajesActualizada) {
    	System.out.println("Personajes originales:");
    	for (Personaje pers: personajesOriginal) {
    		System.out.println(pers);
    	}
    	
    	System.out.println("Personajes actualizada:");
    	for (Personaje pers: personajesActualizada) {
    		System.out.println(pers);
    	}
    }
    
    public static void especieDePersonaje(int codigo) throws MalformedURLException {
    	URL srcUrl = new URL("https://swapi.dev/api/people/" + codigo + "/?format=json");
    	
    	JsonArray especies = null;
    	try (InputStream is = srcUrl.openStream();
    		JsonReader rdr = Json.createReader(is)) {
	    	JsonObject obj = rdr.readObject();

	    	especies = obj.getJsonArray("species");
    	} 
    	catch (IOException e) {
    		System.out.println("ERROR: IO Exception\n" + e.getMessage());
    		e.printStackTrace();
		}
    	
    	for (JsonValue especie: especies) {
    		srcUrl = new URL(especie.toString());
    		try (InputStream is = srcUrl.openStream();
            		JsonReader rdr = Json.createReader(is)) {
        	    	JsonObject obj = rdr.readObject();

        	    	System.out.println(obj.getString("name"));
            } 
            catch (IOException e) {
            	System.out.println("ERROR: IO Exception\n" + e.getMessage());
            	e.printStackTrace();
        	}
    	}
    }
}
