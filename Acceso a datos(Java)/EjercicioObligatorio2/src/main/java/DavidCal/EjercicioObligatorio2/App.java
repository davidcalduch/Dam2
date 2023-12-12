package DavidCal.EjercicioObligatorio2;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

import java.beans.Statement;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;
import java.util.stream.Collectors;
import com.google.gson.JsonArray;

public class App {

    private static final String SWAPI_URL = "https://swapi.dev";
    private static int currentCode = 1;

    public static void main(String[] args) throws FileNotFoundException {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/swapi";
            String usuario = "postgres";
            String password = "postgres";

            // Uso de try-with-resources para garantizar el cierre de la conexión
            try (Connection con = DriverManager.getConnection(url, usuario, password)) {
               // loadData(con);
            	displayMenu(con);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void loadData(Connection con) throws SQLException, FileNotFoundException {
         DataClass.loadPeopleData(con);
         DataClass.loadFilms(con);
         DataClass.loadPlanets(con);
         DataClass.loadSpecies(con);
         DataClass.loadStarships(con);
         DataClass.loadVehicles(con);
         DataClass.loadFilmsPeople(con);
         DataClass.loadVehiclesFilms(con);
         DataClass.loadStarshipsFilms(con);
         DataClass.loadSpeciesPeople(con);
         DataClass.loadStarshipPeople(con);
         DataClass.loadVehiclePeople(con);
         DataClass.loadFilmsPlanets(con);
    }

    private static void displayMenu(Connection con) throws SQLException, FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("Inside displayMenu while loop");
            System.out.println("Menu");
            System.out.println("1-Resetear base de datos");
            System.out.println("2-Nueva pelicula");
            System.out.println("3-Añadir otra pelicula");
            System.out.println("4-Buscar por nombre de pelicula");
            System.out.println("5-Buscar Personajes especiales");

            System.out.println("Elija una opcion");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    EliminarDatos(con);
                    loadData(con);
                    break;
                case 2:
                    addFilm(con);
                    break;
                case 3:
                	addFilmWithPreparedStatement(con);
                    break;
                case 4:
                	 buscarPorNombre(con);
                	 break;
                case 5:
                	 obtenerPersonajesSinStarships(con); 
                	break;
                default:
                    System.out.println("OPCION INCORRECTA");
            }
        }
        scanner.close();
    }

    private static void EliminarDatos(Connection con) throws SQLException {
        try (java.sql.Statement statement = con.createStatement()) {
            statement.executeUpdate(
                    "TRUNCATE TABLE films, planets, people, starships, species, vehicles, vehicles_films, starship_films, films_people, species_people, starship_people, vehicle_people, flims_planets");
        }
    }

    private static void addFilm(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese los datos de la nueva película:");
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Director: ");
        String director = scanner.nextLine();
        System.out.print("Episodio: ");
        int episode = scanner.nextInt();
        scanner.nextLine(); // Consumir el salto de línea

        // Obtener el próximo código de película
        int filmCode = getNextFilmCode(connection);

        // Insertar la película
        String insertFilmSql = "INSERT INTO films (codigo, title, director, episode_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement filmStatement = connection.prepareStatement(insertFilmSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            filmStatement.setInt(1, filmCode);
            filmStatement.setString(2, title);
            filmStatement.setString(3, director);
            filmStatement.setInt(4, episode);

            int filmRowsAffected = filmStatement.executeUpdate();

            if (filmRowsAffected > 0) {
                System.out.println("Película agregada con éxito.");

                // Solicitar personajes asociados a la película
                System.out.print("¿Cuántos personajes desea agregar a la película? ");
                int numCharacters = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                for (int i = 0; i < numCharacters; i++) {
                    System.out.print("Código del personaje " + (i + 1) + ": ");
                    int characterCode = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea

                    // Insertar relación entre película y personaje
                    String insertCharacterSql = "INSERT INTO films_people(codigo_film, codigo_people) VALUES (?, ?)";
                    try (PreparedStatement characterStatement = connection.prepareStatement(insertCharacterSql)) {
                        characterStatement.setInt(1, filmCode);
                        characterStatement.setInt(2, characterCode);

                        int characterRowsAffected = characterStatement.executeUpdate();

                        if (characterRowsAffected > 0) {
                            System.out.println("Relación exitosa entre la película y el personaje.");
                        } else {
                            System.out.println("No se pudo insertar la relación entre la película y el personaje.");
                        }
                    }
                }

                // Solicitar planetas asociados a la película
                System.out.print("¿Cuántos planetas desea agregar a la película? ");
                int numPlanets = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                for (int i = 0; i < numPlanets; i++) {
                    System.out.print("Código del planeta " + (i + 1) + ": ");
                    int planetCode = scanner.nextInt();
                    scanner.nextLine(); // Consumir el salto de línea

                    // Insertar relación entre película y planeta
                    String insertPlanetSql = "INSERT INTO films_planets(codigo_film, codigo_planet) VALUES (?, ?)";
                    try (PreparedStatement planetStatement = connection.prepareStatement(insertPlanetSql)) {
                        planetStatement.setInt(1, filmCode);
                        planetStatement.setInt(2, planetCode);

                        int planetRowsAffected = planetStatement.executeUpdate();

                        if (planetRowsAffected > 0) {
                            System.out.println("Relación exitosa entre la película y el planeta.");
                        } else {
                            System.out.println("No se pudo insertar la relación entre la película y el planeta.");
                        }
                    }
                }
            } else {
                System.out.println("No se pudo insertar la película.");
            }
        }
    }

    private static int getNextFilmCode(Connection con) throws SQLException {
        String query = "SELECT COALESCE(MAX(codigo), 0) + 1 FROM films";
        try (PreparedStatement preparedStatement = con.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 1; // Si no hay películas, empezamos desde 1
            }
        }
    }

    private static void insertFilmPlanet(Connection connection, int filmCode, int planetCode) throws SQLException {
        String insertFilmPlanetSql = "INSERT INTO films_planets(codigo_film, codigo_planet) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertFilmPlanetSql)) {
            preparedStatement.setInt(1, filmCode);
            preparedStatement.setInt(2, planetCode);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Relación exitosa entre la película y el planeta.");
            } else {
                System.out.println("No se pudo insertar la relación entre la película y el planeta.");
            }
        }
    }
    private static void insertFilmCharacter1(Connection connection, int filmCode, int characterCode) throws SQLException {
        // Verificar si la relación ya existe
        if (filmCharacterExists(connection, filmCode, characterCode)) {
            System.out.println("La relación entre la película y el personaje ya existe.");
            return;
        }

        String insertFilmCharacterSql = "INSERT INTO films_people(codigo_film, codigo_people) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertFilmCharacterSql)) {
            preparedStatement.setInt(1, filmCode);
            preparedStatement.setInt(2, characterCode);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Relación exitosa entre la película y el personaje.");
            } else {
                System.out.println("No se pudo insertar la relación entre la película y el personaje.");
            }
        }
    }


    private static boolean filmCharacterExists(Connection connection, int filmCode, int characterCode) throws SQLException {
        String query = "SELECT COUNT(*) FROM films_people WHERE codigo_film = ? AND codigo_people = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, filmCode);
            preparedStatement.setInt(2, characterCode);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    private static void insertFilmCharacter(Connection connection, int filmCode, int characterCode) throws SQLException {
        // Verificar si la relación ya existe
        if (filmCharacterExists(connection, filmCode, characterCode)) {
            System.out.println("La relación entre la película y el personaje ya existe.");
            return;
        }

        String insertFilmCharacterSql = "INSERT INTO films_people(codigo_film, codigo_people) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertFilmCharacterSql)) {
            preparedStatement.setInt(1, filmCode);
            preparedStatement.setInt(2, characterCode);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Relación exitosa entre la película y el personaje.");
            } else {
                System.out.println("No se pudo insertar la relación entre la película y el personaje.");
            }
        }
    }
    private static void buscarPorNombre(Connection con) throws SQLException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el nombre a buscar: ");
        String nombre = scanner.nextLine();

        // Llamar al procedimiento almacenado y mostrar los resultados
        try (CallableStatement callableStatement = con.prepareCall("{CALL buscar_peliculas(?)}")) {
            callableStatement.setString(1, nombre);

            // Ejecutar la consulta
            try (ResultSet resultSet = callableStatement.executeQuery()) {
                if (!resultSet.isBeforeFirst()) {
                    System.out.println("No se encontraron resultados.");
                } else {
                    while (resultSet.next()) {
                        String title = resultSet.getString("title");
                        String director = resultSet.getString("director");
                        String characterName = resultSet.getString("character_name");

                        // Handle null values
                        title = (title != null) ? title : "N/A";
                        director = (director != null) ? director : "N/A";
                        characterName = (characterName != null) ? characterName : "N/A";

                        System.out.println("Título: " + title);
                        System.out.println("Director: " + director);
                        System.out.println("Nombre del personaje: " + characterName);
                        System.out.println("----------------------");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        private static void obtenerPersonajesSinStarships(Connection con) throws SQLException {
            try (CallableStatement callableStatement = con.prepareCall("{CALL personajes_sin_starships()}")) {
                try (ResultSet resultSet = callableStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String characterName = resultSet.getString(1);

                        System.out.println("Nombre del personaje sin starships: " + characterName);
                        System.out.println("----------------------");
                    }
                }
            }
        }
        
        private static void addFilmWithPreparedStatement(Connection connection) throws SQLException {
            try (Scanner scanner = new Scanner(System.in);
                 PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO films (codigo, title, director, episode_id) VALUES (?, ?, ?, ?)")) {

                // Generar el código de la película
                int filmCode = getNextFilmCode(connection);

                System.out.print("Título de la película: ");
                String title = scanner.nextLine();

                System.out.print("Director de la película: ");
                String director = scanner.nextLine();

                System.out.print("Episodio de la película: ");
                int episode = scanner.nextInt();

                preparedStatement.setInt(1, filmCode); // Asignar el código generado
                preparedStatement.setString(2, title);
                preparedStatement.setString(3, director);
                preparedStatement.setInt(4, episode);

                preparedStatement.executeUpdate();

                System.out.println("Película añadida con éxito.");
            }
        }
        private static int getNextFilmCode1(Connection con) throws SQLException {
            String query = "SELECT COALESCE(MAX(codigo), 0) + 1 FROM films";
            try (PreparedStatement preparedStatement = con.prepareStatement(query);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                } else {
                    return 1; // Si no hay películas, empezamos desde 1
                }
            }
        }
       

         
}

