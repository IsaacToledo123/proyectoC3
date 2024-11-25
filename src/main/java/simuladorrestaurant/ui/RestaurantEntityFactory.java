package simuladorrestaurant.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.ViewComponent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RestaurantEntityFactory implements EntityFactory {

    // Enum para los tipos de entidad
    public enum EntityType {
        MESERO,
        COCINERO,
        COMENSAL
    }

    @Override
    public Entity newEntity(String entityType, double x, double y) {
        switch (entityType) {
            case "MESERO":
                return createMesero(x, y);
            case "COCINERO":
                return createCocinero(x, y);
            case "COMENSAL":
                return createComensal(x, y);
            default:
                return null;
        }
    }

    public Entity createMesero(double x, double y) {
        Rectangle rect = new Rectangle(30, 30, Color.BLUE);
        Entity mesero = new Entity();
        mesero.setPosition(x, y);

        // Agregar el componente de colisión
        mesero.addComponent(new CollidableComponent(true));

        // Agregar el componente de vista
        ViewComponent viewComponent = new ViewComponent();
        viewComponent.addChild(rect);  // Usar setView para añadir la vista
        mesero.addComponent(viewComponent);  // Añadir el componente de vista a la entidad

        return mesero;
    }

    public Entity createCocinero(double x, double y) {
        Rectangle rect = new Rectangle(30, 30, Color.RED);
        Entity cocinero = new Entity();
        cocinero.setPosition(x, y);

        // Agregar colisión
        cocinero.addComponent(new CollidableComponent(true));

        // Agregar el componente de vista
        ViewComponent viewComponent = new ViewComponent();
        viewComponent.addChild(rect);  // Usar setView para añadir la vista
        cocinero.addComponent(viewComponent);  // Añadir el componente de vista a la entidad

        return cocinero;
    }

    public Entity createComensal(double x, double y) {
        Rectangle rect = new Rectangle(20, 20, Color.GREEN);
        Entity comensal = new Entity();
        comensal.setPosition(x, y);

        // Agregar colisión
        comensal.addComponent(new CollidableComponent(true));

        // Agregar el componente de vista
        ViewComponent viewComponent = new ViewComponent();
        viewComponent.addChild(rect);  // Usar setView para añadir la vista
        comensal.addComponent(viewComponent);  // Añadir el componente de vista a la entidad

        return comensal;
    }
}
