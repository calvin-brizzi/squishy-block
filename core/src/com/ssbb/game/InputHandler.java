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
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !player.colliding) {
            player.flip(true);
            player.ax = -10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !player.colliding) {
            if (player.grounded) {
                player.ay += 22;
                player.grounded = false;
            }
        }

        // Tetronimo commands
        if (Gdx.input.isKeyPressed(Input.Keys.W) && !block.dropping && !block.rising) {
            block.y += 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) && !block.dropping && !block.rising) {
            block.y -= 10;
            if (block.y < 20) {
                block.y = 20;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) && !block.dropping && !block.rising) {
            block.x -= 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && !block.dropping && !block.rising) {
            block.x += 10;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !block.dropping && block.canDrop) {
            block.dropping = true;
            block.canDrop = false;
        }

    }
}
