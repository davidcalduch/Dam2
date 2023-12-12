module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.CateringFX to javafx.fxml;
    exports com.example.CateringFX;
}