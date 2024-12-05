package simuladorrestaurant.util;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import simuladorrestaurant.ui.RestaurantEntityFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.function.BiPredicate;

public class EntidadManager {
    private final Map<String, Entity> entidadMapa = new HashMap<>();
    private RestaurantEntityFactory entityFactory;
    private final int ROWS = 36; // Filas en la cuadrícula
    private final int COLUMNS = 30; // Columnas en la cuadrícula

    // Constructor
    public EntidadManager(RestaurantEntityFactory entityFactory) {
        if (entityFactory == null) {
            throw new IllegalArgumentException("RestaurantEntityFactory no puede ser null");
        }
        this.entityFactory = entityFactory;
    }

    // Método para establecer el factory después de la creación
    public void setEntityFactory(RestaurantEntityFactory entityFactory) {
        if (entityFactory == null) {
            throw new IllegalArgumentException("RestaurantEntityFactory no puede ser null");
        }
        this.entityFactory = entityFactory;
    }

    // Método para generar puntos válidos de spawn
    public List<SpawnData> generarPuntosSpawn(BiPredicate<Integer, Integer> areaValida) {
        List<SpawnData> puntosSpawn = new ArrayList<>();
        double cellWidth = FXGL.getAppWidth() / (double) COLUMNS;
        double cellHeight = FXGL.getAppHeight() / (double) ROWS;

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (areaValida.test(row, col)) {
                    puntosSpawn.add(new SpawnData(col * cellWidth, row * cellHeight));
                }
            }
        }
        return puntosSpawn;
    }

    // Registrar una entidad lógica y gráfica
    public void registrarEntidadLogica(String id, String tipo, BiPredicate<Integer, Integer> areaValida) {
        List<SpawnData> puntosSpawn = generarPuntosSpawn(areaValida);

        if (!puntosSpawn.isEmpty()) {
            int index = FXGL.random(0, puntosSpawn.size() - 1);
            SpawnData spawnData = puntosSpawn.get(index);

            Entity entidadGrafica = crearEntidadGrafica(tipo, spawnData);

            if (entidadGrafica != null) {
                FXGL.getGameWorld().addEntity(entidadGrafica);
                registrarEntidad(id, entidadGrafica);
            }
        } else {
            System.out.println("No hay puntos válidos para el tipo: " + tipo);
        }
    }

    // Crear entidad gráfica basada en el tipo
    public Entity crearEntidadGrafica(String tipo, SpawnData spawnData) {
        if (entityFactory == null) {
            throw new IllegalStateException("RestaurantEntityFactory no está inicializado.");
        }

        Entity entidadGrafica = null;
        switch (tipo.toLowerCase()) {
            case "mesa":
                entidadGrafica = entityFactory.createMesa(spawnData);
                break;
            case "mesero":
                entidadGrafica = entityFactory.createMesero(spawnData);
                break;
            case "comensal":
                entidadGrafica = entityFactory.createComensal(spawnData);
                break;
            case "cocinero":
                entidadGrafica = entityFactory.createCocinero(spawnData);
                break;
            default:
                System.out.println("Tipo desconocido: " + tipo);
        }
        return entidadGrafica;
    }

    // Registrar una entidad con un ID único
    public void registrarEntidad(String id, Entity entity) {
        entidadMapa.put(id, entity);
    }

    // Obtener una entidad por su ID
    public Entity obtenerEntidad(String id) {
        return entidadMapa.get(id);
    }

    // Eliminar una entidad por su ID
    public void eliminarEntidad(String id) {
        entidadMapa.remove(id);
    }
}
