module com.example.cateringfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cateringfx to javafx.fxml;
    exports com.example.cateringfx;
}