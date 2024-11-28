module simuladorrestaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.almasb.fxgl.all;

    // Permitir que JavaFX acceda al paquete simuladorrestaurant
    opens simuladorrestaurant to javafx.fxml;

    // Exportar simuladorrestaurant.ui al módulo com.almasb.fxgl.core
    exports simuladorrestaurant.ui to com.almasb.fxgl.core;

    // Exportar otros paquetes si es necesario
    exports simuladorrestaurant;
}