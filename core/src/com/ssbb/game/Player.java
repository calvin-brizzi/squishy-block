package com.ssbb.game;

/**
 * Created by calvin on 2014/09/26.
 */
public class Player extends Collidable {
    public int direction;
    public int ax, ay;

    public Player(String name){
        super(name);
        direction = 1;
    }

    public void update(){
        // Player movement
        ay -= SquishyBlock.GRAVITY;
        y += ay;
        if (y < 64) {
            y = 64;
            ay = 0;
        }

        ax -= SquishyBlock.FRICTION;

        if (ax < 0) {
            ax = 0;
        }

        x += ax * direction;
    }
}
