package simuladorrestaurant.actores;

import com.almasb.fxgl.entity.Entity;
import simuladorrestaurant.concurrencia.MonitorCocina;
import simuladorrestaurant.concurrencia.Buffer;
import simuladorrestaurant.concurrencia.BufferComida;
import javafx.application.Platform;

public class Cocinero extends Thread {
    private final MonitorCocina monitorCocina;
    private final Buffer bufferOrdenes;
    private final BufferComida bufferComida;
    private final Entity entity;

    public Cocinero(Buffer bufferOrdenes, BufferComida bufferComida, MonitorCocina monitorCocina, Entity entity) {
        this.monitorCocina = monitorCocina;  // Inicializamos el monitor de cocina
        this.bufferOrdenes = bufferOrdenes;  // Inicializamos el buffer de órdenes
        this.bufferComida = bufferComida;    // Inicializamos el buffer de comida
        this.entity = entity;  // Inicializamos la entidad FXGL
    }

    @Override
    public void run() {
        try {
            while (true) {
                String orden = bufferOrdenes.tomarOrden();

                if (orden != null) {
                    // Simular estado de cocina
                    cambiarEstadoEntidad("Cocinando");

                    System.out.println("Cocinero empieza a cocinar la orden de " + orden);
                    Thread.sleep(2000);  // Tiempo de cocción

                    cambiarEstadoEntidad("Comida lista");
                    System.out.println("Cocinero termina de cocinar la comida de " + orden);

                    bufferComida.agregarComida(orden);
                }

                Thread.sleep(1000);  // Prevenir uso excesivo de CPU
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Método para cambiar el estado de la entidad en la UI
    private void cambiarEstadoEntidad(String estado) {
        Platform.runLater(() -> {
            if (entity != null) {
                entity.setProperty("estado", estado); // Actualiza el estado gráfico
            }
        });
    }
}
