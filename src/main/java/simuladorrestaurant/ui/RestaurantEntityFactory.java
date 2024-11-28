package simuladorrestaurant.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.image.ImageView;

public class RestaurantEntityFactory implements EntityFactory {
    public enum EntityType {
        MESERO, COCINERO, COMENSAL
    }

    // Para Mesero, se usa una imagen (por ejemplo, "mesero.png")
    @Spawns("mesero")
    public Entity createMesero(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.MESERO)
                .view(FXGL.getAssetLoader().loadTexture("mesero.png")) // Directamente usar el Texture
                .collidable()
                .build();
    }

    // Sobrecarga del método para coordenadas x, y
    public Entity createMesero(int x, int y) {
        SpawnData data = new SpawnData(x, y);
        return createMesero(data);
    }

    // Para Cocinero, se usa una imagen (por ejemplo, "cocinero.png")
    @Spawns("cocinero")
    public Entity createCocinero(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.COCINERO)
                .view(FXGL.getAssetLoader().loadTexture("cocinero.png")) // Directamente usar el Texture
                .collidable()
                .build();
    }

    public Entity createCocinero(int x, int y) {
        SpawnData data = new SpawnData(x, y);
        return createCocinero(data);
    }

    // Para Comensal, se usa una imagen (por ejemplo, "comensal.png")
    @Spawns("comensal")
    public Entity createComensal(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.COMENSAL)
                .view(FXGL.getAssetLoader().loadTexture("comensal.png")) // Directamente usar el Texture
                .collidable()
                .build();
    }

    public Entity createComensal(int x, int y) {
        SpawnData data = new SpawnData(x, y);
        return createComensal(data);
    }
}
