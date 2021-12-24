module tempnus {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens tempnus to javafx.fxml;
    exports tempnus;
    opens tempnus.ui to javafx.fxml;
    exports tempnus.ui;
}