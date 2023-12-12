package com.example.cateringfx2;

import com.example.cateringfx2.model.MenuElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.example.cateringfx2.model.Menu;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import static com.example.cateringfx2.utils.FileUtils.loadElements;

public class HelloController implements Initializable {
    private List<Menu> menus = new ArrayList<>();
    private double caloriesLimit = 0;
    private double carbohydratesLimit = 0;
    private double fatLimit = 0;

    @FXML
    private Button idNewAliments;
    @FXML
    private Button idSetLimits;
    @FXML
    private TableView<MenuElement> idTable2;
    @FXML
    private TableView<MenuElement> idTabla1;
    @FXML
    private TableColumn<MenuElement, String> idTableName1;
    @FXML
    private TableColumn<MenuElement, Double> idCalories1;
    @FXML
    private TableColumn<MenuElement, Double> idCarboTable1;
    @FXML
    private TableColumn<MenuElement, Double> idFatTable1;
    @FXML
    private TableColumn<MenuElement, String> idDescrptionTable2;
    @FXML
    private TableColumn<MenuElement, String> idNameTable2;
    @FXML
    private Label NVCalorias;
    @FXML
    private Label IdCarbohydrates;
    @FXML
    private Label idFat;
    @FXML
    private Button idQuitar;
    @FXML
    private CheckBox idMilk;
    @FXML
    private CheckBox idNuts;
    @FXML
    private CheckBox idEgg;
    @FXML
    private CheckBox idGluten;
    @FXML
    private CheckBox idMilk2;
    @FXML
    private CheckBox idNuts2;
    @FXML
    private CheckBox idEgg2;
    @FXML
    private CheckBox idGluten2;
    @FXML
    private Button idSaveMenu;
    @FXML
    private DatePicker idDate;
    @FXML
    private TextField idBuscador;
    /**
     * Lista de descripciones de los elementos del menú
     * Lo del save menu esta un poco chungo
     */
    private List<String> elementDescriptions = new ArrayList<>();
    @FXML
    void abrir_alimentos(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("NewAliment.fxml"));
        Parent NewAliments = loader.load();
        Scene scene = new Scene(NewAliments);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    @FXML
    void abrir_rec(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SetLimits.fxml"));
        Parent SetLimits = loader.load();
        Scene scene = new Scene(SetLimits);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idMilk.setOnAction(event -> handleCheckBox(idMilk));
        idNuts.setOnAction(event -> handleCheckBox(idNuts));
        idEgg.setOnAction(event -> handleCheckBox(idEgg));
        idGluten.setOnAction(event -> handleCheckBox(idGluten));
        List<MenuElement> elements = loadElements();
        System.out.println("Número de elementos cargados: " + elements.size());

        idTableName1.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCalories1.setCellValueFactory(new PropertyValueFactory<>("calories"));
        idCarboTable1.setCellValueFactory(new PropertyValueFactory<>("carbohydrates"));
        idFatTable1.setCellValueFactory(new PropertyValueFactory<>("fat"));

        ObservableList<MenuElement> allElements = FXCollections.observableArrayList(elements);
        FilteredList<MenuElement> filteredElements = new FilteredList<>(allElements, p -> true);
        idTabla1.setItems(filteredElements);

        idBuscador.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredElements.setPredicate(element -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return element.getName().toLowerCase().contains(lowerCaseFilter); // Filtra por nombre
            });
        });
    }
    @FXML
    public void on_click(ActionEvent actionEvent) {
        ObservableList<MenuElement> selectedElements = idTabla1.getSelectionModel().getSelectedItems();
        idTable2.getItems().addAll(selectedElements);
        idNameTable2.setCellValueFactory(new PropertyValueFactory<>("name"));
        idDescrptionTable2.setCellValueFactory(new PropertyValueFactory<>("description"));
        double totalCalories = 0.0;
        double totalCarbohydrates = 0.0;
        double totalFat = 0.0;
        boolean commonMilk = true;
        boolean commonNuts = true;
        boolean commonEggs = true;
        boolean commonGluten = true;

        for (MenuElement element : selectedElements) {
            commonMilk = commonMilk && element.hasMilk();
            commonNuts = commonNuts && element.hasNuts();
            commonEggs = commonEggs && element.hasEggs();
            commonGluten = commonGluten && element.hasGluten();
        }

        idMilk2.setSelected(commonMilk);
        idNuts2.setSelected(commonNuts);
        idEgg2.setSelected(commonEggs);
        idGluten2.setSelected(commonGluten);

        ObservableList<MenuElement> elementsInTable2 = idTable2.getItems();
        for (MenuElement element : elementsInTable2) {
            totalCalories += element.getCalories();
            totalCarbohydrates += element.getCarbohydrates();
            totalFat += element.getFat();
        }
        NVCalorias.setText("" + totalCalories);
        IdCarbohydrates.setText("" + totalCarbohydrates);
        idFat.setText("" + totalFat);

        // Comprobar si los valores superan los límites y cambiar el color de las etiquetas
        if (totalCalories > caloriesLimit) {
            NVCalorias.setTextFill(Color.RED);
        }
        if (totalCarbohydrates > carbohydratesLimit) {
            IdCarbohydrates.setTextFill(Color.RED);
        }
        if (totalFat > fatLimit) {
            idFat.setTextFill(Color.RED);
        }
    }

    @FXML
    public void quitar(ActionEvent actionEvent) {
        ObservableList<MenuElement> selectedElements = idTable2.getSelectionModel().getSelectedItems();
        idTable2.getItems().removeAll(selectedElements);
    }

    @FXML
    public void applyFilters(ActionEvent actionEvent) {
        List<MenuElement> elements = loadElements();
        List<MenuElement> filteredElements = new ArrayList<>();

        for (MenuElement element : elements) {
            boolean includeElement = true;

            if (idMilk.isSelected() && !element.hasMilk()) {
                includeElement = false;
            }
            if (idNuts.isSelected() && !element.hasNuts()) {
                includeElement = false;
            }
            if (idEgg.isSelected() && !element.hasEggs()) {
                includeElement = false;
            }
            if (idGluten.isSelected() && !element.hasGluten()) {
                includeElement = false;
            }
            if (includeElement) {
                filteredElements.add(element);
            }
        }
        idTabla1.setItems(FXCollections.observableArrayList(filteredElements));
    }
    @FXML
    public void guardar_menu(ActionEvent actionEvent) throws IOException {
        LocalDate selectedDate = idDate.getValue();
        Menu existingMenu = findMenuByDate(selectedDate);

        if (existingMenu != null) {
            // Mostrar un mensaje de error aquí
            System.out.println("Ya existe un menú para esta fecha.");
            return;
        }

        Menu currentMenu = new Menu(selectedDate, new ArrayList<>());
        menus.add(currentMenu);

        List<MenuElement> currentElements = currentMenu.getElements();
        currentElements.clear();
        currentElements.addAll(idTable2.getItems());

        try {
            String formattedMenu = formatMenu(currentMenu);

            if (!formattedMenu.isEmpty()) {
                Files.write(Paths.get("menu.txt"), Collections.singleton(formattedMenu), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }

            System.out.println("Menú guardado en el archivo.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean isValidNumber(String text) {
        return text.matches("-?\\d+(\\.\\d+)?");
    }
    private Menu findMenuByDate(LocalDate date) {
        for (Menu menu : menus) {
            LocalDate menuDate = menu.getDate();
            if (menuDate != null && menuDate.equals(date)) {
                return menu;
            }
        }
        return null;
    }
    private void handleCheckBox(CheckBox checkBox) {
        if (checkBox.isSelected()) {
            idMilk.setSelected(false);
            idNuts.setSelected(false);
            idEgg.setSelected(false);
            idGluten.setSelected(false);
            checkBox.setSelected(true);
        }
    }
    private String formatMenu(Menu menu) {
        StringBuilder formattedMenu = new StringBuilder();
        LocalDate date = menu.getDate();

        if (date != null) {
            formattedMenu.append(date.toString());
            formattedMenu.append(';');
            for (MenuElement element : menu.getElements()) {
                formattedMenu.append(element.getName());
                formattedMenu.append(" or dishes"); // Modifica esto según tus necesidades
                formattedMenu.append(';');
            }
        }
        return formattedMenu.toString();
    }

}
