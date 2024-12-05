package com.restaurant.simulator.monitores;

import javafx.geometry.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RecepcionistMonitor {
    private final int capacity;
    private final Map<Point2D, Boolean> tableStatus;

    public RecepcionistMonitor(int capacity, Point2D[] tablePositions) {
        this.capacity = capacity;
        this.tableStatus = new HashMap<>();
        for (Point2D position : tablePositions) {
            tableStatus.put(position, false);
        }
    }

    public synchronized Point2D assignTable() throws InterruptedException {
        while (tableStatus.values().stream().noneMatch(status -> !status)) {
            wait();
        }
        for (Map.Entry<Point2D, Boolean> entry : tableStatus.entrySet()) {
            Thread.sleep(500);
            if (!entry.getValue()) {
                entry.setValue(true);
                return entry.getKey();
            }
        }
        return null;
    }

    public synchronized void releaseTable(Point2D tablePosition) {
        tableStatus.put(tablePosition, false); // Liberar la mesa
        System.out.println("La mesa ubicada en:"+tablePosition+" ha sido liberada");
        notifyAll(); // Notificar a los hilos en espera
    }

    public synchronized boolean isTableAvailable(Point2D tablePosition) {
        return tableStatus.getOrDefault(tablePosition, false);
    }
}
