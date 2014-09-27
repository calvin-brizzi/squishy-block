package com.ssbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

/**
 * Created by calvin on 2014/09/26.
 */
public class Renderer {
    SquishyBlock game;

    public Renderer(SquishyBlock game){
        this.game = game;
    }
    public void render(){
        game.cameraController.update();

        game.renderer.setView(game.cam);
        game.renderer.render();

        game.batch.setProjectionMatrix(game.cam.combined);
        // Render sprites
        game.batch.begin();
        for(Collidable c: game.colliders){
            game.batch.draw(c.sprite, c.sprite.getX(), c.sprite.getY());
        }
        game.batch.end();
    }
}
