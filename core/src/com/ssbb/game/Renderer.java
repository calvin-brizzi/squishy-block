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
        // Clear and render everything
        //Gdx.gl.glClearColor(1, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render sprites
        game.batch.begin();
//        game.batch.draw(game.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        game.batch.draw(game.terrain, 0, 0, Gdx.graphics.getWidth(), 20);
        for(Collidable c: game.colliders){
            game.batch.draw(c.sprite, c.sprite.getX(), c.sprite.getY());
        }
        game.batch.end();
    }
}
