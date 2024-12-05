package com.restaurant.simulator.actores;

import com.restaurant.simulator.controladores.ComensalController;
import com.restaurant.simulator.monitores.RecepcionistMonitor;
import com.restaurant.simulator.monitores.MonitorMesero;
import javafx.geometry.Point2D;

import java.util.Random;

public class Comensal extends Thread {
    private static final Random random = new Random();
    private static final  double LAMBDA = 0.1;
    private RecepcionistMonitor recepcionistMonitor;
    private MonitorMesero monitorMesero;
    private ComensalController comensalController;
    private boolean hasReceivedFood = false;
    private Point2D mesa;

    public Comensal(RecepcionistMonitor recepcionistMonitor, MonitorMesero monitorMesero, String name) {
        super(name);
        this.recepcionistMonitor = recepcionistMonitor;
        this.monitorMesero = monitorMesero;
        this.comensalController = new ComensalController(name);
    }

    public ComensalController getCustomerController() {
        return comensalController;
    }

    public Point2D getMesa() {
        return mesa;
    }

    @Override
    public void run() {
        try {
            System.out.println("Comensal " + this.getName() + " ha llegado al restaurante.");
            Thread.sleep(poisson(LAMBDA));
            comensalController.moveToLobby(380, 590);
            Thread.sleep(1000);
            mesa = recepcionistMonitor.asignarMesa();
            comensalController.moveToTable(mesa.getX(), mesa.getY());

            System.out.println("Comensal " + this.getName() + " está listo para ordenar.");
            monitorMesero.addOrder("Orden " + this.getName());

            monitorMesero.esperarComida(this.getName());

            System.out.println("Comensal " + this.getName() + " está comiendo.");
            Thread.sleep((int) (Math.random() * 5000) + 5000);
            System.out.println("Comensal " + this.getName() + " ha terminado de comer y está saliendo.");

            comensalController.exitRestaurant();

            recepcionistMonitor.mesaLibre(mesa);
        } catch (InterruptedException e) {
            System.err.println("Comensal " + this.getName() + " fue interrumpido: " + e.getMessage());
        }
    }

    private long poisson (double lambda){
        double u = random.nextDouble();
        return (long) (-Math.log(1 - u) / lambda * 1000);
    }

    public synchronized void receiveFood() {
        System.out.println("Valor cambiado");
        hasReceivedFood = true;
        notifyAll();
    }
}
