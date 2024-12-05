package simuladorrestaurant.ui;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import com.almasb.fxgl.entity.SpawnData;
import javafx.util.Duration;
import com.almasb.fxgl.entity.Entity;
import simuladorrestaurant.concurrencia.MonitorCocina;
import simuladorrestaurant.config.MesaConfig;
import simuladorrestaurant.concurrencia.Buffer;
import simuladorrestaurant.concurrencia.BufferComida;
import simuladorrestaurant.concurrencia.MonitorMesas;
import simuladorrestaurant.concurrencia.interfaces.Observer;
import simuladorrestaurant.logica.Restaurant;
import simuladorrestaurant.util.EntidadManager;

import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantUI extends GameApplication implements Observer {

    private static final int COLUMNS = 30; // Número de columnas de la matriz
    private static final int ROWS = 36;   // Número de filas de la matriz

    private int comensalesEnEspera = 0;
    private int mesasDisponibles = 10;

    private List<Entity> entidades = new ArrayList<>();
    private static MonitorMesas monitorMesas;
    private static BufferComida bufferComida;
    private Restaurant restaurant;
    private static Buffer bufferOrdenes;
    private Text textoEstado;
    private RestaurantEntityFactory entityFactory;
    private  EntidadManager entidadManager; // Instancia de la fábrica
    private static final MonitorCocina monitorCocina = new MonitorCocina();

    // Constructor con MonitorMesas
    public RestaurantUI() {
        this.monitorMesas = new MonitorMesas(10); // Valor predeterminado
        this.bufferOrdenes = new Buffer(10); // Instancia predeterminada
        this.bufferComida = new BufferComida(5); // Instancia predeterminada
        this.entityFactory = new RestaurantEntityFactory(this.monitorMesas,
                monitorCocina,
                this.bufferOrdenes,
                this.bufferComida,
                entidadManager);  // Pasando el EntidadManager a la fábrica
    }

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
        // Inicializar el EntidadManager y RestaurantEntityFactory
        EntidadManager entidadManager = new EntidadManager(null);
        RestaurantEntityFactory restaurantEntityFactory = new RestaurantEntityFactory(monitorMesas, monitorCocina, bufferOrdenes, bufferComida, entidadManager);
        entidadManager.setEntityFactory(restaurantEntityFactory);
        this.entidadManager = entidadManager;

        // Inicializar el MonitorMesas si no ha sido inicializado
        if (this.monitorMesas == null) {
            this.monitorMesas = new MonitorMesas(10);
        }

        // Se agrega la fábrica de entidades al mundo del juego
        FXGL.getGameWorld().addEntityFactory(new RestaurantEntityFactory(
                monitorMesas,
                monitorCocina,
                bufferOrdenes,
                bufferComida,
                entidadManager));

        // Registrar el observer para el monitorMesas
        monitorMesas.registrarObserver(this);

        // Crear e iniciar el restaurante, pasando el EntidadManager
        restaurant = new Restaurant(2, 1, monitorMesas, entidadManager);  // Se pasa el EntidadManager
        restaurant.iniciarSimulacion();
        restaurant.llegarComensales(monitorCocina, entidadManager);  // Usamos EntidadManager aquí también

        // Crear entidades usando puntos válidos de spawn
        // Crear meseros
        int numMeseros = 2;
        for (int i = 0; i < numMeseros; i++) {
            // Crear SpawnData con las coordenadas de fila y columna
            SpawnData spawnData = new SpawnData(i, 0); // i = fila, 0 = columna
            entidadManager.crearEntidadGrafica("mesero", spawnData);
        }

        // Crear cocineros
        int numCocineros = 1;
        for (int i = 0; i < numCocineros; i++) {
            // Crear SpawnData con las coordenadas de fila y columna
            SpawnData spawnData = new SpawnData(i, 1); // i = fila, 1 = columna
            entidadManager.crearEntidadGrafica("cocinero", spawnData);
        }

        // Crear mesas
        int numMesas = 10;
        for (int i = 0; i < numMesas; i++) {
            // Crear SpawnData con las coordenadas de fila y columna
            SpawnData spawnData = new SpawnData(2, i); // 2 = fila, i = columna
            entidadManager.crearEntidadGrafica("mesa", spawnData);
        }
    }

    @Override
    protected void initUI() {
        // Crear o acceder a una instancia de RestaurantEntityFactory
        RestaurantEntityFactory entityFactory = new RestaurantEntityFactory(monitorMesas, monitorCocina, bufferOrdenes, bufferComida, entidadManager);
        // Llamar al método initializeEntities para agregar la imagen de fondo
        Platform.runLater(() -> {
            System.out.println("Intentando cargar la imagen de fondo...");
            entityFactory.initializeEntities();
        });

        // Crear la cuadrícula y asignar colores
        double cellWidth = getSettings().getWidth() / COLUMNS;
        double cellHeight = getSettings().getHeight() / ROWS;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(cellWidth, cellHeight);
                rect.setX(col * cellWidth);
                rect.setY(row * cellHeight);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(0.5);

                // Usar funciones de validación para asignar colores
                if (isPuntoComensal(row, col)) {
                    rect.setFill(Color.YELLOW);
                } else if (isCocina(row, col)) {
                    rect.setFill(Color.RED);
                } else if (isCaminar(row, col)) {
                    rect.setFill(Color.LIGHTGRAY);
                } else if (isAsientoSimple(row, col)) {
                    rect.setFill(Color.BLUE);
                } else if (MesaConfig.isMesa(row, col)) {
                    rect.setFill(Color.GREEN);
                } else {
                    rect.setFill(Color.TRANSPARENT);
                }

                getGameScene().addUINode(rect);
            }
        }

        // Crear texto de estado
        textoEstado = new Text("Comensales en espera: 0");
        textoEstado.setFill(Color.WHITE);
        textoEstado.setX(10);
        textoEstado.setY(20);
        getGameScene().addUINode(textoEstado);

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

    // Punto de comensales
    private boolean isPuntoComensal(int row, int col) {return row == 35 && (col >= 14 && col <= 15);}

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
}