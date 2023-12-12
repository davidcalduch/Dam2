package com.example.imagenscalerfx2;

import com.example.imagenscalerfx2.utils.IOUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class HelloController implements Initializable {
    private String folderPath = "C:\\Users\\dacal\\IdeaProjects\\ImagenScalerFX2\\images";
    @FXML
    private ListView<String> listaImagen;
    @FXML
    private Button btnStart;
    @FXML
    private Label labelStatus;
    @FXML
    private ListView<String> ListaRescalada;
    @FXML
    private ImageView imagen;
    private ScheduledService<Void> scheduledService;

    private ThreadPoolExecutor executorService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Crear subdirectorios al iniciar la aplicación
        createSubdirectories();

        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    listaImagen.getItems().add(file.getName());
                }
            }
        }



        listaImagen.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                loadScaledImages(newValue);
            }
        });

        ListaRescalada.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showImage(newValue);
            }
        });
        scheduledService = startScheduledService();

    }

    @FXML
    public void start() {
        String selectedItem = listaImagen.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            btnStart.setDisable(true);

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    String themeName = getThemeName(selectedItem);
                    String themeFolderPath = getFolderPath(themeName);

                    System.out.println("Ruta del directorio a eliminar: " + themeFolderPath);

                    try {
                        if (Files.exists(Paths.get(themeFolderPath))) {
                            IOUtils.deleteDirectory(Paths.get(themeFolderPath));
                            Files.createDirectory(Paths.get(themeFolderPath));
                            System.out.println("Directorio recreado con éxito: " + themeFolderPath);

                            for (int scaleFactor = 10; scaleFactor <= 90; scaleFactor += 10) {
                                String scaledImageName = scaleFactor + "_" + selectedItem;
                                String originalImagePath = folderPath + File.separator + selectedItem;
                                String scaledImagePath = themeFolderPath + File.separator + scaledImageName;

                                System.out.println("Intentando leer la imagen original desde: " + originalImagePath);

                                if (Files.exists(Paths.get(originalImagePath))) {
                                    try {
                                        Image originalImage = new Image(new File(originalImagePath).toURI().toURL().toString());
                                        IOUtils.resize(originalImagePath, scaledImagePath, scaleFactor);
                                        System.out.println("Imagen escalada al " + scaleFactor + "% y guardada como: " + scaledImageName);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        System.out.println("Error al leer o escalar la imagen: " + originalImagePath);
                                    }
                                } else {
                                    System.out.println("La imagen original no existe: " + originalImagePath);
                                }
                            }
                        } else {
                            System.out.println("El directorio no existe: " + themeFolderPath);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("No se pudo recrear el directorio: " + themeFolderPath);
                    }

                    return null;
                }
            };

            task.setOnSucceeded(event -> {
                Platform.runLater(() -> {
                    // Habilitar el botón después de que la tarea haya terminado
                    btnStart.setDisable(false);

                    // Llama a loadScaledImages desde el hilo de la interfaz de usuario
                    loadScaledImages(selectedItem);
                });
            });

            // Iniciar la tarea en un nuevo hilo
            new Thread(task).start();

            // Actualizar el estado en el hilo de la interfaz de usuario
            updateStatus();
        }
    }
    private String getThemeName(String imageName) {
        // Obtener el nombre del tema a partir del nombre de la imagen (por ejemplo, "wallpaper01.jpg" -> "wallpaper01")
        return imageName.replaceFirst("[.][^.]+$", "");
    }
    private String getFolderPath(String imageName) {
        return folderPath + File.separator + imageName;
    }
    private ScheduledService<Void> startScheduledService() {
        executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

        ScheduledService<Void> service = new ScheduledService<>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<>() {
                    @Override
                    protected Void call() {
                        try {
                            // Aproximación del número de hilos activos y completados
                            long activeThreads = executorService.getActiveCount();
                            long completedThreads = executorService.getCompletedTaskCount();
                            long totalThreads = executorService.getTaskCount();

                            Platform.runLater(() -> {
                                // Actualiza la etiqueta de estado inferior
                                labelStatus.setText("Active Threads: " + activeThreads + ", Completed Threads: " + completedThreads + " of " + totalThreads + " tasks.");
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return null;
                    }
                };
            }
        };

        service.setDelay(Duration.ZERO);
        service.setPeriod(Duration.seconds(1));
        service.start();

        return service;
    }



    private void loadScaledImages(String selectedImageName) {
        String themeName = getThemeName(selectedImageName);
        String themeFolderPath = getFolderPath(themeName);

        File themeFolder = new File(themeFolderPath);
        File[] listOfThemeFiles = themeFolder.listFiles();

        ObservableList<String> items = FXCollections.observableArrayList();

        if (listOfThemeFiles != null) {
            for (File file : listOfThemeFiles) {
                if (file.isFile()) {
                    items.add(file.getName());
                }
            }
        }
        // Utiliza Platform.runLater para actualizar la interfaz de usuario
        Platform.runLater(() -> {
            ListaRescalada.setItems(items);
            // Muestra la primera imagen en el ImageView si hay alguna
            if (!items.isEmpty()) {
                showImage(items.get(0));
            }
        });
    }
    private void showImage(String imageName) {
        String imagePath = getFolderPath(getThemeName(listaImagen.getSelectionModel().getSelectedItem())) + File.separator + imageName;
        Image image = new Image(new File(imagePath).toURI().toString());
        imagen.setImage(image);
    }
    private void createSubdirectories() {
        // Crear subdirectorios en la carpeta de imágenes al iniciar la aplicación
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    String subdirectoryPath = folderPath + File.separator + getThemeName(fileName);

                    try {
                        Files.createDirectories(Paths.get(subdirectoryPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private void updateStatus() {
        if (scheduledService != null) {
            scheduledService.restart();
        }
    }



}
