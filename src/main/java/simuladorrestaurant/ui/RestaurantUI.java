package simuladorrestaurant.ui;

import simuladorrestaurant.ui.RestaurantEntityFactory;
import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGL.*;

public class RestaurantUI extends GameApplication {

    private int comensalesEnEspera = 0;
    private int mesasDisponibles = 10;

    // Instancia de la fábrica de entidades
    private RestaurantEntityFactory entityFactory = new RestaurantEntityFactory();

    public static void start(String[] args) {
        launch(args);
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
        // Registrar la fábrica de entidades para el juego
        getGameWorld().addEntityFactory(entityFactory);

        // Automatizar el spawn de comensales cada 5 segundos
        getGameTimer().runAtInterval(() -> {
            System.out.println("Generando un nuevo comensal automáticamente");
            spawnComensal();
        }, javafx.util.Duration.seconds(5));

        // Crear mesero y cocinero iniciales
        Entity mesero = entityFactory.createMesero(100, 100);
        Entity cocinero = entityFactory.createCocinero(200, 100);
        getGameWorld().addEntity(mesero);
        getGameWorld().addEntity(cocinero);
    }

    @Override
    protected void initUI() {
        // Mostrar información sobre el estado del restaurante
        Text textoEstado = new Text("Comensales en espera: " + comensalesEnEspera);
        textoEstado.setFill(Color.WHITE);
        textoEstado.setX(10);
        textoEstado.setY(20);
        getGameScene().addUINode(textoEstado);

        getGameTimer().runAtInterval(() -> {
            textoEstado.setText("Comensales en espera: " + comensalesEnEspera);
        }, javafx.util.Duration.seconds(0.5));
    }

    private void spawnComensal() {
        comensalesEnEspera++;

        // Crear el comensal usando la fábrica
        Entity comensal = entityFactory.createComensal(300, 150 + (comensalesEnEspera * 30));
        getGameWorld().addEntity(comensal);

        // Asignar estado y verificar disponibilidad de mesas
        if (mesasDisponibles > 0) {
            mesasDisponibles--;
            comensal.setProperty("estado", "sentado");
            System.out.println("Comensal sentado, mesas disponibles: " + mesasDisponibles);
        } else {
            comensal.setProperty("estado", "esperando");
            System.out.println("Comensal esperando, mesas disponibles: " + mesasDisponibles);
        }

        // Simular atención del comensal
        getGameTimer().runOnceAfter(() -> {
            if ("sentado".equals(comensal.getString("estado"))) {
                atenderComensal(comensal);
            }
        }, javafx.util.Duration.seconds(2));
    }

    private void atenderComensal(Entity comensal) {
        comensal.setProperty("estado", "siendo atendido");
        System.out.println("Mesero atendiendo al comensal");

        getGameTimer().runOnceAfter(() -> {
            comensal.setProperty("estado", "comiendo");
            System.out.println("Comensal comenzando a comer");

            terminarComida(comensal);
        }, javafx.util.Duration.seconds(3));
    }

    private void terminarComida(Entity comensal) {
        comensalesEnEspera--;
        mesasDisponibles++;
        System.out.println("Comensal terminó, mesa liberada");
        comensal.removeFromWorld();
    }
}
