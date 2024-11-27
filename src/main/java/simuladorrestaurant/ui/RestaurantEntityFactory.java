package simuladorrestaurant.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RestaurantEntityFactory implements EntityFactory {

    // Enum para los tipos de entidad
    public enum EntityType {
        MESERO,
        COCINERO,
        COMENSAL
    }

    public Entity createMesero(int x, int y) {
        return FXGL.entityBuilder()
                .at(x, y)
                .type(EntityType.MESERO)
                .viewWithBBox(new Rectangle(30, 30, Color.BLUE))
                .collidable()
                .build();
    }

    public Entity createCocinero(int x, int y) {
        return FXGL.entityBuilder()
                .at(x, y)
                .type(EntityType.COCINERO)
                .viewWithBBox(new Rectangle(30, 30, Color.RED))
                .collidable()
                .build();
    }

    public Entity createComensal(int x, int y) {
        return FXGL.entityBuilder()
                .at(x, y)
                .type(EntityType.COMENSAL)
                .viewWithBBox(new Rectangle(20, 20, Color.GREEN)) // Cambiar la forma si lo deseas
                .collidable()
                .build();
    }
}
