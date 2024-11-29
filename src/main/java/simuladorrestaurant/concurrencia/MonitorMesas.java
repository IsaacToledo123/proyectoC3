package simuladorrestaurant.concurrencia;

import java.util.Queue;
import java.util.LinkedList;

public class MonitorMesas extends Monitor {
    private final Queue<String> mesasDisponibles = new LinkedList<>();

    public MonitorMesas(int numMesas) {
        for (int i = 0; i < numMesas; i++) {
            mesasDisponibles.add("Mesa " + (i + 1));
        }
    }

    // Método para que un comensal busque una mesa
    public synchronized String asignarMesa(String comensal) throws InterruptedException {
        while (mesasDisponibles.isEmpty()) {
            wait(); // El comensal espera a que haya una mesa disponible
        }
        String mesaAsignada = mesasDisponibles.poll();
        System.out.println(comensal + " ha sido asignado a " + mesaAsignada);
        return mesaAsignada;
    }

    // Método para liberar una mesa
    public synchronized void liberarMesa(String mesa) {
        mesasDisponibles.add(mesa);
        notify();  // Despierta a un comensal esperando por una mesa
        System.out.println(mesa + " ha sido liberada.");
    }

    // Nuevo método para obtener la cantidad de mesas disponibles
    public synchronized int getMesasDisponibles() {
        return mesasDisponibles.size();
    }
}
