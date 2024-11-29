package simuladorrestaurant.logica;

import simuladorrestaurant.actores.Mesero;
import simuladorrestaurant.actores.Cocinero;
import simuladorrestaurant.actores.Comensal;
import simuladorrestaurant.concurrencia.*;
import simuladorrestaurant.ui.RestaurantEntityFactory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Restaurant {
    private final MonitorMesas monitorMesas;
    private final MonitorCocina monitorCocina;
    private final Buffer bufferOrdenes;
    private final BufferComida bufferComida;

    private final List<Comensal> comensales;
    private final List<Mesero> meseros;
    private final List<Cocinero> cocineros;

    private final Random random = new Random();

    public Restaurant(int numMeseros, int numCocineros, int numMesas) {
        this.monitorMesas = new MonitorMesas(numMesas);
        this.monitorCocina = new MonitorCocina();
        this.bufferOrdenes = new Buffer(10);
        this.bufferComida = new BufferComida(5);

        this.comensales = new ArrayList<>();
        this.meseros = new ArrayList<>();
        this.cocineros = new ArrayList<>();

        // Crear meseros
        for (int i = 0; i < numMeseros; i++) {
            Entity meseroEntity = new RestaurantEntityFactory().createMesero(
                    100 + (i * 50), // Posición x variada
                    100  // Posición y fija
            );
            Mesero mesero = new Mesero(
                    bufferOrdenes,
                    bufferComida,
                    monitorMesas,
                    monitorCocina,
                    meseroEntity
            );
            this.meseros.add(mesero);

            // Agregar entidad al mundo de juego
            Platform.runLater(() -> FXGL.getGameWorld().addEntity(meseroEntity));
        }

        // Crear cocineros
        for (int i = 0; i < numCocineros; i++) {
            Entity cocineroEntity = new RestaurantEntityFactory().createCocinero(
                    200 + (i * 50), // Posición x variada
                    100  // Posición y fija
            );
            Cocinero cocinero = new Cocinero(
                    bufferOrdenes,
                    bufferComida,
                    monitorCocina,
                    cocineroEntity
            );
            this.cocineros.add(cocinero);

            // Agregar entidad al mundo de juego
            Platform.runLater(() -> FXGL.getGameWorld().addEntity(cocineroEntity));
        }
    }

    public void iniciarSimulacion() {
        // Iniciar hilos de meseros y cocineros
        meseros.forEach(Thread::start);
        cocineros.forEach(Thread::start);
    }

    public void llegarComensales() {
        new Thread(() -> {
            while (true) {
                try {
                    // Nombre único para cada comensal
                    String nombreComensal = "Comensal " + (comensales.size() + 1);

                    // Crear entidad de comensal con posición inicial
                    Entity comensalEntity = new RestaurantEntityFactory().createComensal(
                            300 + random.nextInt(200), // Posición x aleatoria
                            150 + (comensales.size() * 50) // Posición y incremental
                    );

                    // Agregar al mundo de juego
                    Platform.runLater(() -> FXGL.getGameWorld().addEntity(comensalEntity));

                    // Crear instancia de Comensal
                    Comensal comensal = new Comensal(
                            monitorMesas,
                            bufferOrdenes,
                            bufferComida,
                            nombreComensal,
                            comensalEntity
                    );

                    // Añadir a la lista y comenzar
                    comensales.add(comensal);
                    comensal.start();

                    // Tiempo aleatorio entre llegada de comensales
                    Thread.sleep(random.nextInt(5000) + 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    // Método para obtener la cantidad de comensales en espera
    public int getComensalesEnEspera() {
        return (int) comensales.stream()
                .filter(c -> c.getEstadoActual().equals("Buscando mesa"))
                .count();
    }

    // Método para obtener la cantidad de mesas disponibles
    public int getMesasDisponibles() {
        return monitorMesas.getMesasDisponibles();
    }
}
