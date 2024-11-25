package simuladorrestaurant.concurrencia;

public class Monitor {
    public synchronized void esperar() throws InterruptedException {
        wait();  // Pone el hilo actual a esperar
    }

    public synchronized void notificar() {
        notify();  // Despierta a un hilo que está esperando
    }

    public synchronized void notificarTodos() {
        notifyAll();  // Despierta a todos los hilos que están esperando
    }
}
