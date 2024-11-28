package simuladorrestaurant.ui;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.util.Duration;

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
        FXGL.getGameWorld().addEntityFactory(entityFactory);

        // Automatizar el spawn de comensales cada 5 segundos
        FXGL.getGameTimer().runAtInterval(() -> {
            System.out.println("Generando un nuevo comensal automáticamente");
            spawnComensal();
        }, Duration.seconds(5));

        // Crear mesero y cocinero iniciales
        Entity mesero = FXGL.spawn("mesero", 100, 100);
        Entity cocinero = FXGL.spawn("cocinero", 200, 100);
    }

    @Override
    protected void initUI() {
        // Mostrar información sobre el estado del restaurante
        Text textoEstado = new Text("Comensales en espera: " + comensalesEnEspera);
        textoEstado.setFill(Color.WHITE);
        textoEstado.setX(10);
        textoEstado.setY(20);
        FXGL.getGameScene().addUINode(textoEstado);

        FXGL.getGameTimer().runAtInterval(() -> {
            textoEstado.setText("Comensales en espera: " + comensalesEnEspera);
        }, Duration.seconds(0.5));
    }

    private void spawnComensal() {
        comensalesEnEspera++;

        // Crear el comensal usando spawn
        Entity comensal = FXGL.spawn("comensal", 300, 150 + (comensalesEnEspera * 30));

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
        FXGL.getGameTimer().runOnceAfter(() -> {
            if ("sentado".equals(comensal.getString("estado"))) {
                atenderComensal(comensal);
            }
        }, Duration.seconds(2));
    }

    private void atenderComensal(Entity comensal) {
        comensal.setProperty("estado", "siendo atendido");
        System.out.println("Mesero atendiendo al comensal");

        FXGL.getGameTimer().runOnceAfter(() -> {
            comensal.setProperty("estado", "comiendo");
            System.out.println("Comensal comenzando a comer");

            terminarComida(comensal);
        }, Duration.seconds(3));
    }

    private void terminarComida(Entity comensal) {
        comensalesEnEspera--;
        mesasDisponibles++;
        System.out.println("Comensal terminó, mesa liberada");
        comensal.removeFromWorld();
    }

    public static void main(String[] args) {
        launch(args);
    }
}