module com.creditscoreapp.creditscore {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires weka.stable;
    requires java.sql;

    opens com.creditscoreapp.creditscore to javafx.fxml;
    exports com.creditscoreapp.creditscore;
}