package com.restaurant.simulator.monitores;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Map;
import java.util.HashMap;

public class MonitorMesero {
    private final Queue<String> orderBuffer = new LinkedList<>();
    private final Queue<String> orderBufferChef = new LinkedList<>();
    private final Queue<String> readyBuffer = new LinkedList<>();
    private final Map<String, Boolean> dinerFoodStatus = new HashMap<>();

    public synchronized void addOrder(String order) {
        if (order == null || order.isEmpty()) {
            throw new IllegalArgumentException("La orden no puede ser nula o vacía");
        }
        String dinerName = order.split(" ")[1];
        dinerFoodStatus.put(dinerName, false);
        orderBuffer.add(order);
        System.out.println("Orden añadida al buffer: " + order + " del " + dinerName);
        notifyAll();
    }

    public synchronized String takeOrder() throws InterruptedException {
        while (orderBuffer.isEmpty()) {
            wait();
        }
        String order = orderBuffer.poll();
        notifyAll();
        return order;
    }

    public synchronized void addOrderChef(String order){
        if (order == null || order.isEmpty()) {
            throw new IllegalArgumentException("La orden no puede ser nula o vacía");
        }
        orderBufferChef.add(order);
        System.out.println("Orden añadida al buffer de chefs: " + order);
        notifyAll();
    }

    public synchronized String takeOrderChef() throws InterruptedException{
        while (orderBufferChef.isEmpty()){
            wait();
        }
        String orderChef = orderBufferChef.poll();
        notifyAll();
        return orderChef;
    }

    public synchronized void addReadyOrder(String readyOrder) {
        if (readyOrder == null || readyOrder.isEmpty()) {
            throw new IllegalArgumentException("La orden lista no puede ser nula o vacía");
        }
        readyBuffer.add(readyOrder);
        System.out.println("Comida lista añadida al buffer: " + readyOrder);
        notifyAll();
    }

    public synchronized String takeReadyOrder() throws InterruptedException {
        while (readyBuffer.isEmpty()) {
            wait();
        }
        String readyOrder = readyBuffer.poll();
        System.out.println("Comida lista tomada del buffer: " + readyOrder);
        notifyAll();
        return readyOrder;
    }

    public synchronized void deliverFood(String dinerName) {
        dinerFoodStatus.put(dinerName, true);
        System.out.println("Comida entregada a: " + dinerName + " (Estado: " + dinerFoodStatus.get(dinerName) + ")");
        notifyAll();
    }

    public synchronized void waitFood(String dinerName) throws InterruptedException {
        while (!dinerFoodStatus.getOrDefault(dinerName, false)) {
            wait();
        }
        System.out.println("Comida lista para: " + dinerName);
    }
}