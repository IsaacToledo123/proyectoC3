package com.restaurant.simulator.utils;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Rectangle2D;

public class SpriteLoader {
    public static Texture getSprite(String spritesheetPath, int col, int row, int spriteWidth, int spriteHeight) {
        Texture texture = FXGL.getAssetLoader().loadTexture(spritesheetPath);

        Rectangle2D spriteArea = new Rectangle2D(
                col * spriteWidth,
                row * spriteHeight,
                spriteWidth,
                spriteHeight
        );

        // Extrae el sprite
        return texture.subTexture(spriteArea);
    }
}
