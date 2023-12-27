module com.example.cardclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cardclient to javafx.fxml;
    exports com.example.cardclient;
}