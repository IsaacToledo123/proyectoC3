package org.example;

import java.util.List;
import java.util.ArrayList;

public class RepositorioOrden {
    private List<Orden> ordenes = new ArrayList<>();

    public void guardarOrden(Orden orden) {
        ordenes.add(orden);
        System.out.println("Orden guardada: " + orden);
    }

    public List<Orden> obtenerOrdenes() {
        return ordenes;
    }
}
