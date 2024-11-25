package simuladorrestaurant.logica;

// Importar las clases de los actores
import simuladorrestaurant.actores.Mesero;
import simuladorrestaurant.actores.Cocinero;
import simuladorrestaurant.actores.Comensal;

// Importar las clases de concurrencia
import simuladorrestaurant.concurrencia.MonitorCocina;
import simuladorrestaurant.concurrencia.MonitorMesas;
import simuladorrestaurant.concurrencia.Buffer;
import simuladorrestaurant.concurrencia.BufferComida;

// Importar las clases de UI
import simuladorrestaurant.ui.RestaurantEntityFactory;
import com.almasb.fxgl.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {
    private final MonitorMesas monitorMesas;      // Monitor para las mesas
    private final MonitorCocina monitorCocina;    // Monitor para la cocina
    private final Buffer bufferOrdenes;          // Buffer de órdenes
    private final BufferComida bufferComida;    // Buffer de comida
    private final List<Comensal> comensales;     // Lista de comensales
    private final List<Mesero> meseros;          // Lista de meseros
    private final List<Cocinero> cocineros;      // Lista de cocineros

    public Restaurant(int numMeseros, int numCocineros, int numMesas) {
        this.monitorMesas = new MonitorMesas(numMesas);
        this.monitorCocina = new MonitorCocina();
        this.bufferOrdenes = new Buffer(10);
        this.bufferComida = new BufferComida(5);

        this.comensales = new ArrayList<>();
        this.meseros = new ArrayList<>();
        this.cocineros = new ArrayList<>();

        // Crear e iniciar los meseros y cocineros
        for (int i = 0; i < numMeseros; i++) {
            Entity meseroEntity = new RestaurantEntityFactory().createMesero(0, 0);  // Crear la entidad del mesero
            Mesero mesero = new Mesero(this.bufferOrdenes, this.bufferComida, this.monitorMesas, this.monitorCocina, meseroEntity);
            this.meseros.add(mesero);
        }

        for (int i = 0; i < numCocineros; i++) {
            Entity cocineroEntity = new RestaurantEntityFactory().createCocinero(0, 0);  // Crear la entidad del cocinero
            Cocinero cocinero = new Cocinero(this.bufferOrdenes, this.bufferComida, this.monitorCocina, cocineroEntity);
            this.cocineros.add(cocinero);
        }
    }

    // Método para simular la llegada de comensales
    public void llegarComensales() {
        // Simulación de llegada aleatoria de comensales
        new Thread(() -> {
            while (true) {
                try {
                    String nombreComensal = "Comensal " + (comensales.size() + 1);  // Asignar un nombre único al comensal
                    Comensal comensal = new Comensal(monitorMesas, bufferOrdenes, bufferComida, nombreComensal);
                    comensales.add(comensal);
                    comensal.start();  // Inicia el hilo del comensal
                    Thread.sleep((long) (Math.random() * 5000)); // Llegada aleatoria de comensales
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    // Iniciar la simulación del restaurante (meseros, cocineros)
    public void iniciarSimulacion() {
        for (Mesero mesero : meseros) {
            new Thread(mesero).start();
        }

        for (Cocinero cocinero : cocineros) {
            new Thread(cocinero).start();
        }
    }

    public static void main(String[] args) {
        Restaurant restaurant = new Restaurant(5, 3, 10); // 5 meseros, 3 cocineros, 10 mesas
        restaurant.llegarComensales(); // Comienza la llegada de comensales
        restaurant.iniciarSimulacion(); // Inicia la simulación de meseros y cocineros
    }
}