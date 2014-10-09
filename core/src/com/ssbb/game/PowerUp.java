package com.ssbb.game;

/**
 * A simple power up, just restores life for now, but could be improved
 * Created by Calvin on 2014/09/28.
 */
public class PowerUp extends Collidable {
    Player player;

    public PowerUp(String name, Player player) {
        super(name);
        this.player = player;
    }

    public void get() {
        player.life++;
        // Marks itself as removable
        dead = true;
    }
}
