module org.uav.prezentui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;

    opens org.uav.prezentui to javafx.fxml;
    exports org.uav.prezentui;
}