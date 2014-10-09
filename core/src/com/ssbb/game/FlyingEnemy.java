package com.ssbb.game;

/**
 * A flying enemy!
 * Created by Calvin on 2014/09/26.
 */
public class FlyingEnemy extends Collidable {

    Player player;
    int speed = 7;
    PathFinder aStar;

    public FlyingEnemy(String name, Player player, PathFinder aStar) {
        super(name);
        this.player = player;
        this.aStar = aStar;
    }

    public void update() {
        int[] next = aStar.nextMove(this.sprite, player.sprite);
        int xo = next[0] * 64 + 32;
        int yo = next[1] * 64 + 32;
        System.out.println(xo + ", " + yo);
        int ySpeed = 7;
        int xSpeed = 1;

        if (Math.abs(xo - x) > Math.abs(yo - y)) {
            xSpeed = 7;
            ySpeed = 1;
        }

        if (x - xo > 0) {
            x -= xSpeed;

        } else if (x - xo < 0) {
            x += xSpeed;
        }

        if (y - yo > 0) {
            y -= ySpeed;

        } else if (y - yo < 0){
            y += ySpeed;
        }
    }
}
