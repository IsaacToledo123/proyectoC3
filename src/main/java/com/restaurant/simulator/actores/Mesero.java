package com.restaurant.simulator.actores;

import com.restaurant.simulator.controladores.MeseroController;
import com.restaurant.simulator.monitores.MonitorMesero;
import javafx.geometry.Point2D;
import java.util.Random;

public class Mesero extends Thread {
    private final MonitorMesero monitorMesero;
    private final MeseroController meseroController;
    private Point2D[] tablePositions = {
            new Point2D(130, 200),
            new Point2D(270, 305),
            new Point2D(130, 410),
            new Point2D(350, 200),
            new Point2D(490, 305),
            new Point2D(490, 410),
            new Point2D(420, 160),
            new Point2D(490, 160)
    };

    public Mesero(MonitorMesero monitorMesero, String name) {
        super(name);
        this.monitorMesero = monitorMesero;
        this.meseroController = new MeseroController(name); // Nuevo controlador
    }

    @Override
    public void run() {
        try {
            while (true) {
                String order = monitorMesero.tomarOrden();
                System.out.println(this.getName() + " tomó la orden: " + order);

                meseroController.moverCocina(400, 200);
                monitorMesero.agregarOrden(order);

                String readyOrder = monitorMesero.tomarBuffer();
                System.out.println(this.getName() + " entregó: " + readyOrder);

                System.out.println("La mesa del cliente es:");
                String dinerName = readyOrder.split(" ")[1];
                Random random = new Random();
                int mesaRandom = random.nextInt(7);
                Point2D point = tablePositions[mesaRandom];
                meseroController.moveToTable(point);
                monitorMesero.entregarComida(dinerName);
                meseroController.deliverOrder(readyOrder);

                Thread.sleep((int) (Math.random() * 2000));
            }
        } catch (InterruptedException e) {
            System.out.println("Mesero " + this.getName() + " ha terminado su turno.");
        }
    }
}
