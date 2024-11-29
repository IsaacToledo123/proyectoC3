package simuladorrestaurant.ui;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import simuladorrestaurant.logica.Restaurant;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

public class RestaurantUI extends GameApplication {
    private Restaurant restaurant;
    private Text textoEstado;
    private int comensalesEnEspera = 0;
    private int mesasDisponibles = 10;

    public static void main(String[] args) {
        launch(args);  // Llamada correcta al método launch
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Simulador de Restaurante");
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame() {
        // Registrar la fábrica de entidades
        getGameWorld().addEntityFactory(new RestaurantEntityFactory());

        // Crear la instancia de Restaurant
        restaurant = new Restaurant(
                5,  // Número de meseros
                3,  // Número de cocineros
                10  // Número de mesas
        );

        // Iniciar la simulación
        restaurant.iniciarSimulacion();
        restaurant.llegarComensales();
    }

    @Override
    protected void initUI() {
        // Crear texto de estado
        textoEstado = new Text("Comensales en espera: 0");
        textoEstado.setFill(Color.WHITE);
        textoEstado.setX(10);
        textoEstado.setY(20);
        getGameScene().addUINode(textoEstado);

        // Actualizar texto de estado periódicamente
        getGameTimer().runAtInterval(() -> {
            comensalesEnEspera = restaurant.getComensalesEnEspera();
            mesasDisponibles = restaurant.getMesasDisponibles();

            textoEstado.setText(String.format(
                    "Comensales en espera: %d | Mesas disponibles: %d",
                    comensalesEnEspera,
                    mesasDisponibles
            ));
        }, Duration.seconds(0.5));
    }
}
