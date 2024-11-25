package org.example.Servicio;

public class AsignarMesaService {
    private MonitorMesas monitorMesas;

    public AsignarMesaService(MonitorMesas monitorMesas) {
        this.monitorMesas = monitorMesas;
    }

    public void asignarMesaAComensal(Comensal comensal) {
        monitorMesas.asignarMesa();
        System.out.println("Mesa asignada a " + comensal.getEstado());
        comensal.esperarAtencion();
    }
}
