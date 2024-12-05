package com.restaurant.simulator.controladores;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.restaurant.simulator.utils.SpriteLoader;
import javafx.geometry.Point2D;

public class ComensalController {
    private Entity dinerEntity;

    public ComensalController(String dinerName) {
        Texture texture = SpriteLoader.getSprite("character.png",1,1,32,32);
        dinerEntity = FXGL.entityBuilder()
                .at(700, 650)
                .scale(1.5,1.5)
                .view(texture)
                .buildAndAttach();
    }

    public void moveToLobby(int tableX, int tableY) {
        FXGL.animationBuilder()
                .onFinished(()->{
                    FXGL.animationBuilder()
                            .duration(javafx.util.Duration.seconds(1))
                            .translate(dinerEntity)
                            .to(new Point2D(tableX,550))
                            .buildAndPlay();
                })
                .duration(javafx.util.Duration.seconds(1))
                .translate(dinerEntity)
                .to(new Point2D(tableX, tableY))
                .buildAndPlay();
    }

    public void moveToTable(double tableX, double tableY){
        FXGL.animationBuilder()
                .onFinished(()->{
                    FXGL.animationBuilder()
                            .duration(javafx.util.Duration.seconds(1))
                            .translate(dinerEntity)
                            .to(new Point2D(tableX, tableY))
                            .buildAndPlay();
                })
                .duration(javafx.util.Duration.seconds(1))
                .translate(dinerEntity)
                .to(new Point2D(350, 500))
                .buildAndPlay();
    }

    public void exitRestaurant() {
        // Movimiento de salida del restaurante
        FXGL.animationBuilder()
                .onFinished(()->{
                    FXGL.animationBuilder()
                            .onFinished(()->{
                                FXGL.animationBuilder()
                                        .duration(javafx.util.Duration.seconds(1))
                                        .translate(dinerEntity)
                                        .to(new Point2D(400, 650))
                                        .buildAndPlay();
                            })
                            .duration(javafx.util.Duration.seconds(1))
                            .translate(dinerEntity)
                            .to(new Point2D(400, 550))
                            .buildAndPlay();
                })
                .duration(javafx.util.Duration.seconds(1))
                .translate(dinerEntity)
                .to(new Point2D(250, 300))
                .buildAndPlay();
    }
}
