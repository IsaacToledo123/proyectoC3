package com.restaurant.simulator.actores;

import com.restaurant.simulator.controladores.CocineroController;
import com.restaurant.simulator.monitores.MonitorMesero;

public class Cocinero extends Thread {
    private final MonitorMesero monitorMesero;
    private final CocineroController cocineroController;

    public Cocinero(MonitorMesero monitorMesero, String name) {
        super(name);
        this.monitorMesero = monitorMesero;
        this.cocineroController = new CocineroController(name); // Nuevo controlador
    }

    @Override
    public void run() {
        try {
            while (true) {
                String order = monitorMesero.takeOrderChef();
                cocineroController.startCooking(order);

                Thread.sleep((int) (Math.random() * 2000));

                monitorMesero.addReadyOrder(order);
                cocineroController.finishCooking(order);
            }
        } catch (InterruptedException e) {
            System.out.println("Cocinero " + this.getName() + " ha terminado su turno.");
        }
    }
}
