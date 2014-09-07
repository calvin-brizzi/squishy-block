package com.ssbb.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class SquishyBlock extends ApplicationAdapter {
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;

    Collidable player;
    Collidable enemy;
    Collidable bee;

    ArrayList<Collidable> colliders = new ArrayList<Collidable>();

    Texture background;
    Texture terrain;

    int px, py;
    int ex, ey, ed;
    int bx, by, bd;

    int ya;
    int gravity = 1;

    int xa, xd;
    int friction = 1;


    @Override
    public void create() {

        px = 0;
        py = 20;

        ex = 300;
        ey = 20;
        ed = -1;

        bx = 300;
        by = 100;
        bd = 1;

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        player = new Collidable("player.png");
        enemy = new Collidable("enemy.png");
        bee = new Collidable("bee.png");

        colliders.add(player);
        colliders.add(enemy);
        colliders.add(bee);

        bee.flip(true);

        background = new Texture("background.png");
        terrain = new Texture("terrain.png");

    }

    @Override
    public void render() {

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && !player.colliding) {
            player.flip(false);
            xa = 10;
            if (px > 640) {
                px = -40;
            }
            xd = 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && !player.colliding) {
            player.flip(true);
            xa = 10;
            if (px < -20) {
                px = 640;
            }
            xd = -1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !player.colliding) {
            if (py == 20) {
                ya += 20;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !player.colliding) {
            py -= 10;
            if (py < 20) {
                py = 20;
            }
        }

        ex += ed * 5;

        if (ex < 100) {
            ed = 1;
        } else if (ex > 400) {
            ed = -1;
        }
        bx += bd * 2;
        by += bd * 2;

        if (bx < 300) {
            bd = 1;
            bx = 300;
            bee.flip(true);
        } else if (bx > 500) {
            bd = -1;
            bx = 500;
            bee.flip(false);
        }


        for (Collidable s : colliders) {
            if (s == player) {
                continue;
            }
            if (player.sprite.getBoundingRectangle().overlaps(s.sprite.getBoundingRectangle())) {
                if (CollisionChecker.collide(player.sprite.getBoundingRectangle(), s.sprite.getBoundingRectangle(), player.mask, player.mask)) {
                    Vector2 center1 = new Vector2();
                    Vector2 center2 = new Vector2();
                    player.sprite.getBoundingRectangle().getCenter(center1);
                    s.sprite.getBoundingRectangle().getCenter(center2);
                    center1 = center1.sub(center2);
                    ya = (int) center1.y / 3;
                    xa = Math.abs((int) center1.x / 3);
                    if (center1.x > 0) {
                        xd = 1;
                    } else {
                        xd = -1;
                    }
                    px += xa * xd;
                    player.colliding = true;
                }
            }else {
                player.colliding = false;
            }
        }

        ya -= gravity;
        py += ya;
        if (py < 20) {
            py = 20;
            ya = 0;
        }

        xa -= friction;

        if (xa < 0) {
            xa = 0;
        }

        px += xa * xd;


        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        player.sprite.setPosition(px, py);
        enemy.sprite.setPosition(ex, ey);
        bee.sprite.setPosition(bx, by);


        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(terrain, 0, 0, Gdx.graphics.getWidth(), 20);
        batch.draw(enemy.sprite, enemy.sprite.getX(), enemy.sprite.getY());
        batch.draw(player.sprite, player.sprite.getX(), player.sprite.getY());
        batch.draw(bee.sprite, bee.sprite.getX(), bee.sprite.getY());
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(player.sprite.getBoundingRectangle().x, player.sprite.getBoundingRectangle().y, player.sprite.getBoundingRectangle().width, player.sprite.getBoundingRectangle().height);
            shapeRenderer.rect(enemy.sprite.getBoundingRectangle().x, enemy.sprite.getBoundingRectangle().y, enemy.sprite.getBoundingRectangle().width, enemy.sprite.getBoundingRectangle().height);
            shapeRenderer.rect(bee.sprite.getBoundingRectangle().x, bee.sprite.getBoundingRectangle().y, bee.sprite.getBoundingRectangle().width, bee.sprite.getBoundingRectangle().height);
            shapeRenderer.end();
        }
    }
}
