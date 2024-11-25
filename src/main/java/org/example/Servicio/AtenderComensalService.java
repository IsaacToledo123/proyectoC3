package org.example.Servicio;

public class AtenderComensalService {
    private MonitorAtencion monitorAtencion;

    public AtenderComensalService(MonitorAtencion monitorAtencion) {
        this.monitorAtencion = monitorAtencion;
    }

    public void atenderComensal(Mesero mesero, Comensal comensal) {
        monitorAtencion.atender();
        mesero.atenderComensal(comensal);
        comensal.comer();
    }
}
