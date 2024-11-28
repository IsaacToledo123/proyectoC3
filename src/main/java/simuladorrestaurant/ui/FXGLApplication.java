package simuladorrestaurant.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import javafx.scene.layout.Pane;

public class FXGLApplication extends GameApplication {

    private static Pane fxglPane;

    public static void startFXGL(Pane pane) {
        fxglPane = pane;
        launch(FXGLApplication.class, new String[0]);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(600);
        settings.setHeight(400);
        settings.setTitle("Simulación FXGL");
        settings.setVersion("0.1");
        settings.setMainMenuEnabled(false);  // Opcional
    }

    @Override
    protected void initGame() {
        // Registra la fábrica de entidades
        FXGL.getGameWorld().addEntityFactory(new RestaurantEntityFactory());

        // Spawn de las entidades en posiciones específicas
        FXGL.spawn("mesero", 100, 100);
        FXGL.spawn("cocinero", 200, 100);
        FXGL.spawn("comensal", 300, 100);

        // Agregar el canvas de FXGL al contenedor
        fxglPane.getChildren().add(FXGL.getGameScene().getRoot());
    }

    public static void main(String[] args) {
        startFXGL(new Pane());
    }
}