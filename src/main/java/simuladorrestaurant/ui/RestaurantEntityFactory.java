package simuladorrestaurant.ui;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import simuladorrestaurant.actores.Cocinero;
import simuladorrestaurant.actores.Comensal;
import simuladorrestaurant.actores.Mesero;
import simuladorrestaurant.concurrencia.Buffer;
import simuladorrestaurant.concurrencia.BufferComida;
import simuladorrestaurant.concurrencia.MonitorCocina;
import simuladorrestaurant.config.MesaConfig;
import simuladorrestaurant.concurrencia.MonitorMesas;
import simuladorrestaurant.util.EntidadManager;
import javafx.scene.shape.Rectangle;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

import java.util.Objects;

public class RestaurantEntityFactory implements EntityFactory {

    private static final String SPRITE_SHEET_PATH = "file:resources/simuladorrestaurant/assets/character.png";

    private final MonitorMesas monitorMesas;
    private final MonitorCocina monitorCocina;
    private final Buffer bufferOrdenes;
    private final BufferComida bufferComida;
    private final EntidadManager entidadManager;

    // Constructor para inicializar las dependencias
    public RestaurantEntityFactory(MonitorMesas monitorMesas, MonitorCocina monitorCocina, Buffer bufferOrdenes, BufferComida bufferComida, EntidadManager entidadManager) {
        this.monitorMesas = monitorMesas;
        this.monitorCocina = monitorCocina;
        this.bufferOrdenes = bufferOrdenes;
        this.bufferComida = bufferComida;
        this.entidadManager = entidadManager;
    }

    // Método para cargar imágenes
    public Image loadImage(String imagePath) {
        try {
            return new Image(Objects.requireNonNull(getClass().getResource(imagePath)).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error al cargar la imagen: recurso no encontrado. Ruta: " + imagePath);
            return null;
        }
    }

    // Método para inicializar entidades y recursos
    public void initializeEntities() {
        Image fondo = loadImage("/simuladorrestaurant/assets/cuked.jpeg");
        if (fondo != null) {
            ImageView fondoView = new ImageView(fondo);
            fondoView.setFitWidth(FXGL.getAppWidth()); // Tamaño adaptado al ancho de la app
            fondoView.setFitHeight(FXGL.getAppHeight()); // Tamaño adaptado a la altura de la app

            // Añadir el nodo de la imagen al juego
            FXGL.getGameScene().addUINode(fondoView);
            System.out.println("Fondo inicializado con éxito.");
        } else {
            System.out.println("No se pudo inicializar el fondo porque la imagen no está disponible.");
        }
    }

    public Entity createMesa(SpawnData data) {
        double cellWidth = (double) FXGL.getSettings().getWidth() / 50;
        double cellHeight = (double) FXGL.getSettings().getHeight() / 50;

        // Crear visualización de la mesa
        Rectangle mesaVisual = new Rectangle(cellWidth, cellHeight, Color.GREEN.deriveColor(0, 1, 1, 0.5));

        // Validar que SpawnData contenga las claves "row" y "col"
        if (!data.hasKey("row") || !data.hasKey("col")) {
            throw new IllegalArgumentException("SpawnData is missing required keys 'row' or 'col'.");
        }

        int row = data.get("row");
        int col = data.get("col");

        // Cambiar el color si la mesa está ocupada
        if (monitorMesas != null && !monitorMesas.isMesaOcupada(row, col)) {
            mesaVisual.setFill(Color.RED);
        }

        // Calcular las coordenadas basadas en la configuración
        double x = MesaConfig.calcularX(col, cellWidth);
        double y = MesaConfig.calcularY(row, cellHeight);

        // Crear la entidad de la mesa
        return FXGL.entityBuilder()
                .view(mesaVisual)
                .at(x, y)
                .with("estado", "disponible")
                .build();
    }

    public Entity createMesero(SpawnData data) {
        String uniqueId = java.util.UUID.randomUUID().toString();
        if (data == null) {
            throw new IllegalArgumentException("SpawnData no puede ser null");
        }
            // Crear la entidad gráfica
        Entity meseroEntity = FXGL.entityBuilder(data)
                .view(createSprite(2, 2))
                .collidable()
                .with("tipo", "mesero")
                .with("estado", "libre")
                .with("id", uniqueId)
                .build();

        // Registrar la entidad gráfica en el EntidadManager
        entidadManager.registrarEntidad(uniqueId, meseroEntity);

        // Crear la entidad lógica (Mesero)
        Mesero meseroLogico = new Mesero(bufferOrdenes, bufferComida, monitorMesas, monitorCocina, meseroEntity);
        meseroLogico.start();

        return meseroEntity;
    }

    public Entity createCocinero(SpawnData data) {
        String uniqueId = java.util.UUID.randomUUID().toString();
        if (data == null) {
            throw new IllegalArgumentException("SpawnData no puede ser null");
        }
            // Crear la entidad gráfica
        Entity cocineroEntity = FXGL.entityBuilder(data)
                .view(createSprite(3, 2))
                .collidable()
                .with("tipo", "cocinero")
                .with("estado", "ocupado")
                .with("id", uniqueId)
                .build();

        // Registrar la entidad gráfica en el EntidadManager
        entidadManager.registrarEntidad(uniqueId, cocineroEntity);

        // Crear la entidad lógica (Cocinero)
        Cocinero cocineroLogico = new Cocinero(bufferOrdenes, bufferComida, monitorCocina, cocineroEntity);
        cocineroLogico.start();

        return cocineroEntity;
    }

    public Entity createComensal(SpawnData data) {
        String uniqueId = java.util.UUID.randomUUID().toString();
        if (data == null) {
            throw new IllegalArgumentException("SpawnData no puede ser null");
        }
            // Crear la entidad gráfica
        Entity comensalEntity = FXGL.entityBuilder(data)
                .view(createSprite(1, 2))
                .collidable()
                .with("tipo", "comensal")
                .with("estado", "esperando")
                .with("id", uniqueId)
                .build();

        // Registrar la entidad gráfica en el EntidadManager
        entidadManager.registrarEntidad(uniqueId, comensalEntity);

        // Crear la entidad lógica (Comensal)
        Comensal comensalLogico = new Comensal(monitorMesas, bufferOrdenes, bufferComida, "Comensal-" + uniqueId, comensalEntity);
        comensalLogico.start();

        return comensalEntity;
    }

    private ImageView createSprite(int section, int quadrant) {
        Image spriteSheet = new Image(SPRITE_SHEET_PATH);

        double spriteWidth = spriteSheet.getWidth() / 4;
        double spriteHeight = spriteSheet.getHeight() / 2;

        double startX = (section - 1) % 4 * spriteWidth;
        double startY = (section - 1) / 4 * spriteHeight;

        double subSpriteWidth = spriteWidth / 3;
        double subSpriteHeight = spriteHeight / 3;

        double subSpriteStartX = startX + (subSpriteWidth * (quadrant % 3));
        double subSpriteStartY = startY + (subSpriteHeight * (quadrant / 3));

        // Imprimir las coordenadas y dimensiones para verificar
        System.out.println("StartX: " + startX + " StartY: " + startY);
        System.out.println("SubSpriteStartX: " + subSpriteStartX + " SubSpriteStartY: " + subSpriteStartY);
        System.out.println("SubSpriteWidth: " + subSpriteWidth + " SubSpriteHeight: " + subSpriteHeight);

        Rectangle2D subSpriteRegion = new Rectangle2D(subSpriteStartX, subSpriteStartY, subSpriteWidth, subSpriteHeight);
        ImageView spriteView = new ImageView(spriteSheet);
        spriteView.setViewport(subSpriteRegion);

        return spriteView;
    }
}
