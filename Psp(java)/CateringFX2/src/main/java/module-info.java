module com.example.cateringfx2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cateringfx2 to javafx.fxml;
    exports com.example.cateringfx2;
    exports com.example.cateringfx2.model;
    opens com.example.cateringfx2.model to javafx.fxml;
    exports com.example.cateringfx2.utils;
    opens com.example.cateringfx2.utils to javafx.fxml;
}