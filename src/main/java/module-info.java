module loteria {
    // Required JavaFX modules
    requires javafx.controls;
    // No fxml needed since the GUI is programmatic
    // requires javafx.fxml;

    // Make your model and view packages available
    exports loteria.model;
    exports loteria.view;

    // This is not needed if you aren't using FXML
    // opens loteria.view to javafx.fxml;
}
