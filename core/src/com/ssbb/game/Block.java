package com.ssbb.game;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created by calvin on 2014/09/26.
 */
public class Block extends Collidable {
    boolean dropping;
    boolean rising;
    boolean canDrop = true;
    int riseTo = 400;
    CameraController cam;
    public Block(String name, CameraController cam){
        super(name);
        this.cam = cam;
    }

    public void update(){
        if (rising){
            y += 30;
            if (y > riseTo - 100){
                canDrop = true;
            }
            if (y > riseTo){
                rising = false;
            }

        }
        if(dropping){
            y -= 50;
            if (y < 64){
                y = 64;
                cam.shake();
                dropping = false;
                rising = true;
                riseTo = Math.min(y + 350, 500);
            }

        }
    }

    public void resolve(Rectangle r){
        if(dropping){
            cam.shake();
            dropping = false;
            rising = true;
            riseTo = Math.min(y + 350, 500);
        }
    }
}
