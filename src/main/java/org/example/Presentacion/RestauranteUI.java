package org.example.Presentacion;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.entity.Entity;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;

public class RestauranteUI extends GameApplication {

    private int comensalesEnEspera = 0;
    private int mesasDisponibles = 10;

    public static void main(String[] args) {
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
        // Crear entidades para los actores del restaurante (comensales, meseros, cocineros)
        spawnComensal();
        spawnMesero();
        spawnCocinero();
    }

    @Override
    protected void initInput() {
        // Aquí puedes mapear las teclas para interactuar con los actores (por ejemplo, mover comensales, meseros, etc.)
  getInput().addAction(() -> {
            System.out.println("Comensal llega al restaurante");
            spawnComensal();
        }, KeyCode.SPACE);
    }

    @Override
    protected void initUI() {
        // Aquí se muestra información sobre el estado del restaurante
        Text textoEstado = new Text("Comensales en espera: " + comensalesEnEspera);
        textoEstado.setFill(Color.WHITE);
        textoEstado.setX(10);
        textoEstado.setY(20);
        getGameScene().addUINode(textoEstado);
    }

    // Método para crear comensales
    private void spawnComensal() {
        comensalesEnEspera++;

        Entity comensal = entityBuilder()
                .at(100, 100 + (comensalesEnEspera * 30))  // Ubicación basada en el número de comensales
                .viewWithBBox("comensal.png")  // Imagen para comensal (deberías tener una imagen)
                .buildAndAttach();

        // Si hay mesas disponibles, el comensal se sienta. De lo contrario, espera
        if (mesasDisponibles > 0) {
            mesasDisponibles--;
            comensal.setProperty("estado", "sentado");
            System.out.println("Comensal sentado, mesas disponibles: " + mesasDisponibles);
        } else {
            comensal.setProperty("estado", "esperando");
            System.out.println("Comensal esperando, mesas disponibles: " + mesasDisponibles);
        }

        // Simulamos que el mesero atiende al comensal después de un tiempo
        getGameTimer().runOnceAfter(() -> {
            if (comensal.getProperty("estado").equals("sentado")) {
                atenderComensal(comensal);
            }
        }, 2);  // Tiempo de espera antes de atender (2 segundos)
    }

    // Método para simular que el mesero atiende al comensal
    private void atenderComensal(Entity comensal) {
        comensal.setProperty("estado", "siendo atendido");
        System.out.println("Mesero atendiendo al comensal");

        // Simula la entrega de la comida después de un tiempo
        getGameTimer().runOnceAfter(() -> {
            comensal.setProperty("estado", "comiendo");
            System.out.println("Comensal comenzando a comer");
            terminarComida(comensal);
        }, 3);  // Tiempo de servicio (3 segundos)
    }

    // Método para simular que el comensal termina su comida
    private void terminarComida(Entity comensal) {
        comensalesEnEspera--;
        mesasDisponibles++;
        System.out.println("Comensal terminó, mesa liberada");
        comensal.removeFromWorld();  // El comensal sale del restaurante
    }

    // Método para crear meseros
    private void spawnMesero() {
        Entity mesero = entityBuilder()
                .at(200, 100)
                .viewWithBBox("mesero.png")  // Imagen para mesero
                .buildAndAttach();
        System.out.println("Mesero listo para atender");
    }

    // Método para crear cocineros
    private void spawnCocinero() {
        Entity cocinero = entityBuilder()
                .at(300, 100)
                .viewWithBBox("cocinero.png")  // Imagen para cocinero
                .buildAndAttach();
        System.out.println("Cocinero listo para cocinar");
    }
}
