package com.restaurant.simulator.init;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.restaurant.simulator.controladores.RecepcionistaController;
import com.restaurant.simulator.actores.Cocinero;
import com.restaurant.simulator.actores.Comensal;
import com.restaurant.simulator.monitores.RecepcionistMonitor;
import com.restaurant.simulator.actores.Mesero;
import com.restaurant.simulator.monitores.MonitorMesero;
import javafx.geometry.Point2D;


public class MainApp extends GameApplication {


    private RecepcionistMonitor recepcionistMonitor;
    private MonitorMesero monitorMesero;

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setTitle("Restaurant Simulation");
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame() {
        FXGL.getGameScene().setBackgroundRepeat("fondo.jpeg");

        Point2D[] PosicionMesas = {
                new Point2D(50, 350), new Point2D(280, 300), new Point2D(50, 480), new Point2D(200, 200), new Point2D(450, 350), new Point2D(580, 480), new Point2D(480, 200), new Point2D(550, 300)
        };

        RecepcionistaController controller = new RecepcionistaController();

        int capacity = 8;
        recepcionistMonitor = new RecepcionistMonitor(PosicionMesas.length, PosicionMesas);
        monitorMesero = new MonitorMesero();
        for (int i = 0; i < capacity; i++) {
            Point2D position = PosicionMesas[i];
        }

        int numChefs = (int) Math.ceil(capacity * 0.1);
        for (int i = 0; i < numChefs; i++) {
            String NombreCocinero = "Chef" + (i + 1);
            Cocinero cocinero = new Cocinero(monitorMesero, NombreCocinero);
            cocinero.start();
        }


        int numWaiters = (int) Math.ceil(capacity * 0.1);
        for (int i = 0; i < numWaiters; i++) {
            String waiterName = "Mesero " + (i + 1);
            Mesero mesero = new Mesero(monitorMesero, waiterName);
            mesero.start();
        }

        for (int i = 1; i <= capacity*3; i++) {
            String dinerName = "C" + i;
            Comensal comensal = new Comensal(recepcionistMonitor, monitorMesero, dinerName);
            comensal.start();
        }
    }

    @Override
    protected void initUI() {
        FXGL.addUINode(FXGL.getUIFactoryService().newText("Restaurante don Mapache", 20), 10, 20);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
