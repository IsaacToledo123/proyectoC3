package simuladorrestaurant.ui;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

public class RestaurantUI extends Application {

    @FXML
    private Pane fxglPane;

    @FXML
    private void onStartSimulation() {
        System.out.println("Simulación iniciada!");
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Cargar archivo FXML como base
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/main-view.fxml"));
        Scene scene = new Scene(loader.load());

        stage.setScene(scene);
        stage.setTitle("Simulador Restaurante con FXGL");
        stage.show();

        // Iniciar FXGL dentro del Pane
        FXGLApplication.startFXGL(fxglPane);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
