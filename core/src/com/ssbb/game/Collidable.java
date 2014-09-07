package com.ssbb.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


/**
 * Created by calvin on 2014/09/05.
 */
public class Collidable {
    public Sprite sprite;
    public long[] mask;
    public boolean colliding;

    public Collidable(String name) {
        sprite = new Sprite(new Texture(name));
        mask = newMask(new Pixmap(new FileHandle(name)));
    }

    public long[] newMask(Pixmap map) {
        int h = map.getHeight();
        int w = map.getWidth();

        long[] mask = new long[h];
        for (int i = 0; i < h; i++) {
            long line = 0;
            for (int j = 0; j < w; j++) {
                if(line> Long.MAX_VALUE /2 -5){
                    System.out.print("OVERFLOW!!!");
                }
                line = line * 2;
                if ((map.getPixel(j, i) & 0x000000ff) != 0) {
                    line += 1;
                }
            }
            mask[h-1-i] = line;
        }
        testPrint(mask);
        return mask;

    }

    public void flip(boolean t) {
        //sprite.setFlip(t, false);
        // This does not work for pixel perfect collision
        // Would need to generate a mask for flipped sprite
        // Final game will not use pixel perfect collision
    }

    public void testPrint(long[] mask){

       // For testing
        for (int i = (mask.length)-1; i >= 0; i--) {
            System.out.println(String.format("%64s", Long.toBinaryString(mask[i])).replace(' ', '0'));
        }
        System.out.print("\n\n\n");
    }

}
