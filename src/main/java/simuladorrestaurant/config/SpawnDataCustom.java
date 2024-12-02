package simuladorrestaurant.config;

import java.util.HashMap;
import java.util.Map;

public class SpawnDataCustom {
    private double x;
    private double y;
    private Map<String, Integer> data = new HashMap<>();

    public SpawnDataCustom(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void put(String key, int value) {
        data.put(key, value);
    }

    public Integer get(String key) {
        return data.get(key);
    }
}
