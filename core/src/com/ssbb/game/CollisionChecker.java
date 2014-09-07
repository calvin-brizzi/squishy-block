package com.ssbb.game;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by calvin on 2014/09/05.
 */
public class CollisionChecker {
    public static boolean collide(Rectangle r1, Rectangle r2, long[] one, long[] two) {
        int udShift = (int) (r1.y - r2.y);
        int lrShift = (int) (r1.x - r2.x);
        for (int i = 0; i < Math.max(r1.height, r2.height); i++) {
            if (i - udShift > 0 && (i - udShift) < Math.min( two.length, one.length )) {
                if (lrShift > 0) {
                    if (((one[i] << lrShift) & two[(i - udShift)]) != 0) {
                        System.out.println("Collision");
                        return true;
                    } else {
                        System.out.println("No Collision: lrshift>0");
                    }
                } else {
                    if (((one[i] >>> lrShift) & two[((i - udShift))]) != 0) {
                        System.out.print("Collision");
                        return true;
                    }else {
                        System.out.println("No Collision: lrshift<=0");
                    }
                }
            }
        }
        System.out.println("No collision");
        return false;
    }
}
