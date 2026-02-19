module ar.edu.ubp.sia.optimizaciondrones {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens ar.edu.ubp.sia.optimizaciondrones to javafx.fxml;
    exports ar.edu.ubp.sia.optimizaciondrones;
}