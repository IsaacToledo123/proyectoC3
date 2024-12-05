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
import simuladorrestaurant.util.EntidadManager;

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

    private final RestaurantEntityFactory factory;
    private final EntidadManager entidadManager;

    private final Random random = new Random();

    public Restaurant(int numMeseros, int numCocineros, MonitorMesas monitorMesas, EntidadManager entidadManager) {
        this.entidadManager = entidadManager; // Inicializar EntidadManager
        MonitorCocina monitorCocina = new MonitorCocina();
        Buffer bufferOrdenes = new Buffer(10);
        BufferComida bufferComida = new BufferComida(5);
        this.factory = new RestaurantEntityFactory(monitorMesas, monitorCocina, bufferOrdenes, bufferComida, entidadManager);
        this.monitorMesas = monitorMesas; // Asignar el monitorMesas pasado
        this.bufferOrdenes = bufferOrdenes;
        this.bufferComida = bufferComida;

        this.comensales = new ArrayList<>();
        this.meseros = new ArrayList<>();
        this.cocineros = new ArrayList<>();

        for (int i = 0; i < numMeseros; i++) {
            SpawnData spawnData = new SpawnData(100 + (i * 50), 100);
            System.out.println("ver valores del spawn"+spawnData);
            Entity meseroEntity = factory.createMesero(spawnData);
            Mesero mesero = new Mesero(
                    bufferOrdenes,
                    bufferComida,
                    monitorMesas,
                    monitorCocina,
                    meseroEntity
            );
            this.meseros.add(mesero);
            crearEntidadLogica("mesero", 1, 1);
            FXGL.getGameWorld().addEntity(meseroEntity);
        }

        for (int i = 0; i < numCocineros; i++) {
            SpawnData spawnData = new SpawnData(200 + (i * 50), 100);
            Entity cocineroEntity = factory.createCocinero(spawnData);
            Cocinero cocinero = new Cocinero(
                    bufferOrdenes,
                    bufferComida,
                    monitorCocina,
                    cocineroEntity
            );
            this.cocineros.add(cocinero);
            entidadManager.registrarEntidadLogica(id,  tipo, areaValida);
            FXGL.getGameWorld().addEntity(cocineroEntity);
        }
    }

    public void iniciarSimulacion() {
        for (Mesero mesero : meseros) {
            try {
                mesero.start();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error iniciando el mesero: " + e.getMessage());
            }
        }
        for (Cocinero cocinero : cocineros) {
            try {
                cocinero.start();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error iniciando el cocinero: " + e.getMessage());
            }
        }
    }

    public void llegarComensales(MonitorCocina monitorCocina, EntidadManager entidadManager) {
        new Thread(() -> {
            while (true) {
                try {
                    String nombreComensal = "Comensal " + (comensales.size() + 1);
                    SpawnData spawnData = new SpawnData(
                            300 + random.nextInt(200),
                            150 + (comensales.size() * 50)
                    );
                    Entity comensalEntity = factory.createComensal(spawnData);
                    Platform.runLater(() -> FXGL.getGameWorld().addEntity(comensalEntity));
                    Comensal comensal = new Comensal(
                            monitorMesas, bufferOrdenes, bufferComida, nombreComensal, comensalEntity
                    );
                    comensales.add(comensal);
                    crearEntidadLogica("comensal", 2, 2);
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
        return monitorMesas.getMesasDisponibles();
    }

    public List<Comensal> getComensales() {
        return comensales;
    }
}
