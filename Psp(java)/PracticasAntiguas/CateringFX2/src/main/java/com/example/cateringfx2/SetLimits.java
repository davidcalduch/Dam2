package com.example.cateringfx2;

import com.example.cateringfx2.utils.MessageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * This class is used to set the limits of the calories, carbohydrates and fat
 */
public class SetLimits {
    @FXML
    private Button btnSave;
    @FXML
    private TextField caloriesid;
    @FXML
    private TextField idCarbohydatesSL;
    @FXML
    private TextField idFatSeT;
    private List<Double> nutritionalInfoList = new ArrayList<>();
    private Double calLimit;
    private Double carbLimit;
    private Double fatLimit;

    public Double getCalLimit() {
        return calLimit;
    }

    public Double getCarbLimit() {
        return carbLimit;
    }

    public Double getFatLimit() {
        return fatLimit;
    }

    @FXML
    private Button saveButton;

    @FXML
    void guardar_save(ActionEvent event) throws IOException {
        String caloriesText = caloriesid.getText();
        String carbohydratesText = idCarbohydatesSL.getText();
        String fatText = idFatSeT.getText();

        if (caloriesText.isEmpty() || carbohydratesText.isEmpty() || fatText.isEmpty()) {
            MessageUtils.ShowError("Por favor, ingrese valores en todos los campos.", "Campos vacíos");
        } else {
            double Calories = Double.parseDouble(caloriesText);
            double Carbohydrates = Double.parseDouble(carbohydratesText);
            double Fat = Double.parseDouble(fatText);

            if (Calories < 0 || Carbohydrates < 0 || Fat < 0) {
                MessageUtils.ShowError("Este es un mensaje de error", "Título del Error");
            } else {
                nutritionalInfoList.add(Calories);
                nutritionalInfoList.add(Carbohydrates);
                nutritionalInfoList.add(Fat);

                // Establecer los límites
                calLimit = Calories;
                carbLimit = Carbohydrates;
                fatLimit = Fat;

                System.out.println(nutritionalInfoList);

                // Cargar la vista "Guardar.fxml"
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Guardar.fxml"));
                Parent Guardar = loader.load();
                Scene scene = new Scene(Guardar);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
            }
        }
    }
}
