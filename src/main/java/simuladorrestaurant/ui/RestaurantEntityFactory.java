package simuladorrestaurant.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RestaurantEntityFactory implements EntityFactory {
    public enum EntityType {
        MESERO, COCINERO, COMENSAL
    }

    @Spawns("mesero")
    public Entity createMesero(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.MESERO)
                .viewWithBBox(new Rectangle(30, 30, Color.BLUE))
                .collidable()
                .build();
    }

    // Sobrecarga del método para coordenadas x, y
    public Entity createMesero(int x, int y) {
        SpawnData data = new SpawnData(x, y);
        return createMesero(data);
    }

    // Hacer lo mismo para Cocinero y Comensal
    @Spawns("cocinero")
    public Entity createCocinero(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.COCINERO)
                .viewWithBBox(new Rectangle(30, 30, Color.RED))
                .collidable()
                .build();
    }

    public Entity createCocinero(int x, int y) {
        SpawnData data = new SpawnData(x, y);
        return createCocinero(data);
    }

    @Spawns("comensal")
    public Entity createComensal(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.COMENSAL)
                .viewWithBBox(new Rectangle(20, 20, Color.GREEN))
                .collidable()
                .build();
    }

    public Entity createComensal(int x, int y) {
        SpawnData data = new SpawnData(x, y);
        return createComensal(data);
    }
}