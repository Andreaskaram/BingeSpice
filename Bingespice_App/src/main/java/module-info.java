module com.example.bingespice_app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires java.prefs;
    requires java.sql;
    requires org.json;
    requires java.net.http;

    opens com.example.bingespice_app to javafx.fxml;
    exports com.example.bingespice_app;
}