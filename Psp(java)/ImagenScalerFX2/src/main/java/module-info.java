module com.example.imagenscalerfx2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.imagenscalerfx2 to javafx.fxml;
    exports com.example.imagenscalerfx2;
}