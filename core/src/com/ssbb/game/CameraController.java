package com.ssbb.game;

import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.Random;

/**
 * Created by calvin on 2014/09/27.
 */
public class CameraController {
    Random mizer = new Random();
    OrthographicCamera cam;
    Player player;
    int mapWidth;
    int mapHeight;
    int camWidth;
    int camHeight;
    int shake;
    boolean shaking;

    public CameraController(OrthographicCamera camera, Player player, int width, int height) {
        this.player = player;
        cam = camera;
        camWidth = (int) cam.viewportWidth / 2;
        camHeight = (int) cam.viewportHeight / 2;
        mapWidth = width - camWidth;
        mapHeight = height - camHeight;
    }

    public void shake() {
        shaking = true;
        shake = 30;
    }

    public void update() {

        // We want to be a little ahead of the player
        cam.position.x -= (cam.position.x - (player.x + 400 * player.direction)) / 32;

        // Horizontal axis
        if (cam.position.x < camWidth) {
            cam.position.x = camWidth;
        } else if (cam.position.x >= mapWidth) {
            cam.position.x = mapWidth;
        }

        // Vertical axis
        if (cam.position.y <= 0) {
            cam.position.y = camHeight;
        } else if (cam.position.y >= mapHeight) {
            cam.position.y = mapHeight;
        }
        // Screen Shake
        if (shaking) {
            shake -= 1;
            if (shake < 1) {
                shaking = false;
                cam.position.y = camHeight;
            } else if (shake % 2 == 0) {
                cam.position.x += mizer.nextInt(shake);
                cam.position.y -= mizer.nextInt(shake);
            } else {
                cam.position.x -= mizer.nextInt(shake);
                cam.position.y += mizer.nextInt(shake);
            }
        }
        cam.update();
    }
}
