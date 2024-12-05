package com.restaurant.simulator.controladores;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.restaurant.simulator.utils.SpriteLoader;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.scene.shape.Circle;

public class MeseroController {
    private String name;
    private Circle waiterView;
    private Entity waiterEntity;


    public MeseroController(String name) {
        this.name = name;
        Texture textureWaiter = SpriteLoader.getSprite("pan.png",1,1,32,32);
        waiterEntity= FXGL.entityBuilder()
                .at(150,200)
                .scale(1.5,1.5)
                .viewWithBBox(textureWaiter)
                .buildAndAttach();
    }

    public void moveToKitchen(int tableX, int tableY) {
        FXGL.animationBuilder()
                .duration(javafx.util.Duration.seconds(1))
                .translate(waiterEntity)
                .to(new Point2D(tableX, tableY))
                .buildAndPlay();
    }

    public void moveToTable(Point2D msa) {
        FXGL.animationBuilder()
                .duration(javafx.util.Duration.seconds(1))
                .translate(waiterEntity)
                .to(new Point2D(msa.getX(), msa.getY()))
                .buildAndPlay();
    }

    public void deliverOrder(String order) {
        System.out.println(name + " está entregando la orden: " + order);
    }
}
