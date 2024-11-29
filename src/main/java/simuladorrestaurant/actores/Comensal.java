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

    public Comensal(MonitorMesas monitorMesas, Buffer bufferOrdenes, BufferComida bufferComida, String nombre, Entity entidad) {
        this.monitorMesas = monitorMesas;
        this.bufferOrdenes = bufferOrdenes;
        this.bufferComida = bufferComida;
        this.nombre = nombre;
        this.entidad = entidad; // Inicializar la entidad gráfica
    }

    @Override
    public void run() {
        try {
            // Actualizar estado en la UI
            actualizarEstado("Buscando mesa");

            System.out.println(nombre + " llega al restaurante y busca una mesa.");
            String mesa = monitorMesas.asignarMesa(nombre);

            actualizarEstado("Sentado en " + mesa);

            synchronized (bufferOrdenes) {
                System.out.println(nombre + " se sienta en " + mesa + " y espera al mesero para hacer su pedido.");
                bufferOrdenes.agregarOrden(nombre);
                System.out.println(nombre + " hace su pedido.");
                bufferOrdenes.wait();
            }

            actualizarEstado("Comiendo");
            System.out.println(nombre + " recibe su comida y empieza a comer.");

            // Simular tiempo de comida
            Thread.sleep(3000);

            actualizarEstado("Terminando");
            monitorMesas.liberarMesa(mesa);

            actualizarEstado("Saliendo");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // Eliminar entidad de la UI
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
                entidad.setProperty("estado", estado);
            }
        });
    }

    // Método para actualizar posición en la UI
    public void actualizarPosicion(double x, double y) {
        Platform.runLater(() -> {
            if (entidad != null) {
                entidad.setPosition(x, y);
            }
        });
    }

    // Getters y setters adicionales
    public String getEstadoActual() {
        return estadoActual;
    }
}
