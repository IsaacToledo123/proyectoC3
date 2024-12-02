package simuladorrestaurant.logica;

import com.almasb.fxgl.entity.SpawnData;
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

@SuppressWarnings("BusyWait")
public class Restaurant {
    private final MonitorMesas monitorMesas;
    private final Buffer bufferOrdenes;
    private final BufferComida bufferComida;

    private final List<Comensal> comensales;
    private final List<Mesero> meseros;
    private final List<Cocinero> cocineros;

    private RestaurantEntityFactory factory;

    private final Random random = new Random();

    public Restaurant(int numMeseros, int numCocineros, MonitorMesas monitorMesas) {
        this.factory = new RestaurantEntityFactory(monitorMesas);
        this.monitorMesas = monitorMesas;  // Asignar el monitorMesas pasado
        MonitorCocina monitorCocina = new MonitorCocina();
        this.bufferOrdenes = new Buffer(10);
        this.bufferComida = new BufferComida(5);

        this.comensales = new ArrayList<>();
        this.meseros = new ArrayList<>();
        this.cocineros = new ArrayList<>();

        for (int i = 0; i < numMeseros; i++) {
            SpawnData spawnData = new SpawnData(100 + (i * 50), 100); // Usar SpawnData
            Entity meseroEntity = new RestaurantEntityFactory(monitorMesas).createMesero(spawnData); // Pasar SpawnData
            Mesero mesero = new Mesero(
                    bufferOrdenes,
                    bufferComida,
                    monitorMesas,
                    monitorCocina,
                    meseroEntity
            );
            this.meseros.add(mesero);
            FXGL.getGameWorld().addEntity(meseroEntity);
        }

        for (int i = 0; i < numCocineros; i++) {
            SpawnData spawnData = new SpawnData(200 + (i * 50), 100);
            Entity cocineroEntity = new RestaurantEntityFactory(monitorMesas).createCocinero(spawnData);
            Cocinero cocinero = new Cocinero(
                    bufferOrdenes,
                    bufferComida,
                    monitorCocina,
                    cocineroEntity
            );
            this.cocineros.add(cocinero);
            FXGL.getGameWorld().addEntity(cocineroEntity);
        }
    }

    public void iniciarSimulacion() {
        meseros.forEach(Thread::start);
        cocineros.forEach(Thread::start);
    }

    public void llegarComensales() {
        new Thread(() -> {
            while (true) {
                try {
                    String nombreComensal = "Comensal " + (comensales.size() + 1);
                    SpawnData spawnData = new SpawnData(
                            300 + random.nextInt(200),
                            150 + (comensales.size() * 50)
                    );
                    Entity comensalEntity = new RestaurantEntityFactory(monitorMesas).createComensal(spawnData);
                    Platform.runLater(() -> FXGL.getGameWorld().addEntity(comensalEntity));
                    Comensal comensal = new Comensal(
                            monitorMesas, bufferOrdenes, bufferComida, nombreComensal, comensalEntity
                    );
                    comensales.add(comensal);
                    comensal.start();
                    monitorMesas.asignarMesa(comensal);
                    monitorMesas.notificar();
                    Thread.sleep(random.nextInt(5000) + 1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    public int getComensalesEnEspera() {
        return (int) comensales.stream()
                .filter(c -> c.getEstadoActual().equals("Buscando mesa"))
                .count();
    }

    public int getMesasDisponibles() {
        return monitorMesas.getMesasDisponibles(); // Se asegura de que refleje correctamente las mesas disponibles
    }

    public List<Comensal> getComensales() {
        return comensales;
    }
}
