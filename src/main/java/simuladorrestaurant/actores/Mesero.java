package simuladorrestaurant.actores;

import javafx.application.Platform;
import simuladorrestaurant.concurrencia.MonitorCocina;
import simuladorrestaurant.concurrencia.MonitorMesas;
import simuladorrestaurant.concurrencia.Buffer;
import simuladorrestaurant.concurrencia.BufferComida;
import com.almasb.fxgl.entity.Entity;

public class Mesero extends Thread {
    private final MonitorMesas monitorMesas;
    private final MonitorCocina monitorCocina;
    private final Buffer bufferOrdenes;
    private final BufferComida bufferComida;
    private final Entity entity;

    // Constructor actualizado para aceptar el parámetro entity de FXGL
    public Mesero(Buffer bufferOrdenes, BufferComida bufferComida, MonitorMesas monitorMesas, MonitorCocina monitorCocina, Entity entity) {
        this.monitorMesas = monitorMesas;
        this.monitorCocina = monitorCocina;
        this.bufferOrdenes = bufferOrdenes;
        this.bufferComida = bufferComida;
        this.entity = entity; // Inicializamos la entidad FXGL
    }

    @Override
    public void run() {
        try {
            while (true) {
                String comida = bufferComida.tomarComida();

                if (comida != null) {
                    // Simular movimiento hacia la posición de la entrega
                    moverEntidad(200, 200); // Ejemplo de movimiento a una posición fija

                    System.out.println("Mesero entrega la comida a " + comida);

                    // Simular pausa para la entrega
                    Thread.sleep(500);

                    // Retornar a la posición inicial (ejemplo)
                    moverEntidad(0, 0);
                }

                Thread.sleep(1000); // Prevenir uso excesivo de CPU
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Método para mover la entidad en la UI
    private void moverEntidad(double x, double y) {
        Platform.runLater(() -> {
            if (entity != null) {
                entity.setPosition(x, y); // Actualiza posición gráfica
            }
        });
    }
}
