package simuladorrestaurant.actores;

import com.almasb.fxgl.entity.Entity;
import javafx.application.Platform;
import simuladorrestaurant.concurrencia.MonitorMesas;
import simuladorrestaurant.concurrencia.Buffer;
import simuladorrestaurant.concurrencia.BufferComida;

public class Comensal extends Thread {
    private final MonitorMesas monitorMesas;
    private final Buffer bufferOrdenes;
    private final BufferComida bufferComida;
    private final String nombre;
    private final Entity entidad;
    private String estadoActual;

    // Constructor
    public Comensal(MonitorMesas monitorMesas, Buffer bufferOrdenes, BufferComida bufferComida, String nombre, Entity entidad) {
        this.monitorMesas = monitorMesas;
        this.bufferOrdenes = bufferOrdenes;
        this.bufferComida = bufferComida;
        this.nombre = nombre;
        this.entidad = entidad; // Inicializar la entidad gráfica
    }

    // Método principal que ejecuta el hilo
    @Override
    public void run() {
        try {
            // Actualiza el estado a "Buscando mesa"
            actualizarEstado("Buscando mesa");

            System.out.println(nombre + " llega al restaurante y busca una mesa.");
            // Asigna una mesa y actualiza el estado a "Sentado"
            String mesa = monitorMesas.asignarMesa(this);
            actualizarEstado("Sentado en " + mesa);
            System.out.println(nombre + " se sienta en " + mesa + " y espera al mesero para hacer su pedido.");

            // Hacer pedido
            bufferOrdenes.agregarOrden(nombre);
            System.out.println(nombre + " hace su pedido.");

            // Esperar a recibir la comida
            String comida = null;
            while (comida == null) {
                comida = bufferComida.tomarComida();
                if (comida == null) {
                    Thread.sleep(100); // Espera antes de intentar nuevamente
                }
            }

            // Recibe la comida y actualiza el estado
            actualizarEstado("Comiendo");
            System.out.println(nombre + " recibe su comida y empieza a comer.");

            // Simula tiempo de comida
            Thread.sleep(3000);

            // Termina y libera la mesa
            actualizarEstado("Terminando");
            monitorMesas.liberarMesa(mesa);
            System.out.println(nombre + " termina de comer y libera " + mesa);

            // Sale del restaurante
            actualizarEstado("Saliendo");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // Elimina la entidad gráfica de la UI cuando el hilo termina
            Platform.runLater(() -> {
                if (entidad != null) {
                    entidad.removeFromWorld();
                }
            });
        }
    }

    // Método para actualizar estado en la UI
    private void actualizarEstado(String estado) {
        this.estadoActual = estado;
        Platform.runLater(() -> {
            if (entidad != null) {
                entidad.setProperty("estado", estado);  // Actualiza el estado gráfico
            }
        });
    }

    // Método para actualizar posición en la UI
    public void actualizarPosicion(double x, double y) {
        Platform.runLater(() -> {
            if (entidad != null) {
                entidad.setPosition(x, y);  // Mueve la entidad gráficamente
            }
        });
    }

    // Método getter para el nombre
    public String getNombre() {
        return nombre;
    }

    // Getters y setters adicionales
    public String getEstadoActual() {
        return estadoActual;
    }
}
