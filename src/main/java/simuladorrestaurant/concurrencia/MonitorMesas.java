package simuladorrestaurant.concurrencia;

import simuladorrestaurant.concurrencia.interfaces.Observer;
import simuladorrestaurant.actores.Comensal;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class MonitorMesas extends Monitor {
    private final Queue<String> mesasDisponibles;
    private final Set<String> mesasOcupadas;  // Set para llevar un registro de las mesas ocupadas
    private final List<Observer> observers;  // Lista de observadores

    public MonitorMesas(int numMesas) {
        mesasDisponibles = new LinkedList<>();
        mesasOcupadas = new HashSet<>();  // Usamos un HashSet para almacenar las mesas ocupadas
        observers = new LinkedList<>();

        for (int i = 0; i < numMesas; i++) {
            mesasDisponibles.add("Mesa " + (i + 1)); // Agrega mesas a la cola
        }
    }

    // Método para registrar observadores
    public synchronized void registrarObserver(Observer observer) {
        observers.add(observer);
    }

    // Método para desregistrar observadores
    public synchronized void desregistrarObserver(Observer observer) {
        observers.remove(observer);
    }

    // Notificar a todos los observadores de un cambio en el estado
    public synchronized void notificar() {
        for (Observer observer : observers) {
            observer.actualizar(); // Llamar al método de actualización de cada observador
        }
    }

    public synchronized String asignarMesa(Comensal comensal) throws InterruptedException {
        while (mesasDisponibles.isEmpty()) {
            System.out.println(comensal.getNombre() + " está esperando por una mesa...");
            wait(); // El comensal espera a que haya una mesa disponible
        }

        // Verificar si hay una mesa disponible que no esté ya ocupada
        String mesaAsignada = null;
        for (String mesa : mesasDisponibles) {
            // Si la mesa no está ocupada
            if (!mesasOcupadas.contains(mesa)) {
                mesaAsignada = mesa; // Asignar la mesa al comensal
                break; // Salir del bucle una vez que se haya encontrado una mesa
            }
        }

        if (mesaAsignada == null) {
            System.out.println("No se ha encontrado ninguna mesa disponible para " + comensal.getNombre());
            return null; // No se encontró ninguna mesa disponible
        }

        // Marcar la mesa como ocupada
        mesasDisponibles.remove(mesaAsignada); // Remover la mesa de las disponibles
        mesasOcupadas.add(mesaAsignada); // Agregarla a las ocupadas

        System.out.println(comensal.getNombre() + " se ha asignado a " + mesaAsignada);

        // Notificar a los observadores sobre el cambio de estado (mesa asignada)
        notificar();

        return mesaAsignada;
    }

    public synchronized void liberarMesa(String mesa) {
        mesasDisponibles.add(mesa); // Se agrega la mesa liberada a la cola
        mesasOcupadas.remove(mesa);  // Marcar la mesa como libre
        notify(); // Notifica a los comensales esperando que una mesa está libre
        System.out.println(mesa + " ha sido liberada.");

        // Notificar a los observadores sobre el cambio de estado (mesa liberada)
        notificar();
    }

    public synchronized int getMesasDisponibles() {
        return mesasDisponibles.size(); // Devuelve la cantidad de mesas disponibles
    }

    // Nuevo método para verificar si una mesa está ocupada
    public synchronized boolean isMesaOcupada(int row, int col) {
        String mesa = "Mesa " + (row * 10 + col + 1);  // Supongamos que las mesas se numeran de esta forma
        return mesasOcupadas.contains(mesa);  // Verifica si la mesa está en el conjunto de mesas ocupadas
    }

    // Método adicional para manejar la disponibilidad de mesas con cocineros
    public synchronized void liberarMesaPorCocinero(String mesa) {
        // Verifica si la mesa está ocupada antes de liberar
        if (mesasOcupadas.contains(mesa)) {
            liberarMesa(mesa); // Usar el método de liberar mesa
            System.out.println("Cocinero ha liberado la " + mesa);
        }
    }

    // Método para verificar la cantidad de mesas ocupadas
    public synchronized int getMesasOcupadas() {
        return mesasOcupadas.size(); // Devuelve la cantidad de mesas ocupadas
    }
}
