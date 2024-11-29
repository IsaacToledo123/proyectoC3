package simuladorrestaurant.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RestaurantEntityFactory implements EntityFactory {
    // Enum para tipos de entidades
    public enum EntityType {
        MESERO, COCINERO, COMENSAL
    }

    @Spawns("mesero")
    public Entity createMesero(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.MESERO)
                .view(new Rectangle(40, 40, Color.BLUE))
                .collidable()
                .with("estado", "libre")
                .build();
    }

    // Sobrecarga para crear con coordenadas específicas
    public Entity createMesero(int x, int y) {
        return createMesero(new SpawnData(x, y));
    }

    @Spawns("cocinero")
    public Entity createCocinero(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.COCINERO)
                .view(new Rectangle(40, 40, Color.RED))
                .collidable()
                .with("estado", "libre")
                .build();
    }

    // Sobrecarga para crear con coordenadas específicas
    public Entity createCocinero(int x, int y) {
        return createCocinero(new SpawnData(x, y));
    }

    @Spawns("comensal")
    public Entity createComensal(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.COMENSAL)
                .view(new Rectangle(30, 30, Color.GREEN))
                .collidable()
                .with("estado", "esperando")
                .build();
    }

    // Sobrecarga para crear con coordenadas específicas
    public Entity createComensal(int x, int y) {
        return createComensal(new SpawnData(x, y));
    }
}