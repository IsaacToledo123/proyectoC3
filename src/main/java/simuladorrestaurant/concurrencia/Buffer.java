package simuladorrestaurant.concurrencia;

import java.util.ArrayList;
import java.util.List;

public class Buffer {
    private final List<String> bufferOrdenes;
    private final int capacidad;

    public Buffer(int capacidad) {
        this.capacidad = capacidad;
        this.bufferOrdenes = new ArrayList<>();
    }

    // Método sincronizado para que el mesero coloque una orden en el buffer
    public synchronized void agregarOrden(String orden) throws InterruptedException {
        while (bufferOrdenes.size() == capacidad) {
            wait();  // Espera si el buffer está lleno
        }
        bufferOrdenes.add(orden);
        System.out.println("Orden agregada al buffer: " + orden);
        notifyAll();  // Notifica a los cocineros que hay una nueva orden
    }

    // Método sincronizado para que el cocinero tome una orden del buffer
    public synchronized String tomarOrden() throws InterruptedException {
        while (bufferOrdenes.isEmpty()) {
            wait();  // Espera si el buffer está vacío
        }
        String orden = bufferOrdenes.remove(0);
        System.out.println("Cocinero toma la orden: " + orden);
        notifyAll();  // Notifica a los meseros que hay espacio en el buffer
        return orden;
    }
}
