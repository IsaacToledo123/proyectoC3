module simuladorrestaurant.simuladorrestaurant {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens simuladorrestaurant to javafx.fxml;
    exports simuladorrestaurant;
}