package simuladorrestaurant.concurrencia;

import java.util.Queue;
import java.util.LinkedList;

public class MonitorCocina {
    private final Queue<String> bufferDeOrdenes = new LinkedList<>();
    private final Queue<String> bufferDeComidas = new LinkedList<>();

    // Método para agregar una orden a la cocina
    public synchronized void agregarOrden(String comensal) {
        bufferDeOrdenes.add(comensal);
        System.out.println("Orden añadida por " + comensal);
        notify();  // Despierta al cocinero para que cocine la orden
    }

    // Método para obtener una orden pendiente para el cocinero
    public synchronized String obtenerOrden() throws InterruptedException {
        while (bufferDeOrdenes.isEmpty()) {
            wait();  // El cocinero espera a que haya órdenes
        }
        return bufferDeOrdenes.poll();
    }

    // Método para agregar comida lista al buffer
    public synchronized void agregarComida(String comida) {
        bufferDeComidas.add(comida);
        System.out.println("Comida lista para " + comida);
        notify();  // Notifica al mesero que la comida está lista
    }

    // Método para obtener la comida lista para el mesero
    public synchronized String obtenerComida() throws InterruptedException {
        while (bufferDeComidas.isEmpty()) {
            wait();  // El mesero espera a que haya comida lista
        }
        return bufferDeComidas.poll();
    }

    // Método de notificación al comensal
    public synchronized void notifyComensal(String comida) {
        System.out.println("Comensal, su comida está lista: " + comida);
    }
}
