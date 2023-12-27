package com.example.cateringfx2;
import com.example.cateringfx2.utils.MessageUtils;

import com.example.cateringfx2.model.Aliment;
import com.example.cateringfx2.utils.FileUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class NewAliment {
    @FXML
    private Button idSaveNA;
    @FXML
    private TextField idNameNA;
    @FXML
    private TextField idDescriptionNA;
    @FXML
    private TextField idFrecuencyNA;
    @FXML
    private TextField idCaloriesNA;
    @FXML
    private TextField idCarbohydratesNA;
    @FXML
    private TextField idFatNA;
    @FXML
    private CheckBox idMilk;
    @FXML
    private CheckBox idNuts;
    @FXML
    private CheckBox idEgg;
    @FXML
    private CheckBox idGluten;

    /**
     *  This method is used to save the new aliment in the file aliments.txt
     * @param event
     * @throws IOException
     */
    @FXML
    void save_guardar(ActionEvent event) throws IOException {
        String name = idNameNA.getText();
        String description = idDescriptionNA.getText();
        String frequency = idFrecuencyNA.getText();
        String calories = idCaloriesNA.getText();
        String carbohydrates = idCarbohydratesNA.getText();
        String fat = idFatNA.getText();
        boolean Milk= idMilk.isSelected();
        boolean Nuts= idNuts.isSelected();
        boolean Egg= idEgg.isSelected();
        boolean Gluten= idGluten.isSelected();
        if (name.isEmpty() || description.isEmpty() || frequency.isEmpty()|| calories.isEmpty()|| carbohydrates.isEmpty()|| fat.isEmpty()) {
            MessageUtils.ShowError("Ingrese datos", "Campos vacíos");
        } else {
            try{
                double caloriesValue = Double.parseDouble(calories);
                double carbohydratesValue = Double.parseDouble(carbohydrates);
                double fatValue = Double.parseDouble(fat);
            Aliment newAliment = new Aliment(name, description, frequency, Double.parseDouble(calories), Double.parseDouble(fat), Double.parseDouble(carbohydrates), Nuts, Milk, Egg, Gluten);
            FileUtils.storeAliment(newAliment, "aliments.txt");
            MessageUtils.ShowMessage("Exito", "Datos Añadidos.");
        } catch (NumberFormatException e) {
            MessageUtils.ShowError("Por favor, ingrese números válidos en los campos de calorías, carbohidratos y grasa.", "Error de validación");
        }
        }
        idNameNA.clear();
        idDescriptionNA.clear();
        idFrecuencyNA.clear();
        idCaloriesNA.clear();
        idCarbohydratesNA.clear();
        idFatNA.clear();
        idMilk.setSelected(false);
        idNuts.setSelected(false);
        idEgg.setSelected(false);
        idGluten.setSelected(false);

    }
    @FXML
    void volver(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Guardar.fxml"));
        Parent NewAliments = loader.load();
        Scene scene = new Scene(NewAliments);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }
}
