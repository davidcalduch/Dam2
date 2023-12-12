package DavidCal.EjercicioObligatorio;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.google.gson.Gson;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class App{
    private static ArrayList<Personaje> personaje = new ArrayList<>();
    private static final String PERSONAJE_FILE = "personaje.dat";
    public static void main(String[] args) {
        while (true) {
            mostrarMenu();
            int opcion = obtenerOpcion();
            ejecutarOpcion(opcion);
        }
    }
    private static void mostrarMenu() {
        System.out.println("1-Conversor");
        System.out.println("2-Añadir Personaje"); 
        System.out.println("3-Salvar Personajes");
        System.out.println("4-Mostrar Personajes");
        System.out.println("5-Especie del Personaje");
        System.out.println("6-Mostrar datos XML");
        System.out.println("7-Salir");
    }
    private static int obtenerOpcion() {
        int opcion = 0;
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Selecciona una opción");
            opcion = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Error de formato. Ingresa un número válido.");
        }
        return opcion;
    }
    private static void ejecutarOpcion(int opcion) {
    	Scanner scanner = new Scanner(System.in);
        switch (opcion) {
            case 1:
            	  try {
                      System.out.print("Ingrese el código de película: ");
                      int codigoPelicula = Integer.parseInt(scanner.nextLine());
                      conversor(codigoPelicula);
                  } catch (InputMismatchException e) {
                      System.out.println("Error de formato. Ingresa un número válido.");
                  }
                  break;
            case 2:
            	try {
            	System.out.println("Ingrese un codigo de personaje: ");
            	int codigo = Integer.parseInt(scanner.nextLine());
            	anyadirPersonaje(codigo,personaje);
            	}catch(InputMismatchException e)
            	{
            		System.out.print("Error de formato. Ingresa otro caracter valido");
            	}
                break;
            case 3:
            	 salvarPersonajes(personaje);
                break;
            case 4:
            	 mostrarPersonajes();
                break;
            case 5:
            	especieDelPersonaje();
                break;
            case 6:
              System.out.print("no me dio tiempo a hacerlo");
                break;
            case 7:
                System.out.println("Saliendo del programa");
                System.exit(0);
                break;
            default:
                System.out.println("Opción no válida");
        }
    }
    public static void conversor(int codigo) {
        try {
            String apiUrl = "https://swapi.dev/api/films/" + codigo + "/?format=json";

            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }

                reader.close();

                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();

                Element filmElement = doc.createElement("film");
                doc.appendChild(filmElement);

                JSONObject jsonObject = new JSONObject(jsonBuilder.toString());

                for (String key : jsonObject.keySet()) {
                    Object value = jsonObject.get(key);

                    if (value instanceof JSONArray) {
                        JSONArray jsonArray = (JSONArray) value;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Element arrayElement = doc.createElement(key);
                            arrayElement.appendChild(doc.createTextNode(jsonArray.getString(i)));
                            filmElement.appendChild(arrayElement);
                        }
                    } else {
                        Element element = doc.createElement(key);
                        element.appendChild(doc.createTextNode(value.toString()));
                        filmElement.appendChild(element);
                    }
                }

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);

                StringWriter xmlStringWriter = new StringWriter();
                StreamResult result = new StreamResult(xmlStringWriter);

                transformer.transform(source, result);

                String xmlString = xmlStringWriter.toString();
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("result.xml"))) {
                    writer.write(xmlString);
                }
            } else {
                System.out.println("Error al obtener el JSON de la película. Código de estado HTTP: " + connection.getResponseCode());
            }
        } catch (IOException | ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }
   public static void anyadirPersonaje(int codigo, ArrayList<Personaje> personajes) {
        try {
            String apiUrl = "https://swapi.dev/api/people/" + codigo + "/?format=json";

            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }

                reader.close();

                Gson gson = new Gson();
                Personaje personaje = gson.fromJson(jsonBuilder.toString(), Personaje.class);

                if (!personajeYaExiste(personaje, personajes)) {
                    personajes.add(personaje);
                    System.out.println("Personaje añadido con éxito.");
                } else {
                    System.out.println("Este personaje ya está en la lista.");
                }
            } else {
                System.out.println("Error al obtener el JSON del personaje. Código de estado HTTP: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean personajeYaExiste(Personaje personaje, ArrayList<Personaje> personajes) {
        return personajes.contains(personaje);
    }
    public static void salvarPersonajes(ArrayList<Personaje> personajes) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("personajes.dat");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {

            objectOutputStream.writeObject(personajes);
            System.out.println("Personajes salvados con éxito en personajes.dat.");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al salvar los personajes en personajes.dat.");
        }
    }
    public static void cargarPersonajes() {
        try (FileInputStream fileInputStream = new FileInputStream(PERSONAJE_FILE);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

            List<Personaje> personajesAnteriores = (List<Personaje>) objectInputStream.readObject();
            personaje.addAll(personajesAnteriores);
            System.out.println("Personajes cargados desde personajes.dat.");

        } catch (FileNotFoundException e) {
            System.out.println("No se encontró el archivo personajes.dat. Se creará uno nuevo.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar los personajes desde personajes.dat.");
        }
    }
    public static void mostrarPersonajes() {
        if (personaje.isEmpty()) {
            System.out.println("No hay personajes para mostrar.");
        } else {
            System.out.println("Lista de Personajes:");
            for (Personaje personaje : personaje) {
                System.out.println("Nombre: " + personaje.getName()); 
            }
        }
    }
    public static void especieDelPersonaje() {
    	 int codigoPersonaje = 0; 
    	    try {
    	        Scanner scanner = new Scanner(System.in);
    	        System.out.print("Ingrese el código del personaje: ");
    	        codigoPersonaje = Integer.parseInt(scanner.nextLine());
    	    } catch (InputMismatchException e) {
    	        System.out.print("Error de formato");
    	    }
    	
        try {
            String apiUrl = "https://swapi.dev/api/species/" + codigoPersonaje + "/?format=json";

            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }

                reader.close();

                try {
                    JSONObject species = new JSONObject(jsonBuilder.toString());
                    String speciesName = species.getString("name");
                    System.out.println("Especie del personaje: " + speciesName);
                } catch (JSONException e) {
                    System.out.println("Error al procesar los datos de la especie. JSON no válido.");
                }
            } else {
                System.out.println("Error al obtener los datos de la especie. Código de estado HTTP: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            System.out.println("Error al conectar con el servidor. Asegúrate de tener una conexión a Internet.");
        } catch (NumberFormatException e) {
            System.out.println("Error al procesar el código del personaje. Debe ser un número válido.");
        }
    }
 }





