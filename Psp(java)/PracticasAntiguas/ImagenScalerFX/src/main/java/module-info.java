module com.example.imagenscalerfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.imagenscalerfx to javafx.fxml;
    exports com.example.imagenscalerfx;
}