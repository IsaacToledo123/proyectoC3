package org.example.Dominio;

public class Cocinero {
    private String nombre;
    private int id;

    public Cocinero(String nombre, int id) {
        this.nombre = nombre;
        this.id = id;
    }

    public void prepararOrden(Orden orden) {
        // Lógica para preparar la orden
        System.out.println("Preparando la orden: " + orden);
        orden.setEstado(EstadoOrden.EN_PROCESO);
    }
}
