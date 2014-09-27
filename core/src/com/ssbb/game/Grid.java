package com.ssbb.game;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * A grid for more efficient collision detection
 * Created by calvin on 2014/09/07.
 */
public class Grid {
    private ArrayList<Collidable>[][] grid = new ArrayList[3][3];
    private int height;
    private int width;
    SquishyBlock game;

    public Grid(int height, int width, SquishyBlock game) {
        // A simple constructor
        this.height = height;
        this.width = width;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j] = new ArrayList<Collidable>();
            }
        }
        this.game = game;
    }

    public void clear() {
        // Clear all objects
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grid[i][j].clear();
            }
        }
    }

    public void add(Collidable entity) {
        // Need to find all grid positions it touches
        // Because our sprites < size of a grid cell, we can just check positions of vertices
        Rectangle bounds = entity.sprite.getBoundingRectangle();

        int[] cells = cell(bounds.x, bounds.y);
        safeAdd(cells, entity);
        cells = cell(bounds.x + bounds.width, bounds.y);
        safeAdd(cells, entity);
        cells = cell(bounds.x + bounds.width, bounds.y + bounds.height);
        safeAdd(cells, entity);
        cells = cell(bounds.x, bounds.y + bounds.height);
        safeAdd(cells, entity);
    }

    private void safeAdd(int[] cells, Collidable entity) {
        // Add only if not present
        if (!grid[cells[0]][cells[1]].contains(entity)) {
            grid[cells[0]][cells[1]].add(entity);
        }
    }

    public int[] cell(float x, float y) {
        // Checks the correct location in most naive way ever
        int[] cells = new int[2];
        if (x < height / 3) {
            cells[0] = 0;
        } else if (x < height * 2 / 3) {
            cells[0] = 1;
        } else {
            cells[0] = 2;
        }

        if (y < width / 3) {
            cells[1] = 0;
        } else if (x < width * 2 / 3) {
            cells[1] = 1;
        } else {
            cells[1] = 2;
        }

        return cells;
    }

    public ArrayList<Collidable> get(int x, int y) {
        // Returns all elements within a given grid
        return grid[x][y];
    }

    public void resolveCollisions() {

        ArrayList<Collidable> colliders = game.colliders;
        ArrayList<Collidable> toDelete = new ArrayList<Collidable>();
        Player player = game.player;
        // Adding everything to the grid
        this.clear();
        for (Collidable c : colliders) {
            if (!c.dead) {
                this.add(c);
                c.sprite.setPosition(c.x, c.y);
                c.bounding.setPosition(c.x, c.y);
            } else {
                toDelete.add(c);
            }
        }

        for (Collidable c : toDelete){
            colliders.remove(c);
        }

        // Checking each grid cell for more than one entity
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                ArrayList<Collidable> collidables = this.get(i, j);

                if (collidables.size() > 1) {
                    // Loop through the collidables and check them against all others
                    for (int indexOfFirst = 0; indexOfFirst < collidables.size(); indexOfFirst++) {
                        // Start second index at index of first + 1 to avoid double checking
                        for (int indexOfSecond = indexOfFirst + 1; indexOfSecond < collidables.size(); indexOfSecond++) {
                            Collidable first = collidables.get(indexOfFirst);
                            Collidable second = collidables.get(indexOfSecond);

                            if (Intersector.overlapConvexPolygons(first.bounding, second.bounding)) {
                                // Check bounding polygons first

                                // push the first element away proportionally to how much it collided
                                // The below just gets nice numbers for the push
                                Vector2 center1 = new Vector2();
                                Vector2 center2 = new Vector2();

                                first.sprite.getBoundingRectangle().getCenter(center1);
                                second.sprite.getBoundingRectangle().getCenter(center2);

                                center1 = center1.sub(center2); // Subtracting vectors to get direction
                                if (first == game.tetronimo && game.tetronimo.dropping) {
                                    second.dead = true;
                                }
                                if (first == player) {
                                    // Accelerate the player the right amount in the right direction
                                    player.ay = (int) center1.y / 3;
                                    player.ax = Math.max(Math.abs((int) center1.x / 3), 10);
                                    if (center1.x > 0) {
                                        player.direction = 1;
                                    } else {
                                        player.direction = -1;
                                    }
                                    player.x += player.ax * player.direction;
                                    player.colliding = true;
                                }
                                second.colliding = true;
                            }

                            second.colliding = false;
                            first.colliding = false;

                        }
                    }
                }
            }
        }

        for (Collidable c : colliders) {
            c.sprite.setPosition(c.x, c.y);
            c.bounding.setPosition(c.x, c.y);
        }
    }

}
