package simuladorrestaurant.actores;

import com.almasb.fxgl.entity.Entity;
import simuladorrestaurant.concurrencia.MonitorCocina;
import simuladorrestaurant.concurrencia.Buffer;
import simuladorrestaurant.concurrencia.BufferComida;

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
                // El cocinero espera a que haya una orden disponible en el buffer de órdenes
                String orden = bufferOrdenes.tomarOrden(); // Tomamos una orden del buffer
                if (orden != null) {
                    System.out.println("Cocinero empieza a cocinar la orden de " + orden);
                    Thread.sleep(2000);  // Simulamos el tiempo de cocción

                    System.out.println("Cocinero termina de cocinar la comida de " + orden);
                    // Coloca la comida lista en el buffer de comida
                    bufferComida.agregarComida(orden);  // Coloca la comida lista en el buffer
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Si se interrumpe el hilo, lo marcamos como interrumpido
            System.out.println("Cocinero interrumpido");
        } catch (Exception e) {
            System.out.println("Error en la ejecución del cocinero: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
