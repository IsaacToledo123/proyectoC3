package org.example.Dominio;

public class Mesero {
    private String nombre;
    private int id;

    public Mesero(String nombre, int id) {
        this.nombre = nombre;
        this.id = id;
    }

    public void atenderComensal(Comensal comensal) {
        // Lógica para atender a un comensal
        System.out.println("Atendiendo a " + comensal.getEstado());
        comensal.hacerPedido();
    }
}
