package simuladorrestaurant.concurrencia;

import java.util.ArrayList;
import java.util.List;

public class BufferComida {
    private final List<String> bufferComida;
    private final int capacidad;

    public BufferComida(int capacidad) {
        this.capacidad = capacidad;
        this.bufferComida = new ArrayList<>();
    }

    // Método sincronizado para que el cocinero coloque la comida lista en el buffer
    public synchronized void agregarComida(String comida) throws InterruptedException {
        while (bufferComida.size() == capacidad) {
            wait();  // Espera si el buffer de comida está lleno
        }
        bufferComida.add(comida);
        System.out.println("Comida agregada al buffer: " + comida);
        notifyAll();  // Notifica a los meseros que hay comida lista
    }

    // Método sincronizado para que el mesero tome la comida lista del buffer
    public synchronized String tomarComida() throws InterruptedException {
        while (bufferComida.isEmpty()) {
            wait();  // Espera si el buffer de comida está vacío
        }
        String comida = bufferComida.remove(0);
        System.out.println("Mesero lleva la comida: " + comida);
        notifyAll();  // Notifica a los cocineros que hay espacio en el buffer
        return comida;
    }
}
