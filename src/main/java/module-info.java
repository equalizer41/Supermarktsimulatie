module supermarket.simulator.supermarketsimulator {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens supermarket.simulator to javafx.fxml;
    exports supermarket.simulator;
    exports supermarket.simulator.controller;
    opens supermarket.simulator.controller to javafx.fxml;
}