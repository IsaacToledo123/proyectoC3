package simuladorrestaurant.ui;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import simuladorrestaurant.config.MesaConfig;
import javafx.scene.shape.Rectangle;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.paint.Color;

public class RestaurantEntityFactory implements EntityFactory {

    public static Entity createMesa(SpawnData data) {
        double cellWidth = (double) FXGL.getSettings().getWidth() / 50;
        double cellHeight = (double) FXGL.getSettings().getHeight() / 50;

        Rectangle mesaVisual = new Rectangle(cellWidth, cellHeight, Color.GREEN.deriveColor(0, 1, 1, 0.5));

        double x = MesaConfig.calcularX(data.get("col"), cellWidth);
        double y = MesaConfig.calcularY(data.get("row"), cellHeight);

        return FXGL.entityBuilder()
                .view(mesaVisual)
                .at(x, y)
                .with("estado", "disponible")
                .build();
    }

    private static final String SPRITE_SHEET_PATH = "file:resources/simuladorrestaurant/assets/character.png";

    @Spawns("mesero1")  // Cambiado para ser único
    public Entity createMesero(SpawnData data) {
        return FXGL.entityBuilder(data)
                .view(createSprite(2, 2))  // Cuadrante 2 de la sección 2 (mesero)
                .collidable()
                .with("estado", "libre")
                .build();
    }

    public Entity createMesero(int x, int y) {
        return createMesero(new SpawnData(x, y));
    }

    @Spawns("cocinero1")  // Cambiado para ser único
    public Entity createCocinero(SpawnData data) {
        return FXGL.entityBuilder(data)
                .view(createSprite(3, 2))  // Cuadrante 2 de la sección 3 (cocinero)
                .collidable()
                .with("estado", "libre")
                .build();
    }

    public Entity createCocinero(int x, int y) {
        return createCocinero(new SpawnData(x, y));
    }

    public Entity createComensal(SpawnData data) {
        return FXGL.entityBuilder(data)
                .view(createSprite(1, 2))  // Cuadrante 2 de la sección 1 (comensal)
                .collidable()
                .with("estado", "esperando")
                .build();
    }

    public Entity createComensal(int x, int y) {
        return createComensal(new SpawnData(x, y));
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
