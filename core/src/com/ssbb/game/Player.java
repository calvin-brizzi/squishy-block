package com.ssbb.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by calvin on 2014/09/26.
 */
public class Player extends Collidable {
    SquishyBlock game;

    public int ax, ay, prevx, prevy;

    public Player(String name, SquishyBlock game) {
        super(name);
        direction = 1;
        this.game = game;
    }

    public void update() {
        prevx = x;
        prevy = y;
        // Player movement
        ay -= SquishyBlock.GRAVITY;
        y += ay;
        if (y < 64) {
            y = 64;
            ay = 0;
            grounded = true;
        }
        if (ax > 0) {
            ax -= SquishyBlock.FRICTION;
        } else if (ax < 0) {
            ax += SquishyBlock.FRICTION;
        }
        x += ax;

        if (x < 0) {
            x = 0;
        }
        while (x > game.mapWidth - this.sprite.getWidth()) {
            x--;
        }
    }

    public void resolve(Rectangle r) {
        Rectangle pr = sprite.getBoundingRectangle();
        Rectangle intersection = new Rectangle();
        Intersector.intersectRectangles(pr, r, intersection);
        if (intersection.width > intersection.height + 10) {
            // y direction smaller
            solveY(intersection);
            ay = 0;
        } else {
            if (intersection.height < 20 && pr.y == intersection.y){
                y += intersection.height;
            }
            solveX(intersection);
            ax = 0;

        }
        sprite.setPosition(x, y);
    }

    private void solveY(Rectangle intersection) {
        Rectangle pr =  sprite.getBoundingRectangle();

        if (pr.y != intersection.y) {
            prevy = y;
            y -= intersection.height;
        } else {
            prevy = y;
            y += intersection.height;
            grounded = true;
        }
    }

    private void solveX(Rectangle intersection) {
        Rectangle pr =  sprite.getBoundingRectangle();
        if (pr.x == intersection.x) {
            prevx = x;
            x += intersection.width;
        } else {
            prevx = x;
            x -= intersection.width;
        }
    }
}
