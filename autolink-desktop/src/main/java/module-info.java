module balloumi {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.swing;
    requires java.sql;

    requires org.apache.pdfbox;
    requires jbcrypt;
    requires mysql.connector.j;
    requires twilio;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires firebase.admin;

    opens controllers to javafx.fxml;
    opens models to javafx.base;
    opens utils to javafx.base;
    opens services to javafx.base;
    opens Enum to javafx.base;

    exports controllers;
    exports models;
    exports utils;
    exports services;
    exports Enum;
    exports org.example;
}
