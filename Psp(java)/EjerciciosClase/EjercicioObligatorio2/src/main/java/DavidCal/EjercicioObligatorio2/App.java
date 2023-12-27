package DavidCal.EjercicioObligatorio2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.nio.charset.StandardCharsets;

public class App {

    private static final String SWAPI_URL = "https://swapi.dev/api/";

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/swapi";
            String usuario = "postgres";
            String password = "postgres";
            Connection con = DriverManager.getConnection(url, usuario, password);

            displayMenu(con);
            con.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void loadData(Connection con) throws SQLException {
        // Truncate existing data
        truncateTables(con);

        loadPeopleData(con);
    }

    private static void truncateTables(Connection con) throws SQLException {
        String[] tableNames = {"films", "planets", "people", "starships", "species", "vehicles", "vehicles_films",
                "starship_films", "films_people", "species_people", "starship_people", "vehicle_people", "films_planets"};

        for (String tableName : tableNames) {
            String truncateSql = "TRUNCATE TABLE " + tableName + " CASCADE";
            try (PreparedStatement preparedStatement = con.prepareStatement(truncateSql)) {
                preparedStatement.executeUpdate();
            }
        }
    }

    private static void displayMenu(Connection con) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Menu");
            System.out.println("1-Resetear base de datos");

            System.out.println("Elija una opcion");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    resetDatabase(con);
                    break;
                default:
                    System.out.println("OPCION INCORRECTA");
            }
        }
        scanner.close();
    }

    private static void loadPeopleData(Connection con) {
        try {
            String peopleUrl = SWAPI_URL + "people/";
            JsonNode peopleData = fetchDataFromApi(peopleUrl);

            for (JsonNode person : peopleData.get("results")) {
                insertPeople(con, person);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static JsonNode fetchDataFromApi(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(jsonReader);
            JsonNode jsonNode = new com.fasterxml.jackson.databind.ObjectMapper().readTree(jsonElement.toString());

            jsonReader.close();
            inputStream.close();
            connection.disconnect();

            return jsonNode;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void insertPeople(Connection con, JsonNode person) throws SQLException {
        String insertPeopleSql = "INSERT INTO people(codigo, name, birth_year, eye_color, gender, hair_color, height, mass, skin_color, homeworld, created, edited) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(insertPeopleSql)) {
            preparedStatement.setInt(1, extractCodeFromUrl(person.get("url").asText()));
            preparedStatement.setString(2, person.get("name").asText());
            preparedStatement.setString(3, person.get("birth_year").asText());
            preparedStatement.setString(4, person.get("eye_color").asText());
            preparedStatement.setString(5, person.get("gender").asText());
            preparedStatement.setString(6, person.get("hair_color").asText());
            preparedStatement.setString(7, person.get("height").asText());
            preparedStatement.setString(8, person.get("mass").asText());
            preparedStatement.setString(9, person.get("skin_color").asText());
            preparedStatement.setInt(10, extractCodeFromUrl(person.get("homeworld").asText()));
            preparedStatement.setString(11, person.get("created").asText());
            preparedStatement.setString(12, person.get("edited").asText());

        }
    }

    private static void resetDatabase(Connection con) throws SQLException {
        truncateTables(con);
        loadData(con);
        System.out.println("Base de datos reseteada");
    }

    private static int extractCodeFromUrl(String url) {
        String[] parts = url.split("/");
        String lastPart = parts[parts.length - 1];
        return Integer.parseInt(lastPart);
    }

    // Add methods to fetch data from SWAPI, parse JSON, and insert data into corresponding tables
    // ...
}
