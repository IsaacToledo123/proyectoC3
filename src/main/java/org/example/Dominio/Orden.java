package org.example.Dominio;

public class Orden {
    private int id;
    private EstadoOrden estado;

    public Orden(int id) {
        this.id = id;
        this.estado = EstadoOrden.PENDIENTE;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public EstadoOrden getEstado() {
        return estado;
    }
}

enum EstadoOrden {
    PENDIENTE, EN_PROCESO, TERMINADA
}
