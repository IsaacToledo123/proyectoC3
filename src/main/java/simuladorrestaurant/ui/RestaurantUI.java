package simuladorrestaurant.ui;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import com.almasb.fxgl.entity.SpawnData;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;
import com.almasb.fxgl.entity.Entity;
import simuladorrestaurant.actores.Comensal;
import simuladorrestaurant.config.MesaConfig;
import simuladorrestaurant.concurrencia.MonitorMesas;
import simuladorrestaurant.concurrencia.interfaces.Observer;
import simuladorrestaurant.logica.Restaurant;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;
import java.util.List;
import java.util.Objects;

public class RestaurantUI extends GameApplication implements Observer {
    private Restaurant restaurant;
    private MonitorMesas monitorMesas;
    private Text textoEstado;
    private int comensalesEnEspera = 0;
    private int mesasDisponibles = 10;

    private static final int COLUMNS = 30; // Número de columnas de la matriz
    private static final int ROWS = 36;   // Número de filas de la matriz

    public static void main(String[] args) {
        launch(args);  // Llamada correcta al método launch
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(800);
        settings.setHeight(600);
        settings.setTitle("Simulador de Restaurante");
        settings.setVersion("1.0");
    }

    @Override
    protected void initGame() {
        // Registrar la fábrica de entidades
        getGameWorld().addEntityFactory(new RestaurantEntityFactory());

        // Crear el monitor de mesas con el número adecuado de mesas
        monitorMesas = new MonitorMesas(10); // Asumimos que hay 10 mesas disponibles

        // Registrar la UI como observador del MonitorMesas
        monitorMesas.registrarObserver(this); // Registramos el observer

        // Crear la instancia de Restaurant
        restaurant = new Restaurant(5, 3, 10);
        restaurant.iniciarSimulacion();
        restaurant.llegarComensales();
    }

    @Override
    protected void initUI() {
        // Registrar el EntityFactory
        getGameWorld().addEntityFactory(new RestaurantEntityFactory());

        // Fondo
        Image fondo = new Image(Objects.requireNonNull(getClass().getResource("/simuladorrestaurant/assets/cuked.jpeg")).toExternalForm());
        ImageView imagenFondo = new ImageView(fondo);
        imagenFondo.setFitWidth(getSettings().getWidth());
        imagenFondo.setFitHeight(getSettings().getHeight());
        getGameScene().addUINode(imagenFondo);

        // Crear texto de estado
        textoEstado = new Text("Comensales en espera: 0");
        textoEstado.setFill(Color.WHITE);
        textoEstado.setX(10);
        textoEstado.setY(20);
        getGameScene().addUINode(textoEstado);

        // Dimensiones de las celdas
        getSettings();
        getSettings();

        // Dibujar la cuadrícula sin colores visibles
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                // Asignar áreas sin colores
                if (isCocina(row, col)) {
                    // Cocina
                    spawnCocinero(row, col);
                } else if (isCaminar(row, col)) {
                    // Caminar
                    spawnMesero(row, col);
                } else if (isAsientoSimple(row, col)) {
                    // Asientos simples
                    spawnComensal(row, col);
                } else if (MesaConfig.isMesa(row, col)) {
                    // Mesas
                    spawnMesa(row, col);
                } else if (isPuntoComensal(row, col)) {
                    // Punto de comensales
                }
            }
        }

        // Actualizar estado periódicamente
        getGameTimer().runAtInterval(() -> {
            comensalesEnEspera = restaurant.getComensalesEnEspera();
            mesasDisponibles = restaurant.getMesasDisponibles();

            textoEstado.setText(String.format(
                    "Comensales en espera: %d | Mesas disponibles: %d",
                    comensalesEnEspera,
                    mesasDisponibles
            ));
        }, Duration.seconds(0.5));
    }

    // Cocina
    private boolean isCocina(int row, int col) {
        return (row >= 11 && row <= 12) && (col >= 2 && col <= 29);
    }

    // Caminar
    private boolean isCaminar(int row, int col) {
        return (row == 16 && (col >= 2 && col <= 7 || col >= 9 && col <= 10 || col >= 13 && col <= 17 || col >= 20 && col <= 27)) ||
                (row == 17 && (col >= 2 && col <= 27)) ||
                (row >= 18 && row <= 19 && col >= 2 && col <= 26) ||
                (row >= 20 && row <= 21 && (col >= 2 && col <= 3 || col >= 6 && col <= 10 || col >= 25 && col <= 27)) ||
                (row >= 22 && row <= 24 && (col >= 9 && col <= 10 || col >= 25 && col <= 27)) ||
                (row >= 25 && row <= 27 && (col >= 9 && col <= 12 || col >= 16 && col <= 17 || col >= 20 && col <= 22 || col >= 25 && col <= 26)) ||
                (row == 28 && (col >= 2 && col <= 27)) ||
                (row >= 29 && row <= 31 && (col >= 9 && col <= 17 || col >= 25 && col <= 27)) ||
                (row >= 32 && row <= 34 && (col >= 9 && col <= 17 || col >= 25 && col <= 26));
    }

    // Asientos
    private boolean isAsientoSimple(int row, int col) {
        return ((row == 16 && (col == 8 || col == 11 || col == 12 || col == 18 || col == 19)) ||
                ((row == 18 || row == 19) && col == 27)) || ((row == 25 || row == 26) && col == 27) || ((row == 32 || row == 33) && col == 27) ||
                (row >= 21 && row <= 22) && (col >= 4 && col <= 5 || col >= 11 && col <= 12 || col >= 16 && col <= 17 || col >= 18 && col <= 19 || col >= 23 && col <= 24) ||
                (row >= 23 && row <= 24) && (col >= 11 && col <= 12 || col >= 16 && col <= 17 || col >= 18 && col <= 19 || col >= 23 && col <= 24) ||
                (row >= 30 && row <= 31) && (col >= 2 && col <= 3 || col >= 7 && col <= 8 || col >= 18 && col <= 19 || col >= 23 && col <= 24) ||
                (row >= 32 && row <= 33) && (col >= 2 && col <= 3 || col >= 7 && col <= 8 || col >= 18 && col <= 19 || col >= 23 && col <= 24) ||
                (row >= 23 && row <= 24) && (col >= 2 && col <= 3 || col >= 7 && col <= 8 ) ||
                (row >= 25 && row <= 26) && (col >= 2 && col <= 3 || col >= 7 && col <= 8 || col == 13 || col == 15);
    }

    // Punto de comensales
    private boolean isPuntoComensal(int row, int col) {
        return row == 14 && (col == 8 || col == 11 || col == 12 || col == 18 || col == 19);
    }

    // Método para actualizar la UI cuando cambian las mesas
    @Override
    public void actualizar() {
        // Actualizamos el estado de los comensales y las mesas
        comensalesEnEspera = restaurant.getComensalesEnEspera();
        mesasDisponibles = monitorMesas.getMesasDisponibles();

        // Actualizamos el estado visual de las mesas
        for (int row = 0; row < 50; row++) {  // Usamos un valor arbitrario para las filas
            for (int col = 0; col < 50; col++) {  // Usamos un valor arbitrario para las columnas
                if (MesaConfig.isMesa(row, col)) {  // Verificamos si la celda es una mesa
                    // Verificamos si la mesa está ocupada o libre
                    if (monitorMesas.isMesaOcupada(row, col)) {
                        // Actualizamos la mesa a color rojo si está ocupada
                        actualizarMesa(row, col, Color.RED); // Mesa ocupada en color rojo
                    } else {
                        // Actualizamos la mesa a color verde si está libre
                        actualizarMesa(row, col, Color.GREEN); // Mesa disponible en color verde
                    }
                }
            }
        }
    }

    // Método para actualizar la visualización de las mesas con color
    private void actualizarMesa(int row, int col, Color color) {
        double cellWidth = getSettings().getWidth() / 50;
        double cellHeight = getSettings().getHeight() / 50;

        Rectangle mesa = new Rectangle(cellWidth, cellHeight);
        mesa.setFill(color.deriveColor(0, 1, 1, 0.5)); // Color translúcido

        // Usamos los métodos de MesaConfig para calcular la posición de la mesa
        mesa.setX(MesaConfig.calcularX(col, cellWidth));
        mesa.setY(MesaConfig.calcularY(row, cellHeight));

        getGameScene().addUINode(mesa); // Agregamos el nodo a la escena
    }

    // Métodos para spawn de las entidades
    private void spawnMesa(int row, int col) {
        if (MesaConfig.isMesa(row, col)) {
            // Calculamos las dimensiones de la celda
            double cellWidth = getSettings().getWidth() / COLUMNS;
            double cellHeight = getSettings().getHeight() / ROWS;

            // Coordenadas de la celda
            double x = col * cellWidth;
            double y = row * cellHeight;

            // Verificamos si hay comensales esperando
            List<Comensal> comensales = restaurant.getComensales();
            if (!comensales.isEmpty()) {
                // Obtener el próximo comensal de la lista
                Comensal comensal = comensales.get(0);

                try {
                    // Intentamos asignar la mesa al comensal
                    String mesaAsignada = monitorMesas.asignarMesa(comensal);
                    if (mesaAsignada != null) {

                        // Si la mesa fue asignada, creamos la mesa visualmente
                        SpawnData data = new SpawnData(x, y);
                        data.put("col", col);
                        data.put("row", row);
                        RestaurantEntityFactory entityFactory = new RestaurantEntityFactory();
                        Entity mesa = entityFactory.createMesa(data);  // Aquí invocamos el método createMesa
                        System.out.println(comensal.getNombre() + " ha sido asignado a la mesa en: (" + row + ", " + col + ")");
                    } else {
                        System.out.println("No hay mesas disponibles para " + comensal.getNombre());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No hay comensales esperando para asignar mesa.");
            }
        }
    }

    private void spawnCocinero(int row, int col) {
        // Spawn Cocinero
        spawn("cocinero", col * 25, row * 25);
    }

    private void spawnComensal(int row, int col) {
        // Spawn Comensal
        spawn("comensal", col * 25, row * 25);
    }

    private void spawnMesero(int row, int col) {
        // Spawn Mesero
        spawn("mesero", col * 25, row * 25);
    }
}