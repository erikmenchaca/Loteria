module loteria {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires transitive javafx.graphics;
    exports loteria.view;
    exports loteria.model;
}