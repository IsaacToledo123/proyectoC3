package org.example.Servicio;

public class PrepararOrdenService {
    private MonitorCocina monitorCocina;

    public PrepararOrdenService(MonitorCocina monitorCocina) {
        this.monitorCocina = monitorCocina;
    }

    public void prepararOrden(Cocinero cocinero, Orden orden) {
        monitorCocina.preparar();
        cocinero.prepararOrden(orden);
    }
}
