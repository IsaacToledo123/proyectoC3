package simuladorrestaurant.ui;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import simuladorrestaurant.config.MesaConfig;
import simuladorrestaurant.concurrencia.MonitorMesas;
import javafx.scene.shape.Rectangle;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class RestaurantEntityFactory implements EntityFactory {

    private static final String SPRITE_SHEET_PATH = "file:resources/simuladorrestaurant/assets/character.png";
    private MonitorMesas monitorMesas;

    // Constructor para inicializar monitorMesas
    public RestaurantEntityFactory(MonitorMesas monitorMesas) {
        this.monitorMesas = monitorMesas;
    }

    public Entity createMesa(SpawnData data) {
        double cellWidth = (double) FXGL.getSettings().getWidth() / 50;
        double cellHeight = (double) FXGL.getSettings().getHeight() / 50;

        Rectangle mesaVisual = new Rectangle(cellWidth, cellHeight, Color.GREEN.deriveColor(0, 1, 1, 0.5));

        // Verifica si la mesa está ocupada y cambia el color
        if (monitorMesas != null && !monitorMesas.isMesaOcupada(data.get("row"), data.get("col"))) {
            mesaVisual.setFill(Color.RED); // Si está ocupada, se pinta de rojo
        }

        double x = MesaConfig.calcularX(data.get("col"), cellWidth);
        double y = MesaConfig.calcularY(data.get("row"), cellHeight);

        return FXGL.entityBuilder()
                .view(mesaVisual)
                .at(x, y)
                .with("estado", "disponible")
                .build();
    }

    public Entity createMesero(SpawnData data) {
        System.out.println("Creando Mesero...");
        return FXGL.entityBuilder(data)
                .view(createSprite(2, 2))  // Cuadrante 2 de la sección 2 (mesero)
                .collidable()
                .with("tipo", "mesero")  // Tipo diferenciado
                .with("estado", "libre")
                .with("id", java.util.UUID.randomUUID().toString()) // Propiedad única
                .build();
    }

    public Entity createCocinero(SpawnData data) {
        System.out.println("Creando Cocinero...");
        return FXGL.entityBuilder(data)
                .view(createSprite(3, 2))  // Cuadrante 2 de la sección 3 (cocinero)
                .collidable()
                .with("tipo", "cocinero")  // Tipo diferenciado
                .with("estado", "ocupado")
                .with("id", java.util.UUID.randomUUID().toString()) // Propiedad única
                .build();
    }

    public Entity createComensal(SpawnData data) {
        System.out.println("Creando Comensal...");
        return FXGL.entityBuilder(data)
                .view(createSprite(1, 2))  // Cuadrante 2 de la sección 1 (comensal)
                .collidable()
                .with("tipo", "comensal")  // Tipo diferenciado
                .with("estado", "esperando")
                .build();
    }

    private ImageView createSprite(int section, int quadrant) {
        Image spriteSheet = new Image(SPRITE_SHEET_PATH);

        double spriteWidth = spriteSheet.getWidth() / 4;
        double spriteHeight = spriteSheet.getHeight() / 2;

        double startX = (section - 1) % 4 * spriteWidth;
        double startY = (section - 1) / 4 * spriteHeight;

        double subSpriteWidth = spriteWidth / 3;
        double subSpriteHeight = spriteHeight / 3;

        double subSpriteStartX = startX + subSpriteWidth;
        double subSpriteStartY = startY + subSpriteHeight;

        Rectangle2D subSpriteRegion = new Rectangle2D(subSpriteStartX, subSpriteStartY, subSpriteWidth, subSpriteHeight);
        ImageView spriteView = new ImageView(spriteSheet);
        spriteView.setViewport(subSpriteRegion);

        return spriteView;
    }
}
