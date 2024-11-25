package simuladorrestaurant.actores;

import simuladorrestaurant.concurrencia.MonitorMesas;
import simuladorrestaurant.concurrencia.Buffer;
import simuladorrestaurant.concurrencia.BufferComida;

public class Comensal extends Thread {
    private final MonitorMesas monitorMesas;
    private final Buffer bufferOrdenes;
    private final BufferComida bufferComida;
    private final String nombre;

    // Modificar el constructor para aceptar Buffer y BufferComida
    public Comensal(MonitorMesas monitorMesas, Buffer bufferOrdenes, BufferComida bufferComida, String nombre) {
        this.monitorMesas = monitorMesas;
        this.bufferOrdenes = bufferOrdenes;
        this.bufferComida = bufferComida;
        this.nombre = nombre;
    }

    @Override
    public void run() {
        try {
            // El comensal busca una mesa
            System.out.println(nombre + " llega al restaurante y busca una mesa.");
            String mesa = monitorMesas.asignarMesa(nombre);  // Buscar mesa en MonitorMesas

            // Se sincroniza con la cocina para hacer el pedido
            synchronized (bufferOrdenes) {  // Cambié monitorCocina por bufferOrdenes
                System.out.println(nombre + " se sienta en " + mesa + " y espera al mesero para hacer su pedido.");
                bufferOrdenes.agregarOrden(nombre);  // Enviar la orden a bufferOrdenes
                System.out.println(nombre + " hace su pedido.");
                bufferOrdenes.wait();  // Espera hasta que la comida esté lista
            }

            // El comensal recibe la comida
            System.out.println(nombre + " recibe su comida y empieza a comer.");
            monitorMesas.liberarMesa(mesa);  // El comensal libera la mesa después de comer
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
