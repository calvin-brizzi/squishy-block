package com.ssbb.game;

/**
 * Created by calvin on 2014/09/26.
 */
public class FlyingEnemy extends Collidable {
    Player player;
    int speed = 7;

    public FlyingEnemy(String name, Player player){
        super(name);
        this.player = player;
    }

    public void update(){
        int xd = x - player.x;
        int yd = y - player.y;
        int dtp = xd * xd + yd * yd;
        if(dtp < 1000000){
            if(xd > 5){
                x = x - speed;
            } else if (xd < -5){
                x = x + speed;
            }
            if (yd > 5){
                y = y - speed;
            } else if (yd < -5) {
                y = y + speed;
            }

            if(y < 40){
                y = 40;
            }
        }
    }

}
