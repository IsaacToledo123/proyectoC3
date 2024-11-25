package org.example.Dominio;

public class Comensal {
    private String nombre;
    private int id;
    private EstadoComensal estado;

    public Comensal(String nombre, int id) {
        this.nombre = nombre;
        this.id = id;
        this.estado = EstadoComensal.LLEGANDO;
    }

    public void hacerPedido() {
        // Lógica para hacer un pedido
    }

    public void esperarAtencion() {
        this.estado = EstadoComensal.ESPERANDO;
    }

    public void comer() {
        this.estado = EstadoComensal.COMIENDO;
    }

    public void salir() {
        this.estado = EstadoComensal.SALIENDO;
    }

    public EstadoComensal getEstado() {
        return estado;
    }
}

enum EstadoComensal {
    LLEGANDO, ATENDIDO, ESPERANDO, COMIENDO, SALIENDO
}
