module loteria {
    // Required JavaFX modules
    requires javafx.controls;
    requires javafx.fxml;

    // Make your model package available to other modules
    exports loteria.model;

    // FIX: Export your view package so JavaFX can launch it
    exports loteria.view;

    // Open your view package to the FXML loader for reflection
    opens loteria.view to javafx.fxml;
}