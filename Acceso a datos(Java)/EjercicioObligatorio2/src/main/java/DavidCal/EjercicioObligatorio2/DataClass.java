package DavidCal.EjercicioObligatorio2;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.crypto.Data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;

public class DataClass {
	 private static final String SWAPI_URL = "https://swapi.dev";
	 private static int currentCode = 1; 
	 
	 

	    private static JsonElement fetchDataFromApi(String apiUrl) {
	        try {
	            URL url = new URL(apiUrl);
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setRequestMethod("GET");

	            int responseCode = connection.getResponseCode();
	            System.out.println("Response Code: " + responseCode);

	            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
	                JsonParser parser = new JsonParser();
	                String responseData = reader.lines().collect(Collectors.joining());

	                try {
	                    // Intenta parsear la respuesta como JSON
	                    return parser.parse(responseData);
	                } catch (JsonSyntaxException jsonSyntaxException) {
	                    // Si no se puede parsear como JSON, devuelve la respuesta como String
	                    return new JsonPrimitive(responseData);
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    private static void resetCurrentCode() {
	        currentCode = 1;  // Puedes ajustar este valor según tus necesidades
	    }
	    private static boolean hasMorePages(JsonObject personData) {
	        return personData.has("next") && !personData.get("next").isJsonNull();
	    }
	    private static String getStringOrNull(JsonObject jsonObject, String key) {
	        return jsonObject.has(key) && !jsonObject.get(key).isJsonNull() ? jsonObject.get(key).getAsString() : null;
	    }
	    private static int getCodeFromUrl(String url) {
	        String[] urlSegments = url.split("/");
	        String codeStr = urlSegments[urlSegments.length - 1];

	        try {
	            return Integer.parseInt(codeStr);
	        } catch (NumberFormatException e) {
	            System.out.println("No se pudo convertir el código a un entero: " + codeStr);
	            return 0;
	        }
	    }
	 
	 public static void loadPeopleData(Connection con) {
	        System.out.println("Inside loadPeopleData method");
	        int currentPage = 1;
	        boolean hasMoreData = true;
	        int currentCode = 1;

	        while (hasMoreData) {
	            String peopleUrl = SWAPI_URL + "/api/people/?format=json&page=" + currentPage;
	            JsonElement peopleData = fetchDataFromApi(peopleUrl);

	            // Verifica si es un objeto JSON
	            if (peopleData != null && peopleData.isJsonObject()) {
	                JsonObject peopleObject = peopleData.getAsJsonObject();

	                // Obtén la lista de personas desde los resultados
	                JsonArray peopleArray = peopleObject.getAsJsonArray("results");

	                // Procesar cada persona en la lista
	                for (JsonElement personElement : peopleArray) {
	                    if (personElement.isJsonObject()) {
	                        JsonObject personObject = personElement.getAsJsonObject();

	                        // Extraer los campos específicos que necesitas
	                        String name = getStringOrNull(personObject, "name");
	                        String birthYear = getStringOrNull(personObject, "birth_year");
	                        String eyeColor = getStringOrNull(personObject, "eye_color");
	                        String gender = getStringOrNull(personObject, "gender");
	                        String hairColor = getStringOrNull(personObject, "hair_color");
	                        String height = getStringOrNull(personObject, "height");
	                        String mass = getStringOrNull(personObject, "mass");
	                        String skinColor = getStringOrNull(personObject, "skin_color");
	                        String created = getStringOrNull(personObject, "created");
	                        String edited = getStringOrNull(personObject, "edited");

	                        // Insertar en la base de datos
	                        insertPeople(con, currentCode, name, birthYear, eyeColor, gender, hairColor, height, mass, skinColor, created, edited);

	                        // Incrementar el código actual
	                        currentCode++;
	                    }
	                }

	                // Verificar si hay más páginas de resultados
	                hasMoreData = hasMorePages(peopleObject);
	                currentPage++;
	            } else {
	                System.out.println("Response Content:");
	                if (peopleData != null) {
	                    System.out.println(peopleData.getAsJsonPrimitive().getAsString());
	                } else {
	                    System.out.println("No data received");
	                }
	                hasMoreData = false;
	                currentPage++;
	            }
	        }
	    }
	 private static void insertPeople(Connection con, int code, String name, String birthYear, String eyeColor, String gender, String hairColor,
	            String height, String mass, String skinColor, String created, String edited) {
	        try {
	            String insertPeopleSql = "INSERT INTO people(codigo, name, birth_year, eye_color, gender, hair_color, height, mass, skin_color, created, edited, homeworld) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	            try (PreparedStatement preparedStatement = con.prepareStatement(insertPeopleSql)) {
	                preparedStatement.setInt(1, code);
	                preparedStatement.setString(2, name);
	                preparedStatement.setString(3, birthYear);
	                preparedStatement.setString(4, eyeColor);
	                preparedStatement.setString(5, gender);
	                preparedStatement.setString(6, hairColor);
	                preparedStatement.setString(7, height);
	                preparedStatement.setString(8, mass);
	                preparedStatement.setString(9, skinColor);
	                preparedStatement.setString(10, created);
	                preparedStatement.setString(11, edited);
	                preparedStatement.setInt(12, code);

	                int rowsAffected = preparedStatement.executeUpdate();

	                if (rowsAffected > 0) {
	                    System.out.println("Inserción exitosa para: " + name + " con código: " + code);
	                } else {
	                    System.out.println("No se insertaron filas para: " + name);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Error al insertar datos para " + name + ": " + e.getMessage());
	        }
	    }
	 public static void loadFilms(Connection con) {
	        int currentPage = 1;
	        boolean hasMoreData = true;
	        int currentCode = 1;

	        while (hasMoreData) {
	            String filmsUrl = SWAPI_URL + "/api/films/?format=json&page=" + currentPage;
	            JsonElement filmsData = fetchDataFromApi(filmsUrl);

	            if (filmsData != null && filmsData.isJsonObject()) {
	                JsonObject filmsObject = filmsData.getAsJsonObject();
	                JsonArray filmsArray = filmsObject.getAsJsonArray("results");

	                for (JsonElement filmsElement : filmsArray) {
	                    if (filmsElement.isJsonObject()) {
	                        JsonObject filmObject = filmsElement.getAsJsonObject();
	                        String title = getStringOrNull(filmObject, "title");
	                        String episodeId = getStringOrNull(filmObject, "episode_id");
	                        String openingCrawl = getStringOrNull(filmObject, "opening_crawl");
	                        String director = getStringOrNull(filmObject, "director");
	                        String producer = getStringOrNull(filmObject, "producer");
	                        String releaseDate = getStringOrNull(filmObject, "release_date");
	                        String created = getStringOrNull(filmObject, "created");
	                        String edited = getStringOrNull(filmObject, "edited");

	                        insertFilms(con, currentCode, title, episodeId, openingCrawl, director, producer, releaseDate, created, edited);

	                        currentCode++;
	                    }
	                }

	                hasMoreData = hasMorePages(filmsObject);
	                currentPage++;
	            } else {
	                System.out.println("Response Content:");
	                if (filmsData != null) {
	                    System.out.println(filmsData.getAsJsonPrimitive().getAsString());
	                } else {
	                    System.out.println("No data received");
	                }
	                hasMoreData = false;
	                currentPage++;
	            }
	        }
	    }

	    private static void insertFilms(Connection con, int code, String title, String episodeId, String openingCrawl, String director, String producer,
	            String releaseDate, String created, String edited) {
	        try {
	            String insertFilmsSql = "INSERT INTO films(codigo, title, episode_id, opening_crawl, director, producer, release_date, created, edited) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	            try (PreparedStatement preparedStatement = con.prepareStatement(insertFilmsSql)) {
	                preparedStatement.setInt(1, code);
	                preparedStatement.setString(2, title);
	                preparedStatement.setString(3, episodeId);
	                preparedStatement.setString(4, openingCrawl);
	                preparedStatement.setString(5, director);
	                preparedStatement.setString(6, producer);
	                preparedStatement.setString(7, releaseDate);
	                preparedStatement.setString(8, created);
	                preparedStatement.setString(9, edited);

	                int rowsAffected = preparedStatement.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("Inserción exitosa para la película: " + title + " con código: " + code);
	                } else {
	                    System.out.println("No se insertaron filas para la película: " + title);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Error al insertar datos para la película " + title + ": " + e.getMessage());
	        }
	    }


	 public static void loadVehicles(Connection con) {
	        int currentPage = 1;
	        boolean hasMoreData = true;

	        while (hasMoreData) {
	            String vehiclesUrl = SWAPI_URL + "/api/vehicles/?format=json&page=" + currentPage;
	            JsonElement vehiclesData = fetchDataFromApi(vehiclesUrl);

	            if (vehiclesData != null && vehiclesData.isJsonObject()) {
	                JsonObject vehiclesObject = vehiclesData.getAsJsonObject();
	                JsonArray vehiclesArray = vehiclesObject.getAsJsonArray("results");

	                for (JsonElement vehiclesElement : vehiclesArray) {
	                    if (vehiclesElement.isJsonObject()) {
	                        JsonObject vehicleObject = vehiclesElement.getAsJsonObject();
	                        String name = getStringOrNull(vehicleObject, "name");
	                        String model = getStringOrNull(vehicleObject, "model");
	                        String vehicleClass = getStringOrNull(vehicleObject, "vehicle_class");
	                        String manufacturer = getStringOrNull(vehicleObject, "manufacturer");
	                        String length = getStringOrNull(vehicleObject, "length");
	                        String costInCredit = getStringOrNull(vehicleObject, "cost_in_credits");
	                        String crew = getStringOrNull(vehicleObject, "crew");
	                        String passengers = getStringOrNull(vehicleObject, "passengers");
	                        String maxAtmospheringSpeed = getStringOrNull(vehicleObject, "max_atmosphering_speed");
	                        String cargoCapacity = getStringOrNull(vehicleObject, "cargo_capacity");
	                        String consumables = getStringOrNull(vehicleObject, "consumables");
	                        String created = getStringOrNull(vehicleObject, "created");
	                        String edited = getStringOrNull(vehicleObject, "edited");

	                        insertVehicle(con, currentCode, name, model, vehicleClass, manufacturer, length, costInCredit, crew, passengers, maxAtmospheringSpeed, cargoCapacity, consumables, created, edited);
	                        currentCode++;
	                    }
	                }

	                hasMoreData = hasMorePages(vehiclesObject);
	                currentPage++;
	            } else {
	                System.out.println("Response Content:");
	                if (vehiclesData != null) {
	                    System.out.println(vehiclesData.getAsJsonPrimitive().getAsString());
	                } else {
	                    System.out.println("No data received");
	                }
	                hasMoreData = false;
	                resetCurrentCode();
	            }
	        }
	    }
	    private static void insertVehicle(Connection con, int code, String name, String model, String vehicleClass, String manufacturer, String length,
	            String costInCredit, String crew, String passengers, String maxAtmospheringSpeed, String cargoCapacity, String consumables, String created, String edited) {
	        try {
	            String insertVehicleSql = "INSERT INTO vehicles(codigo, name, model, vehicle_class, manufacturer, length, cost_in_credits, crew, passengers, max_atmosphering_speed, cargo_capacity, consumables, created, edited) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	            try (PreparedStatement preparedStatement = con.prepareStatement(insertVehicleSql)) {
	                preparedStatement.setInt(1, code);
	                preparedStatement.setString(2, name);
	                preparedStatement.setString(3, model);
	                preparedStatement.setString(4, vehicleClass);
	                preparedStatement.setString(5, manufacturer);
	                preparedStatement.setString(6, length);
	                preparedStatement.setString(7, costInCredit);
	                preparedStatement.setString(8, crew);
	                preparedStatement.setString(9, passengers);
	                preparedStatement.setString(10, maxAtmospheringSpeed);
	                preparedStatement.setString(11, cargoCapacity);
	                preparedStatement.setString(12, consumables);
	                preparedStatement.setString(13, created);
	                preparedStatement.setString(14, edited);

	                int rowsAffected = preparedStatement.executeUpdate();

	                if (rowsAffected > 0) {
	                    System.out.println("Inserción exitosa para el vehículo: " + name + " con código: " + code);
	                } else {
	                    System.out.println("No se insertaron filas para el vehículo: " + name);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Error al insertar datos para el vehículo " + name + ": " + e.getMessage());
	        }
	    }
	    public static void loadSpecies(Connection con) {
	        int currentPage = 1;
	        boolean hasMoreData = true;

	        while (hasMoreData) {
	            String speciesUrl = SWAPI_URL + "/api/species/?format=json&page=" + currentPage;
	            JsonElement speciesData = fetchDataFromApi(speciesUrl);

	            if (speciesData != null && speciesData.isJsonObject()) {
	                JsonObject speciesObject = speciesData.getAsJsonObject();
	                JsonArray speciesArray = speciesObject.getAsJsonArray("results");

	                for (JsonElement speciesElement : speciesArray) {
	                    if (speciesElement.isJsonObject()) {
	                        JsonObject specieObject = speciesElement.getAsJsonObject();
	                        String name = getStringOrNull(specieObject, "name");
	                        String classification = getStringOrNull(specieObject, "classification");
	                        String designation = getStringOrNull(specieObject, "designation");
	                        String averageHeight = getStringOrNull(specieObject, "average_height");
	                        String averageLifespan = getStringOrNull(specieObject, "average_lifespan");
	                        String eyeColors = getStringOrNull(specieObject, "eye_colors");
	                        String hairColors = getStringOrNull(specieObject, "hair_colors");
	                        String skinColor = getStringOrNull(specieObject, "skin_colors");
	                        String language = getStringOrNull(specieObject, "language");
	                        String created = getStringOrNull(specieObject, "created");
	                        String edited = getStringOrNull(specieObject, "edited");

	                        insertSpecies(con, currentCode, name, classification, designation, averageHeight, averageLifespan, eyeColors, hairColors, skinColor, language, created, edited);

	                        currentCode++;
	                    }
	                }

	                hasMoreData = hasMorePages(speciesObject);
	                currentPage++;
	            } else {
	                System.out.println("Response Content:");
	                if (speciesData != null) {
	                    System.out.println(speciesData.getAsJsonPrimitive().getAsString());
	                } else {
	                    System.out.println("No data received");
	                }
	                hasMoreData = false;
	                resetCurrentCode();
	            }
	        }
	    }

	    private static void insertSpecies(Connection con, int code, String name, String classification, String designation, String averageHeight, String averageLifespan,
	            String eyeColors, String hairColors, String skinColor, String language, String created, String edited) {
	        try {
	            String insertSpeciesSql = "INSERT INTO species(codigo, name, classification, designation, average_height, average_lifespan, eye_colors, hair_colors, skin_color, language, created, edited) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	            try (PreparedStatement preparedStatement = con.prepareStatement(insertSpeciesSql)) {
	                preparedStatement.setInt(1, code);
	                preparedStatement.setString(2, name);
	                preparedStatement.setString(3, classification);
	                preparedStatement.setString(4, designation);
	                preparedStatement.setString(5, averageHeight);
	                preparedStatement.setString(6, averageLifespan);
	                preparedStatement.setString(7, eyeColors);
	                preparedStatement.setString(8, hairColors); // Corregir el nombre de la columna
	                preparedStatement.setString(9, skinColor);
	                preparedStatement.setString(10, language);
	                preparedStatement.setString(11, created);
	                preparedStatement.setString(12, edited);

	                int rowsAffected = preparedStatement.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("Inserción exitosa para la especie: " + name + " con código: " + code);
	                } else {
	                    System.out.println("No se insertaron filas para la especie: " + name);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            System.out.println("Error al insertar datos para la especie " + name + ": " + e.getMessage());
	        }
	    }


	   	    public static void loadPlanets(Connection con) {
	        int currentPage = 1;
	        boolean hasMoreData = true;

	        while (hasMoreData) {
	            String planetsUrl = SWAPI_URL + "/api/planets/?format=json&page=" + currentPage;
	            JsonElement planetsData = fetchDataFromApi(planetsUrl);

	            if (planetsData != null && planetsData.isJsonObject()) {
	                JsonObject planetsObject = planetsData.getAsJsonObject();
	                JsonArray planetsArray = planetsObject.getAsJsonArray("results");

	                for (JsonElement planetsElement : planetsArray) {
	                    if (planetsElement.isJsonObject()) {
	                        JsonObject planetObject = planetsElement.getAsJsonObject();
	                        String name = getStringOrNull(planetObject, "name");
	                        String climate = getStringOrNull(planetObject, "climate");
	                        String terrain = getStringOrNull(planetObject, "terrain");
	                        String gravity = getStringOrNull(planetObject, "gravity");
	                        String population = getStringOrNull(planetObject, "population");
	                        String diameter = getStringOrNull(planetObject, "diameter");
	                        String rotationPeriod = getStringOrNull(planetObject, "rotation_period");
	                        String orbitalPeriod = getStringOrNull(planetObject, "orbital_period");
	                        String surfaceWater = getStringOrNull(planetObject, "surface_water");
	                        String created = getStringOrNull(planetObject, "created");
	                        String edited = getStringOrNull(planetObject, "edited");

	                        insertPlanets(con, currentCode, name, climate, terrain, gravity, population, diameter, rotationPeriod, orbitalPeriod, surfaceWater, created, edited);

	                        currentCode++;
	                    }
	                }

	                hasMoreData = hasMorePages(planetsObject);
	                currentPage++;
	            } else {
	                System.out.println("Response Content:");
	                if (planetsData != null) {
	                    System.out.println(planetsData.getAsJsonPrimitive().getAsString());
	                } else {
	                    System.out.println("No data received");
	                }
	                hasMoreData = false;
	                resetCurrentCode();
	            }
	        }
	    } 
	    private static void insertPlanets(Connection con, int code, String name, String diameter, String rotationPeriod, String orbitalPeriod, String gravity, String population,
                String climate, String terrain, String surfaceWater, String created, String edited) {
            try {
                String insertPlanetsSql = "INSERT INTO planets(codigo, name, diameter, rotation_period, orbital_period, gravity, population, climate, terrain, surface_water, created, edited) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = con.prepareStatement(insertPlanetsSql)) {
                    preparedStatement.setInt(1, code);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, diameter);
                    preparedStatement.setString(4, rotationPeriod);
                    preparedStatement.setString(5, orbitalPeriod);
                    preparedStatement.setString(6, gravity);
                    preparedStatement.setString(7, population);
                    preparedStatement.setString(8, climate);
                    preparedStatement.setString(9, terrain);
                    preparedStatement.setString(10, surfaceWater);
                    preparedStatement.setString(11, created);
                    preparedStatement.setString(12, edited);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Inserción exitosa para el planeta: " + name + " con código: " + code);
                    } else {
                        System.out.println("No se insertaron filas para el planeta: " + name);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al insertar datos para el planeta " + name + ": " + e.getMessage());
            }
        }
	    public static void loadStarships(Connection con) {
            int currentPage = 1;
            boolean hasMoreData = true;

            while (hasMoreData) {
                String starshipsUrl = SWAPI_URL + "/api/starships/?format=json&page=" + currentPage;
                JsonElement starshipsData = fetchDataFromApi(starshipsUrl);

                if (starshipsData != null && starshipsData.isJsonObject()) {
                    JsonObject starshipsObject = starshipsData.getAsJsonObject();
                    JsonArray starshipsArray = starshipsObject.getAsJsonArray("results");

                    for (JsonElement starshipsElement : starshipsArray) {
                        if (starshipsElement.isJsonObject()) {
                            JsonObject starshipObject = starshipsElement.getAsJsonObject();
                            String name = getStringOrNull(starshipObject, "name");
                            String model = getStringOrNull(starshipObject, "model");
                            String starshipClass = getStringOrNull(starshipObject, "starship_class");
                            String manufacturer = getStringOrNull(starshipObject, "manufacturer");
                            String costInCredits = getStringOrNull(starshipObject, "cost_in_credits");
                            String length = getStringOrNull(starshipObject, "length");
                            String crew = getStringOrNull(starshipObject, "crew");
                            String passengers = getStringOrNull(starshipObject, "passengers");
                            String maxAtmospheringSpeed = getStringOrNull(starshipObject, "max_atmosphering_speed");
                            String hyperdriveRating = getStringOrNull(starshipObject, "hyperdrive_rating");
                            String MGLT = getStringOrNull(starshipObject, "MGLT");
                            String cargoCapacity = getStringOrNull(starshipObject, "cargo_capacity");
                            String consumables = getStringOrNull(starshipObject, "consumables");
                            String created = getStringOrNull(starshipObject, "created");
                            String edited = getStringOrNull(starshipObject, "edited");

                            insertStarships(con, currentCode, name, model, starshipClass, manufacturer, costInCredits, length, crew, passengers,
                                    maxAtmospheringSpeed, hyperdriveRating, MGLT, cargoCapacity, consumables, created, edited);

                            currentCode++;
                        }
                    }

                    hasMoreData = hasMorePages(starshipsObject);
                    currentPage++;
                } else {
                    System.out.println("Response Content:");
                    if (starshipsData != null) {
                        System.out.println(starshipsData.getAsJsonPrimitive().getAsString());
                    } else {
                        System.out.println("No data received");
                    }
                    hasMoreData = false;
                    resetCurrentCode();
                }
            }
        }

        private static void insertStarships(Connection con, int code, String name, String model, String starshipClass, String manufacturer, String costInCredits,
                String length, String crew, String passengers, String maxAtmospheringSpeed, String hyperdriveRating, String MGLT,
                String cargoCapacity, String consumables, String created, String edited) {
            try {
                String insertStarshipsSql = "INSERT INTO starships(codigo, name, model, starship_class, manufacturer, cost_in_credits, length, crew, passengers, max_atmosphering_speed, hyperdrive_rating, MGLT, cargo_capacity, consumables, created, edited) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = con.prepareStatement(insertStarshipsSql)) {
                    preparedStatement.setInt(1, code);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, model);
                    preparedStatement.setString(4, starshipClass);
                    preparedStatement.setString(5, manufacturer);
                    preparedStatement.setString(6, costInCredits);
                    preparedStatement.setString(7, length);
                    preparedStatement.setString(8, crew);
                    preparedStatement.setString(9, passengers);
                    preparedStatement.setString(10, maxAtmospheringSpeed);
                    preparedStatement.setString(11, hyperdriveRating);
                    preparedStatement.setString(12, MGLT);
                    preparedStatement.setString(13, cargoCapacity);
                    preparedStatement.setString(14, consumables);
                    preparedStatement.setString(15, created);
                    preparedStatement.setString(16, edited);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Inserción exitosa para la nave estelar: " + name + " con código: " + code);
                    } else {
                        System.out.println("No se insertaron filas para la nave estelar: " + name);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al insertar datos para la nave estelar " + name + ": " + e.getMessage());
            }
        }
        public static void loadFilmsPeople(Connection con) throws FileNotFoundException {
            int currentPage = 1;
            boolean hasMoreData = true;

            while (hasMoreData) {
                try {
                    String peopleUrl = SWAPI_URL + "/api/people/?format=json&page=" + currentPage;
                    JsonElement peopleData = fetchDataFromApi(peopleUrl);

                    if (peopleData != null && peopleData.isJsonObject()) {
                        JsonObject peopleObject = peopleData.getAsJsonObject();

                        if (peopleObject.has("results")) {
                            JsonArray peopleArray = peopleObject.getAsJsonArray("results");

                            for (JsonElement personElement : peopleArray) {
                                if (personElement.isJsonObject()) {
                                    JsonObject personObject = personElement.getAsJsonObject();
                                    String personUrl = getStringOrNull(personObject, "url");
                                    int peopleCode = getCodeFromUrl(personUrl);

                                    System.out.println("Processing person: " + peopleCode);

                                    JsonArray filmsArray = personObject.getAsJsonArray("films");

                                    for (JsonElement filmElement : filmsArray) {
                                        if (filmElement.isJsonPrimitive()) {
                                            String filmUrl = filmElement.getAsString();
                                            int filmCode = getCodeFromUrl(filmUrl);

                                            System.out.println("Processing film: " + filmCode);

                                            insertFilmsPeople(con, filmCode, peopleCode);
                                        }
                                    }
                                }
                            }

                            hasMoreData = hasMorePages(peopleObject);
                            currentPage++;
                        } else {
                            System.out.println("No se encontró el campo 'results' en la respuesta de personas.");
                            hasMoreData = false;
                        }
                    } else {
                        System.out.println("Response Content:");
                        if (peopleData != null) {
                            System.out.println(peopleData.getAsJsonPrimitive().getAsString());
                        } else {
                            System.out.println("No data received");
                        }
                        hasMoreData = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1); // Detener la ejecución en caso de otro tipo de error
                }
            }
        }

        // Método para insertar en la tabla films_people
     // Método para insertar en la tabla films_people
        private static void insertFilmsPeople(Connection con, int filmCode, int peopleCode) {
            try {
                // Verificar si el código de la persona existe en la tabla people
                if (!personExists(con, peopleCode)) {
                    System.out.println("Error: La persona con código " + peopleCode + " no existe en la tabla 'people'. No se puede insertar la relación en 'films_people'.");
                    return;
                }

                String insertFilmsPeopleSql = "INSERT INTO films_people(codigo_film, codigo_people) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = con.prepareStatement(insertFilmsPeopleSql)) {
                    preparedStatement.setInt(1, filmCode);
                    preparedStatement.setInt(2, peopleCode);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Relación exitosa entre la película con código " + filmCode + " y la persona con código " + peopleCode);
                    } else {
                        System.out.println("No se insertaron filas para la relación entre la película con código " + filmCode + " y la persona con código " + peopleCode);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al insertar relación entre la película con código " + filmCode + " y la persona con código " + peopleCode + ": " + e.getMessage());
            }
        }

        // Método para verificar si el código de la persona existe en la tabla people
        private static boolean personExists(Connection con, int peopleCode) {
            try {
                String query = "SELECT 1 FROM people WHERE codigo = ?";
                try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                    preparedStatement.setInt(1, peopleCode);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        return resultSet.next();  // Devuelve true si hay al menos una fila, es decir, la persona existe
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al verificar la existencia de la persona con código " + peopleCode + ": " + e.getMessage());
                return false;
            }
        }

        public static void loadVehiclesFilms(Connection con) {
            int currentPage = 1;
            boolean hasMoreData = true;

            while (hasMoreData) {
                try {
                    String vehiclesUrl = SWAPI_URL + "/api/vehicles/?format=json&page=" + currentPage;
                    JsonElement vehiclesData = fetchDataFromApi(vehiclesUrl);

                    if (vehiclesData != null && vehiclesData.isJsonObject()) {
                        JsonObject vehiclesObject = vehiclesData.getAsJsonObject();

                        if (vehiclesObject.has("results")) {
                            JsonArray vehiclesArray = vehiclesObject.getAsJsonArray("results");

                            for (JsonElement vehicleElement : vehiclesArray) {
                                if (vehicleElement.isJsonObject()) {
                                    JsonObject vehicleObject = vehicleElement.getAsJsonObject();
                                    String vehicleUrl = getStringOrNull(vehicleObject, "url");
                                    int vehicleCode = getCodeFromUrl(vehicleUrl);

                                    System.out.println("Processing vehicle: " + vehicleCode);

                                    JsonArray filmsArray = vehicleObject.getAsJsonArray("films");

                                    for (JsonElement filmElement : filmsArray) {
                                        if (filmElement.isJsonPrimitive()) {
                                            String filmUrl = filmElement.getAsString();
                                            int filmCode = getCodeFromUrl(filmUrl);

                                            System.out.println("Processing film: " + filmCode);

                                            insertVehiclesFilms(con, vehicleCode, filmCode);
                                        }
                                    }
                                }
                            }

                            hasMoreData = hasMorePages(vehiclesObject);
                            currentPage++;
                        } else {
                            System.out.println("No se encontró el campo 'results' en la respuesta de la relación entre vehículos y películas.");
                            hasMoreData = false;
                        }
                    } else {
                        System.out.println("Response Content:");
                        if (vehiclesData != null) {
                            System.out.println(vehiclesData.getAsJsonPrimitive().getAsString());
                        } else {
                            System.out.println("No data received");
                        }
                        hasMoreData = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1); // Detener la ejecución en caso de otro tipo de error
                }
            }
        }

        // Método para insertar en la tabla vehicles_films
        private static void insertVehiclesFilms(Connection con, int vehicleCode, int vehiclesFilmCode) {
            try {
                // Verificar si el código de vehículo existe en la tabla vehicles
                if (!vehicleExists(con, vehicleCode)) {
                    System.out.println("Error: El vehículo con código " + vehicleCode + " no existe en la tabla 'vehicles'. No se puede insertar la relación en 'vehicles_films'.");
                    return;
                }

                String insertVehiclesFilmsSql = "INSERT INTO vehicles_films(codigo_vehicles, codigo_film) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = con.prepareStatement(insertVehiclesFilmsSql)) {
                    preparedStatement.setInt(1, vehicleCode);
                    preparedStatement.setInt(2, vehiclesFilmCode);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Relación exitosa entre el vehículo con código " + vehicleCode + " y la película con código " + vehiclesFilmCode);
                    } else {
                        System.out.println("No se insertaron filas para la relación entre el vehículo con código " + vehicleCode + " y la película con código " + vehiclesFilmCode);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al insertar relación entre el vehículo con código " + vehicleCode + " y la película con código " + vehiclesFilmCode + ": " + e.getMessage());
            }
        }

        // Nuevo método para verificar si el código de vehículo existe en la tabla vehicles
        private static boolean vehicleExists(Connection con, int vehicleCode) throws SQLException {
            String query = "SELECT 1 FROM vehicles WHERE codigo = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setInt(1, vehicleCode);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();  // Devuelve true si hay al menos una fila, es decir, el vehículo existe
                }
            }
        }

        public static void loadStarshipsFilms(Connection con) {
            int currentPage = 1;
            boolean hasMoreData = true;

            while (hasMoreData) {
                try {
                    String starshipsUrl = SWAPI_URL + "/api/starships/?format=json&page=" + currentPage;
                    JsonElement starshipsData = fetchDataFromApi(starshipsUrl);

                    if (starshipsData != null && starshipsData.isJsonObject()) {
                        JsonObject starshipsObject = starshipsData.getAsJsonObject();

                        // Verifica si el campo "results" está presente
                        if (starshipsObject.has("results")) {
                            JsonArray starshipsArray = starshipsObject.getAsJsonArray("results");

                            if (starshipsArray.isEmpty()) {
                                // Si la lista de starships está vacía, no hay más datos para cargar
                                System.out.println("No se encontraron más datos en la API. Terminando la carga de datos.");
                                break; // Salir del bucle
                            }

                            for (JsonElement starshipElement : starshipsArray) {
                                if (starshipElement.isJsonObject()) {
                                    JsonObject starshipObject = starshipElement.getAsJsonObject();
                                    String starshipUrl = getStringOrNull(starshipObject, "url");
                                    int starshipCode = getCodeFromUrl(starshipUrl);

                                    // Imprime información de la nave espacial
                                    System.out.println("Processing starship: " + starshipCode);

                                    JsonArray filmsArray = starshipObject.getAsJsonArray("films");

                                    for (JsonElement filmElement : filmsArray) {
                                        if (filmElement.isJsonPrimitive()) {
                                            String filmUrl = filmElement.getAsString();
                                            int filmCode = getCodeFromUrl(filmUrl);

                                            // Imprime información de la película
                                            System.out.println("Processing film: " + filmCode);

                                            // Inserta en la tabla starships_films
                                            insertStarshipsFilms(con, starshipCode, filmCode);
                                        }
                                    }
                                }
                            }

                            hasMoreData = hasMorePages(starshipsObject);
                            currentPage++;
                        } else {
                            System.out.println("No se encontró el campo 'results' en la respuesta de naves espaciales.");
                            hasMoreData = false;
                        }
                    } else {
                        System.out.println("Response Content:");
                        if (starshipsData != null) {
                            System.out.println(starshipsData.getAsJsonPrimitive().getAsString());
                        } else {
                            System.out.println("No data received");
                        }
                        hasMoreData = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1); // Detener la ejecución en caso de otro tipo de error
                }
            }
        }
        private static boolean filmExists(Connection con, int filmCode) {
            try {
                String query = "SELECT 1 FROM films WHERE codigo = ?";
                try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                    preparedStatement.setInt(1, filmCode);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        return resultSet.next();  // Devuelve true si hay al menos una fila, es decir, la película existe
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al verificar la existencia de la película con código " + filmCode + ": " + e.getMessage());
                return false;
            }
        }

        private static void insertStarshipsFilms(Connection con, int starshipCode, int filmCode) {
            try {
                // Verificar si el código de la nave espacial existe en la tabla starships
                if (!starshipExists(con, starshipCode)) {
                    System.out.println("Error: La nave espacial con código " + starshipCode + " no existe en la tabla 'starships'. No se puede insertar la relación en 'starship_films'.");
                    return;
                }

                String insertStarshipsFilmsSql = "INSERT INTO starship_films(codigo_starship, codigo_film) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = con.prepareStatement(insertStarshipsFilmsSql)) {
                    preparedStatement.setInt(1, starshipCode);
                    preparedStatement.setInt(2, filmCode);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Relación exitosa entre la nave espacial con código " + starshipCode + " y la película con código " + filmCode);
                    } else {
                        System.out.println("No se insertaron filas para la relación entre la nave espacial con código " + starshipCode + " y la película con código " + filmCode);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al insertar relación entre la nave espacial con código " + starshipCode + " y la película con código " + filmCode + ": " + e.getMessage());
            }
        }
        public static void loadSpeciesPeople(Connection con) {
            int currentPage = 1;
            boolean hasMoreData = true;

            while (hasMoreData) {
                try {
                    String url = SWAPI_URL + "/api/species/?format=json&page=" + currentPage;
                    JsonElement speciesData = fetchDataFromApi(url);

                    if (speciesData != null && speciesData.isJsonObject()) {
                        JsonObject speciesObject = speciesData.getAsJsonObject();

                        if (speciesObject.has("results")) {
                            JsonArray speciesArray = speciesObject.getAsJsonArray("results");

                            for (JsonElement speciesElement : speciesArray) {
                                if (speciesElement.isJsonObject()) {
                                    JsonObject currentSpecies = speciesElement.getAsJsonObject();
                                    String speciesUrl = getStringOrNull(currentSpecies, "url");
                                    int speciesCode = getCodeFromUrl(speciesUrl);

                                    System.out.println("Processing species: " + speciesCode);

                                    JsonArray peopleArray = currentSpecies.getAsJsonArray("people");

                                    for (JsonElement personElement : peopleArray) {
                                        if (personElement.isJsonPrimitive()) {
                                            String personUrl = personElement.getAsString();
                                            int peopleCode = getCodeFromUrl(personUrl);

                                            System.out.println("Processing person: " + peopleCode);

                                            insertSpeciesPeople(con, speciesCode, peopleCode);
                                        }
                                    }
                                }
                            }

                            hasMoreData = hasMorePages(speciesObject);
                            currentPage++;
                        } else {
                            System.out.println("No se encontró el campo 'results' en la respuesta de especies.");
                            hasMoreData = false;
                        }
                    } else {
                        System.out.println("Response Content:");
                        if (speciesData != null) {
                            System.out.println(speciesData.getAsJsonPrimitive().getAsString());
                        } else {
                            System.out.println("No data received");
                        }
                        hasMoreData = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1); // Detener la ejecución en caso de otro tipo de error
                }
            }
        }



        // Método para insertar en la tabla species_people
        private static void insertSpeciesPeople(Connection con, int speciesCode, int peopleCode) {
            try {
                // Verificar si la persona existe en la tabla people
                if (!doesPersonExist(con, peopleCode)) {
                    System.out.println("La persona con código " + peopleCode + " no existe en la tabla people. La relación no será insertada.");
                    return; // Salir del método si la persona no existe
                }

                String insertSpeciesPeopleSql = "INSERT INTO species_people(codigo_species, codigo_people) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = con.prepareStatement(insertSpeciesPeopleSql)) {
                    preparedStatement.setInt(1, speciesCode);
                    preparedStatement.setInt(2, peopleCode);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Relación exitosa entre la especie con código " + speciesCode +
                                " y la persona con código " + peopleCode);
                    } else {
                        System.out.println("No se insertaron filas para la relación entre la especie con código " + speciesCode +
                                " y la persona con código " + peopleCode);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al insertar relación entre la especie con código " + speciesCode +
                        " y la persona con código " + peopleCode + ": " + e.getMessage());
            }
        }
        private static boolean doesPersonExist(Connection con, int peopleCode) throws SQLException {
            String checkPersonSql = "SELECT 1 FROM people WHERE codigo = ?";
            try (PreparedStatement checkPersonStatement = con.prepareStatement(checkPersonSql)) {
                checkPersonStatement.setInt(1, peopleCode);
                try (ResultSet resultSet = checkPersonStatement.executeQuery()) {
                    return resultSet.next(); // Retorna true si la persona existe, false de lo contrario
                }
            }
        }

            public static void loadStarshipPeople(Connection con) {
                int currentPage = 1;
                boolean hasMoreData = true;

                while (hasMoreData) {
                    try {
                        String url = SWAPI_URL + "/api/starships/?format=json&page=" + currentPage;
                        JsonElement starshipData = fetchDataFromApi(url);

                        if (starshipData != null && starshipData.isJsonObject()) {
                            JsonObject starshipResponse = starshipData.getAsJsonObject();

                            if (starshipResponse.has("results")) {
                                JsonArray starshipArray = starshipResponse.getAsJsonArray("results");

                                for (JsonElement starshipElement : starshipArray) {
                                    if (starshipElement.isJsonObject()) {
                                        JsonObject currentStarship = starshipElement.getAsJsonObject();
                                        int starshipCode = getCodeFromUrl(getStringOrNull(currentStarship, "url"));

                                        System.out.println("Processing starship: " + starshipCode);

                                        // Verificar si la nave espacial existe
                                        if (!starshipExists(con, starshipCode)) {
                                            System.out.println("La nave espacial con código " + starshipCode + " no existe. Deteniendo el bucle.");
                                            return;  // Esto detendrá la ejecución del método
                                        }

                                        JsonArray peopleArray = currentStarship.getAsJsonArray("pilots");

                                        for (JsonElement personElement : peopleArray) {
                                            if (personElement.isJsonPrimitive()) {
                                                String personUrl = personElement.getAsString();
                                                int peopleCode = getCodeFromUrl(personUrl);

                                                System.out.println("Processing person: " + peopleCode);

                                                // Insertar relación en starship_people
                                                insertStarshipPeople(con, starshipCode, peopleCode);
                                            }
                                        }
                                    }
                                }

                                hasMoreData = hasMorePages(starshipResponse);
                                currentPage++;
                            } else {
                                System.out.println("No se encontró el campo 'results' en la respuesta de naves espaciales.");
                                hasMoreData = false;
                            }
                        } else {
                            System.out.println("Response Content:");
                            if (starshipData != null) {
                                System.out.println(starshipData.getAsJsonPrimitive().getAsString());
                            } else {
                                System.out.println("No data received");
                            }
                            hasMoreData = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.exit(1); // Detener la ejecución en caso de otro tipo de error
                    }
                }
            }

            // Resto del código (starshipExists y insertStarshipPeople) permanece igual

        // Método para verificar si la nave espacial existe
        private static boolean starshipExists(Connection con, int starshipCode) {
            try {
                String query = "SELECT 1 FROM starships WHERE codigo = ?";
                try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                    preparedStatement.setInt(1, starshipCode);
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        return resultSet.next();  // Devuelve true si hay al menos una fila, es decir, la nave existe
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al verificar la existencia de la nave espacial con código " + starshipCode + ": " + e.getMessage());
                return false;
            }
        }

        // Método para insertar en la tabla starship_people
        private static void insertStarshipPeople(Connection con, int starshipCode, int peopleCode) {
            try {
                String insertStarshipPeopleSql = "INSERT INTO starship_people(codigo_starship, codigo_people) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = con.prepareStatement(insertStarshipPeopleSql)) {
                    preparedStatement.setInt(1, starshipCode);
                    preparedStatement.setInt(2, peopleCode);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Relación exitosa entre la nave espacial con código " + starshipCode +
                                " y la persona con código " + peopleCode);
                    } else {
                        System.out.println("No se insertaron filas para la relación entre la nave espacial con código " + starshipCode +
                                " y la persona con código " + peopleCode);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al insertar relación entre la nave espacial con código " + starshipCode +
                        " y la persona con código " + peopleCode + ": " + e.getMessage());
            }
        }

        public static void loadVehiclePeople(Connection con) throws SQLException {
            int currentPage = 1;
            boolean hasMoreData = true;

            while (hasMoreData) {
                String vehiclesUrl = SWAPI_URL + "/api/vehicles/?format=json&page=" + currentPage;
                JsonElement vehiclesData = fetchDataFromApi(vehiclesUrl);

                if (vehiclesData != null && vehiclesData.isJsonObject()) {
                    JsonObject vehiclesObject = vehiclesData.getAsJsonObject();
                    JsonArray vehiclesArray = vehiclesObject.getAsJsonArray("results");

                    for (JsonElement vehicleElement : vehiclesArray) {
                        if (vehicleElement.isJsonObject()) {
                            JsonObject vehicleObject = vehicleElement.getAsJsonObject();
                            String vehicleUrl = getStringOrNull(vehicleObject, "url");
                            int vehicleCode = getCodeFromUrl(vehicleUrl);

                            System.out.println("Processing vehicle: " + vehicleCode);

                            JsonArray peopleArray = vehicleObject.getAsJsonArray("pilots");

                            for (JsonElement personElement : peopleArray) {
                                if (personElement.isJsonPrimitive()) {
                                    String personUrl = personElement.getAsString();
                                    int peopleCode = getCodeFromUrl(personUrl);

                                    System.out.println("Processing person: " + peopleCode);

                                    if (doesPersonExist(con, peopleCode)) {
                                        insertVehiclePeople(con, vehicleCode, peopleCode);
                                    }
                                }
                            }
                        }
                    }

                    hasMoreData = hasMorePages(vehiclesObject);
                    currentPage++;
                } else {
                    System.out.println("Response Content:");
                    if (vehiclesData != null) {
                        System.out.println(vehiclesData.getAsJsonPrimitive().getAsString());
                    } else {
                        System.out.println("No data received");
                    }
                    hasMoreData = false;
                }
            }
        }
        private static void insertVehiclePeople(Connection con, int vehicleCode, int peopleCode) {
            try {
                // Verificar si el vehículo existe
                if (!doesVehicleExist(con, vehicleCode)) {
                    System.out.println("El vehículo con código " + vehicleCode + " no existe en la tabla 'vehicles'. No se puede insertar la relación.");
                    return;
                }

                String insertVehiclePeopleSql = "INSERT INTO vehicle_people(codigo_vehicle, codigo_people) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = con.prepareStatement(insertVehiclePeopleSql)) {
                    preparedStatement.setInt(1, vehicleCode);
                    preparedStatement.setInt(2, peopleCode);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Relación exitosa entre el vehículo con código " + vehicleCode +
                                " y la persona con código " + peopleCode);
                    } else {
                        System.out.println("No se insertaron filas para la relación entre el vehículo con código " + vehicleCode +
                                " y la persona con código " + peopleCode);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al insertar relación entre el vehículo con código " + vehicleCode +
                        " y la persona con código " + peopleCode + ": " + e.getMessage());
            }
        }

        private static boolean doesVehicleExist(Connection con, int vehicleCode) throws SQLException {
            String checkVehicleSql = "SELECT 1 FROM vehicles WHERE codigo = ?";
            try (PreparedStatement checkVehicleStatement = con.prepareStatement(checkVehicleSql)) {
                checkVehicleStatement.setInt(1, vehicleCode);
                try (ResultSet resultSet = checkVehicleStatement.executeQuery()) {
                    return resultSet.next(); // Retorna true si el vehículo existe, false de lo contrario
                }
            }
        }

        public static void loadFilmsPlanets(Connection con) {
            int currentPage = 1;
            boolean hasMoreData = true;

            while (hasMoreData) {
                try {
                    String url = SWAPI_URL + "/api/films/?format=json&page=" + currentPage;
                    JsonElement filmsData = fetchDataFromApi(url);

                    if (filmsData != null && filmsData.isJsonObject()) {
                        JsonObject filmsObject = filmsData.getAsJsonObject();

                        if (filmsObject.has("results")) {
                            JsonArray filmsArray = filmsObject.getAsJsonArray("results");

                            for (JsonElement filmElement : filmsArray) {
                                if (filmElement.isJsonObject()) {
                                    JsonObject currentFilm = filmElement.getAsJsonObject();
                                    String filmUrl = getStringOrNull(currentFilm, "url");
                                    int filmCode = getCodeFromUrl(filmUrl);

                                    System.out.println("Processing film: " + filmCode);

                                    JsonArray planetsArray = currentFilm.getAsJsonArray("planets");

                                    for (JsonElement planetElement : planetsArray) {
                                        if (planetElement.isJsonPrimitive()) {
                                            String planetUrl = planetElement.getAsString();
                                            int planetCode = getCodeFromUrl(planetUrl);

                                            System.out.println("Processing planet: " + planetCode);

                                            insertFilmsPlanets(con, filmCode, planetCode);
                                        }
                                    }
                                }
                            }

                            hasMoreData = hasMorePages(filmsObject);
                            currentPage++;
                        } else {
                            System.out.println("No se encontró el campo 'results' en la respuesta de películas.");
                            hasMoreData = false;
                        }
                    } else {
                        System.out.println("Response Content:");
                        if (filmsData != null) {
                            System.out.println(filmsData.getAsJsonPrimitive().getAsString());
                        } else {
                            System.out.println("No data received");
                        }
                        hasMoreData = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1); // Detener la ejecución en caso de otro tipo de error
                }
            }
        }

        // Método para insertar en la tabla films_planets
        private static void insertFilmsPlanets(Connection con, int filmCode, int planetCode) {
            try {
                // Verificar si la película y el planeta existen
                if (!doesFilmExist(con, filmCode) || !doesPlanetExist(con, planetCode)) {
                    System.out.println("La película con código " + filmCode + " o el planeta con código " + planetCode +
                            " no existe. No se puede insertar la relación.");
                    return;
                }

                String insertFilmsPlanetsSql = "INSERT INTO films_planets(codigo_film, codigo_planet) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = con.prepareStatement(insertFilmsPlanetsSql)) {
                    preparedStatement.setInt(1, filmCode);
                    preparedStatement.setInt(2, planetCode);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Relación exitosa entre la película con código " + filmCode +
                                " y el planeta con código " + planetCode);
                    } else {
                        System.out.println("No se insertaron filas para la relación entre la película con código " + filmCode +
                                " y el planeta con código " + planetCode);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Error al insertar relación entre la película con código " + filmCode +
                        " y el planeta con código " + planetCode + ": " + e.getMessage());
            }
        }

        // Método para verificar si una película existe
        private static boolean doesFilmExist(Connection con, int filmCode) throws SQLException {
            String checkFilmSql = "SELECT 1 FROM films WHERE codigo = ?";
            try (PreparedStatement checkFilmStatement = con.prepareStatement(checkFilmSql)) {
                checkFilmStatement.setInt(1, filmCode);
                try (ResultSet resultSet = checkFilmStatement.executeQuery()) {
                    return resultSet.next(); // Retorna true si la película existe, false de lo contrario
                }
            }
        }

        // Método para verificar si un planeta existe
        private static boolean doesPlanetExist(Connection con, int planetCode) throws SQLException {
            String checkPlanetSql = "SELECT 1 FROM planets WHERE codigo = ?";
            try (PreparedStatement checkPlanetStatement = con.prepareStatement(checkPlanetSql)) {
                checkPlanetStatement.setInt(1, planetCode);
                try (ResultSet resultSet = checkPlanetStatement.executeQuery()) {
                    return resultSet.next(); // Retorna true si el planeta existe, false de lo contrario
                }
            }
        }
}
