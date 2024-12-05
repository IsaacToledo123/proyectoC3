package com.restaurant.simulator.controladores;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.restaurant.simulator.utils.SpriteLoader;
import javafx.geometry.Point2D;

public class MesaController {
    private Entity tableEntity;

    public MesaController(Point2D position) {

        Texture tableTexture = SpriteLoader.getSprite("interior.png",3,8,32,128);

        tableEntity = FXGL.entityBuilder()
                .at(position)
                .viewWithBBox(tableTexture)
                .buildAndAttach();
    }
}
