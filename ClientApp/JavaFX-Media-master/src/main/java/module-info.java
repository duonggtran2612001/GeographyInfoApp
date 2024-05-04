module com.kensoftph.javafxmedia {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires org.json;
    requires json.simple;
    requires cipherizy.lib;
    requires org.jsoup;


    opens com.kensoftph.javafxmedia to javafx.fxml;
    exports com.kensoftph.javafxmedia;
}