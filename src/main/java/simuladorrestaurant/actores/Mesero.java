package simuladorrestaurant.actores;

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
        this.entity = entity;  // Inicializamos la entidad FXGL
    }

    @Override
    public void run() {
        try {
            while (true) {
                // El mesero espera que haya comida lista en el buffer de comida
                String comida = bufferComida.tomarComida();  // Tomamos la comida del buffer de comida

                if (comida != null) {
                    System.out.println("Mesero entrega la comida a " + comida);

                    // Aquí puedes actualizar la posición de la entidad mesero en FXGL si es necesario
                    // Ejemplo de mover la entidad (ajustando a una nueva posición)
                    entity.setPosition(100, 100);  // Establecemos una nueva posición (ajustar según sea necesario)

                    // Notifica al monitor de cocina que se entregó la comida
                    synchronized (monitorCocina) {
                        monitorCocina.notify();  // Notificamos que la comida ha sido entregada
                    }
                }

                // Aquí puedes agregar más lógica si es necesario
                // Por ejemplo, manejar las órdenes del bufferOrdenes si se requiere.
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Captura la interrupción de la thread
        }
    }
}
