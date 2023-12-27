package DavidCal.EjercicioObligatorio;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
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
    private static final String PERSONAJE_FILE = "personaje.dat"; // Agrega el nombre del archivo

    public static void main(String[] args) {
        while (true) {
            mostrarMenu();
            int opcion = obtenerOpcion();
            ejecutarOpcion(opcion);
        }
    }

    private static void mostrarMenu() {
        System.out.println("1-Conversor");
        System.out.println("2-Añadir Personaje"); // Corrige la opción 2
        System.out.println("3-Salvar Personajes");
        System.out.println("4-Mostrar Personajes");
        System.out.println("5-Especie del Personaje");
        System.out.println("6-Mostrar datos XML");
        System.out.println("7-Salir");
    }

    private static int obtenerOpcion() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecciona una opción");
        return scanner.nextInt();
    }

    private static void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
            	Scanner scanner = new Scanner(System.in);
            	System.out.print("Ingrese el código de película: ");
            	int codigoPelicula = Integer.parseInt(scanner.nextLine());
            	conversor(codigoPelicula);

                break;
            case 2:
            	
                break;
            case 3:
                
                break;
            case 4:
               
                break;
            case 5:
                // Agrega la lógica para obtener la especie del personaje
                break;
            case 6:
                // Agrega la lógica para mostrar datos XML
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

            // Realiza la solicitud HTTP para obtener el JSON del personaje
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == 200) {
                // Lee el JSON de la respuesta
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }

                reader.close();

                // Convierte el JSON en un objeto Personaje
                Gson gson = new Gson();
                Personaje personaje = gson.fromJson(jsonBuilder.toString(), Personaje.class);

                if (!personajes.contains(personaje)) {
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


}
