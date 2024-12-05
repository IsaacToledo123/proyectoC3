package com.restaurant.simulator.controladores;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.restaurant.simulator.utils.SpriteLoader;
import javafx.geometry.Point2D;

public class RecepcionistaController {
    private Entity recepcionistEntity;

    public RecepcionistaController(){
        Texture textura = SpriteLoader.getSprite("character.png",6,6,16,16);
        recepcionistEntity = FXGL.entityBuilder()
                .at(350, 540)
                .scale(2,2)
                .view(textura)
                .buildAndAttach();
    }
}
