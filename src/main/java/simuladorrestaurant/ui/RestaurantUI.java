package simuladorrestaurant.ui;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import com.almasb.fxgl.entity.SpawnData;
import javafx.util.Duration;
import com.almasb.fxgl.entity.Entity;
import simuladorrestaurant.config.MesaConfig;
import simuladorrestaurant.concurrencia.MonitorMesas;
import simuladorrestaurant.concurrencia.interfaces.Observer;
import simuladorrestaurant.logica.Restaurant;
import static com.almasb.fxgl.dsl.FXGLForKtKt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestaurantUI extends GameApplication implements Observer {
    private Restaurant restaurant;
    private MonitorMesas monitorMesas;
    private Text textoEstado;
    private int comensalesEnEspera = 0;
    private int mesasDisponibles = 10;
    private List<Entity> entidades = new ArrayList<>();


    private static final int COLUMNS = 30; // Número de columnas de la matriz
    private static final int ROWS = 36;   // Número de filas de la matriz

    private RestaurantEntityFactory entityFactory = new RestaurantEntityFactory(monitorMesas); // Instancia de la fábrica

    // Constructor con MonitorMesas
    public RestaurantUI(MonitorMesas monitorMesas) {
        this.monitorMesas = monitorMesas;
        this.entityFactory = new RestaurantEntityFactory(monitorMesas); // Inicializa la fábrica con monitorMesas
    }

    // Constructor por defecto
    public RestaurantUI() {
        // Aquí inicializamos monitorMesas
        this.monitorMesas = new MonitorMesas(10);
        this.entityFactory = new RestaurantEntityFactory(monitorMesas); // Inicializa la fábrica con monitorMesas
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
        FXGL.getGameWorld().addEntityFactory(new RestaurantEntityFactory(monitorMesas));
        monitorMesas.registrarObserver(this);  // Ya debería estar inicializado
        restaurant = new Restaurant(5, 3, monitorMesas);
        restaurant.iniciarSimulacion();
        restaurant.llegarComensales();
    }

    @Override
    protected void initUI() {
        // Fondo
        Image fondo = new Image(Objects.requireNonNull(getClass().getResource("/simuladorrestaurant/assets/cuked.jpeg")).toExternalForm());
        ImageView imagenFondo = new ImageView(fondo);
        imagenFondo.setFitWidth(getSettings().getWidth());
        imagenFondo.setFitHeight(getSettings().getHeight());
        getGameScene().addUINode(imagenFondo);

        // Establecer el Z-index del fondo (más bajo)
        imagenFondo.setViewOrder(0);

        // Crear texto de estado
        textoEstado = new Text("Comensales en espera: 0");
        textoEstado.setFill(Color.WHITE);
        textoEstado.setX(10);
        textoEstado.setY(20);
        getGameScene().addUINode(textoEstado);

        // Establecer Z-index del texto (por encima del fondo)
        textoEstado.setViewOrder(1);

        // Crear las entidades
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (isCocina(row, col)) {
                    // Crear cocinero
                    crearCocinero(row, col);
                } else if (isCaminar(row, col)) {
                    // Crear mesero
                    crearMesero(row, col);
                } else if (isAsientoSimple(row, col)) {
                    // Crear comensal
                    crearComensal(row, col);
                } else if (MesaConfig.isMesa(row, col)) {
                    // Crear mesa
                    crearMesa(row, col);
                }
            }
        }

        for (Entity entidad : FXGL.getGameWorld().getEntities()) {  // Obtenemos las entidades del mundo
            entidad.setZIndex(2);  // Z-index alto para que se dibujen sobre el fondo y el texto
            System.out.println("Entidad " + entidad.getType() + " tiene Z-index: " + entidad.getZIndex());
        }

        // Actualizar estado periódicamente
        getGameTimer().runAtInterval(() -> {
            comensalesEnEspera = restaurant.getComensalesEnEspera();
            mesasDisponibles = restaurant.getMesasDisponibles();
            System.out.println("Comensales en espera: " + comensalesEnEspera + " | Mesas disponibles: " + mesasDisponibles);
            textoEstado.setText(String.format(
                    "Comensales en espera: %d | Mesas disponibles: %d",
                    comensalesEnEspera,
                    mesasDisponibles
            ));
        }, Duration.seconds(0.5));

    }


    // Método para crear una Mesa
    private void crearMesa(int row, int col) {
        // Calculamos la posición en el mundo basado en el número de filas y columnas
        double x = col * getSettings().getWidth() / COLUMNS;
        double y = row * getSettings().getHeight() / ROWS;
        SpawnData data = new SpawnData(x, y);

        // Agregar las claves 'row' y 'col' a SpawnData
        data.put("row", row);
        data.put("col", col);

        // Depuración: Verifica que el cálculo de la posición es correcto
        System.out.println("Creando entidad Mesa en: (" + x + ", " + y + "), con fila: " + row + " y columna: " + col);

        // Llama a la fábrica para crear la mesa
        Entity mesa = entityFactory.createMesa(data);
        FXGL.getGameWorld().addEntity(mesa);  // Agregamos la entidad al mundo del juego
        entidades.add(mesa);  // Agrega la entidad a la lista
    }

    // Método para crear un Mesero
    private void crearMesero(int row, int col) {
        SpawnData data = new SpawnData(col * getSettings().getWidth() / COLUMNS, row * getSettings().getHeight() / ROWS);
        Entity mesero = entityFactory.createMesero(data);  // Usamos el método de la fábrica
        FXGL.getGameWorld().addEntity(mesero);  // Agregamos la entidad al mundo del juego
        entidades.add(mesero);  // Agrega la entidad a la lista

        // Depuración: Imprimir tipo y posición de la entidad
        System.out.println("Creando entidad Mesero en: (" + (col * getSettings().getWidth() / COLUMNS) + ", " + (row * getSettings().getHeight() / ROWS) + ")");
    }

    // Método para crear un Comensal
    private void crearComensal(int row, int col) {
        SpawnData data = new SpawnData(col * getSettings().getWidth() / COLUMNS, row * getSettings().getHeight() / ROWS);
        Entity comensal = entityFactory.createComensal(data);  // Usamos el método de la fábrica
        FXGL.getGameWorld().addEntity(comensal);  // Agregamos la entidad al mundo del juego
        entidades.add(comensal);  // Agrega la entidad a la lista

        // Depuración: Imprimir tipo y posición de la entidad
        System.out.println("Creando entidad Comensal en: (" + (col * getSettings().getWidth() / COLUMNS) + ", " + (row * getSettings().getHeight() / ROWS) + ")");
    }

    // Método para crear un Cocinero
    private void crearCocinero(int row, int col) {
        // Validamos que los índices row y col estén dentro del rango permitido
        if (row < 0 || row >= ROWS || col < 0 || col >= COLUMNS) {
            System.out.println("Índices fuera de rango: row = " + row + ", col = " + col);
            return;  // Salimos del método si los índices son incorrectos
        }
        SpawnData data = new SpawnData(col * getSettings().getWidth() / COLUMNS, row * getSettings().getHeight() / ROWS);
        Entity cocinero = entityFactory.createCocinero(data);  // Usamos el método de la fábrica
        FXGL.getGameWorld().addEntity(cocinero);  // Agregamos la entidad al mundo del juego
        entidades.add(cocinero);  // Agrega la entidad a la lista

        // Depuración: Imprimir tipo y posición de la entidad
        System.out.println("Creando entidad Cocinero en: (" + (col * getSettings().getWidth() / COLUMNS) + ", " + (row * getSettings().getHeight() / ROWS) + ")");
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

    @Override
    public void actualizar() {

    }
}