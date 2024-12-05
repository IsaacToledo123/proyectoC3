package com.restaurant.simulator.controladores;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.texture.Texture;
import com.restaurant.simulator.utils.SpriteLoader;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CocineroController {
    private String name;
    private Rectangle chefView;
    private Entity entityChef;

    public CocineroController(String name) {
        this.name = name;
        Texture textureChef = SpriteLoader.getSprite("mapache.png",1,1,32,32);
        entityChef = FXGL.entityBuilder()
                .at(150, 100)
                .scale(1.5, 1.5)
                .view(textureChef)
                .buildAndAttach();

    }

    public void startCooking(String order) throws InterruptedException {
        System.out.println(name + " está preparando: " + order);
        FXGL.animationBuilder()
                .duration(javafx.util.Duration.seconds(1))
                .translate(entityChef)
                .to(new Point2D(250,100))
                .buildAndPlay();
        Thread.sleep(5000);
    }

    public void finishCooking(String order) {
        FXGL.animationBuilder()
                .duration(javafx.util.Duration.seconds(1))
                .translate(entityChef)
                .to(new Point2D(150,100))
                .buildAndPlay();
        System.out.println(name + " terminó de preparar: " + order);
    }
}
