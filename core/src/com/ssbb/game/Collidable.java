package com.ssbb.game;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

/**
 * A object to keep things that can collide
 * Created by calvin on 2014/09/05.
 */
public class Collidable {
    public int x, y;
    public Sprite sprite;
    public boolean colliding;
    public Polygon bounding;

    public Collidable(String name) {
        // name is the filename, and will setup the sprite and mask
        sprite = new Sprite(new Texture(name));
        bounding = rectToPoly(sprite.getBoundingRectangle());
    }

    public static Polygon rectToPoly(Rectangle r) {
        // Generates a polygon from a rectangle
        return new Polygon(new float[]{
                r.x + r.width, r.y,
                r.x + r.width, r.y + r.height,
                r.x, r.y + r.height,
                r.x, r.y
        });

    }

    public void setPolygon(float[] coord){
        // Allows setting of better-fitting polygons
        bounding = new Polygon(coord);
    }

    public void flip(boolean t) {
        sprite.setFlip(t, false);
        // This does not work for pixel perfect collision
        // Would need to generate a mask for flipped sprite
        // Final game will not use pixel perfect collision
    }
}
