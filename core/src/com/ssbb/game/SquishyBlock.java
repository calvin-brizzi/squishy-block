package com.ssbb.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;

/**
 * SquishyBlock, a game about not much, so far
 * basically a "first playable" to check that collision works
 * by Calvin Brizzi
 */

public class SquishyBlock extends ApplicationAdapter {

    // Used to draw sprites and their bounding boxes
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;

    // Our entities and the list to store them
    Collidable player;
    Collidable blocky;
    Collidable bee;
    Collidable tetronimo;
    ArrayList<Collidable> colliders = new ArrayList<>();

    // Some Textures
    Texture background;
    Texture terrain;

    // Player position and accelerations
    int playerX, playerY;
    int playerAccelerationY;
    int playerAccelerationX, playerDirection;

    // Tetromino position
    int tetrominoX, tetrominoY;

    // Enemy positions and directions
    int blockyX, blockyY, blockyDirection;
    int beeX, beeY, beeDirection;

    // Constants and a grid for collision detection
    int friction = 1;
    int gravity = 1;
    Grid grid = new Grid(480, 640);


    @Override
    public void create() {

        // Usual initializers
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        player = new Collidable("player.png");
        blocky = new Collidable("enemy.png");
        bee = new Collidable("bee.png");
        tetronimo = new Collidable("block.png");
        tetronimo.setPolygon(tetrominoVert());

        colliders.add(player);
        colliders.add(blocky);
        colliders.add(bee);
        colliders.add(tetronimo);

        background = new Texture("background.png");
        terrain = new Texture("terrain.png");

        // Initial positions
        playerX = 40;
        playerY = 20;

        tetrominoX = 100;
        tetrominoY = 100;

        blockyX = 300;
        blockyY = 20;
        blockyDirection = -1;

        beeX = 300;
        beeY = 200;
        beeDirection = 1;
        bee.flip(true);

    }

    @Override
    public void render() {

        // Player controls

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !player.colliding) {
            player.flip(false);
            playerAccelerationX = 10;
            if (playerX > 640) {
                playerX = -40;
            }
            playerDirection = 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !player.colliding) {
            player.flip(true);
            playerAccelerationX = 10;
            if (playerX < -20) {
                playerX = 640;
            }
            playerDirection = -1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !player.colliding) {
            if (playerY == 20) {
                playerAccelerationY += 20;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !player.colliding) {
            playerY -= 10;
            if (playerY < 20) {
                playerY = 20;
            }
        }

        // Player movement
        playerAccelerationY -= gravity;
        playerY += playerAccelerationY;
        if (playerY < 20) {
            playerY = 20;
            playerAccelerationY = 0;
        }

        playerAccelerationX -= friction;

        if (playerAccelerationX < 0) {
            playerAccelerationX = 0;
        }

        playerX += playerAccelerationX * playerDirection;

        // Tetronimo commands
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            tetrominoY += 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            tetrominoY -= 10;
            if (tetrominoY < 20) {
                tetrominoY = 20;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            tetrominoX -= 10;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            tetrominoX += 10;
        }

        // Enemy movement
        blockyX += blockyDirection * 5;

        if (blockyX < 100) {
            blockyDirection = 1;
        } else if (blockyX > 400) {
            blockyDirection = -1;
        }

        beeX += beeDirection * 2;
        beeY += beeDirection * 2;

        if (beeX < 300) {
            beeDirection = 1;
            beeX = 300;
            bee.flip(true);
        } else if (beeX > 500) {
            beeDirection = -1;
            beeX = 500;
            bee.flip(false);
        }

        // Update Sprite positions and keep the polygons with them
        player.sprite.setPosition(playerX, playerY);
        player.bounding.setPosition(playerX, playerY);

        blocky.sprite.setPosition(blockyX, blockyY);
        blocky.bounding.setPosition(blockyX, blockyY);

        bee.sprite.setPosition(beeX, beeY);
        bee.bounding.setPosition(beeX, beeY);

        tetronimo.sprite.setPosition(tetrominoX, tetrominoY);
        tetronimo.bounding.setPosition(tetrominoX, tetrominoY);

        // Adding everything to the grid
        grid.clear();
        for (Collidable c : colliders) {
            grid.add(c);
        }

        // Checking each grid cell for more than one entity
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                ArrayList<Collidable> collidables = grid.get(i, j);

                if (collidables.size() > 1) {
                    // Loop through the collidables and check them against all others
                    for (int indexOfFirst = 0; indexOfFirst < collidables.size(); indexOfFirst++) {
                        // Start second index at index of first + 1 to avoid double checking
                        for (int indexOfSecond = indexOfFirst + 1; indexOfSecond < collidables.size(); indexOfSecond++) {
                            Collidable first = collidables.get(indexOfFirst);
                            Collidable second = collidables.get(indexOfSecond);

                            if (Intersector.overlapConvexPolygons(first.bounding, second.bounding)) {
                                // Check bounding polygons first
                                if (CollisionChecker.collide(first.sprite.getBoundingRectangle(), second.sprite.getBoundingRectangle(), first.mask, second.mask)) {
                                    // If the pixel perfect collision in detected
                                    // push the first element away proportionally to how much it collided
                                    // The below just gets nice numbers for the push
                                    Vector2 center1 = new Vector2();
                                    Vector2 center2 = new Vector2();

                                    first.sprite.getBoundingRectangle().getCenter(center1);
                                    second.sprite.getBoundingRectangle().getCenter(center2);

                                    center1 = center1.sub(center2); // Subtracting vectors to get direction

                                    if(first == player) {
                                        // Accelerate the player the right amount in the right direction
                                        playerAccelerationY = (int) center1.y / 3;
                                        playerAccelerationX = Math.max(Math.abs((int) center1.x / 3), 10);
                                        if (center1.x > 0) {
                                            playerDirection = 1;
                                        } else {
                                            playerDirection = -1;
                                        }
                                        playerX += playerAccelerationX * playerDirection;
                                        player.colliding = true;
                                    }
                                    second.colliding = true;
                                }
                            } else {
                                second.colliding = false;
                                first.colliding = false;
                            }
                        }
                    }
                }
            }
        }


        // Clear and render everything
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render sprites
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(terrain, 0, 0, Gdx.graphics.getWidth(), 20);
        batch.draw(blocky.sprite, blocky.sprite.getX(), blocky.sprite.getY());
        batch.draw(player.sprite, player.sprite.getX(), player.sprite.getY());
        batch.draw(bee.sprite, bee.sprite.getX(), bee.sprite.getY());
        batch.draw(tetronimo.sprite, tetronimo.sprite.getX(), tetronimo.sprite.getY());
        batch.end();

        // Render bounding polygons if SPACE is pressed
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.polygon(tetronimo.bounding.getTransformedVertices());
            shapeRenderer.polygon(player.bounding.getTransformedVertices());
            shapeRenderer.polygon(blocky.bounding.getTransformedVertices());
            shapeRenderer.polygon(bee.bounding.getTransformedVertices());
            shapeRenderer.end();
        }
    }

    public float[] tetrominoVert(){
        // Basically just for testing, allows for better fitting polygon around the tetronimo
        return new float[]{
                60, 0,
                60, 20,
                40, 20,
                40, 40,
                20, 40,
                20, 20,
                0, 20,
                0, 0

        };
    }
}
