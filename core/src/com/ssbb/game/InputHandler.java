package com.ssbb.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by calvin on 2014/09/24.
 */
public class InputHandler {

    public void update(Player player, Block block){
        // Player controls

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !player.colliding) {
            player.flip(false);
            player.ax = 10;
//            if (player.x > 640) {
//                player.x = -40;
//            }
            player.direction = 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !player.colliding) {
            player.flip(true);
            player.ax = 10;
//            if (player.x < -20) {
//                player.x = 640;
//            }
            player.direction = -1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !player.colliding) {
            if (player.y == 64) {
                player.ay += 20;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !player.colliding) {
            player.y -= 10;
            if (player.y < 20) {
                player.y = 20;
            }
        }


        // Tetronimo commands
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            block.y += 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            block.y -= 10;
            if (block.y < 20) {
                block.y = 20;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            block.x -= 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            block.x += 10;
        }

    }
}
