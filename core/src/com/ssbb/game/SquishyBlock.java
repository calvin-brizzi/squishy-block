package com.ssbb.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * SquishyBlock, a game about not much, so far
 * basically a "first playable" to check that collision works
 * by Calvin Brizzi
 */

public class SquishyBlock extends ApplicationAdapter {

    // So we know what to render
    public enum GameState {
        RUN,
        MENU,
        PAUSE,
        DEAD,
        END
    }

    GameState state = GameState.MENU;

    public static final int FRICTION = 1;
    public static final int GRAVITY = 1;

    // Used to draw sprites, map and to start the game
    public SpriteBatch batch;
    public Renderer gameRendered = new Renderer(this);
    OrthogonalTiledMapRenderer tiledMapRenderer;
    Creator creator = new Creator(this);
    public ShapeRenderer lines;


    // Our entities and the list to store them
    public Player player;
    public Block tetronimo;
    public ArrayList<Collidable> colliders = new ArrayList<Collidable>();

    // Input handler, camera and that jazz
    public InputHandler inputHandler = new InputHandler();
    public OrthographicCamera camera;
    public CameraController cameraController;

    // Map variables and objects
    public int mapWidth;
    public int mapHeight;
    public MapLayer layer;
    public MapObjects objects;
    public Array<RectangleMapObject> obstacles;
    public TiledMap map;

    // Menus and HUD textures
    public Texture menu;
    public Texture pause;
    public Texture dead;
    public Texture win;
    public Texture heart;
    public Texture halfHeart;
    public Texture noHeart;

    // "Grid" for collision detection
    public Grid grid = new Grid(60 * 64, this);
    int counter = 0;

    // Let's animate!
    int animationColumns = 3;
    int animationRows = 4;
    Animation playerAnimation;
    Texture animationTexture;
    TextureRegion[] animationFrames;
    TextureRegion currentFrame;
    float animationState;

    // A*
    PathFinder aStar;
    int level;
    String[] levels = {"astar.tmx", "t.tmx", "three.tmx"};

    @Override
    public void create() {
        // Pass off most responsibilities
        creator.newGame(levels[level]);
        menu = new Texture("menu.png");
        pause = new Texture("pause.png");
        dead = new Texture("dead.png");
        win = new Texture("win.png");
    }

    @Override
    public void render() {

        // Let's see what to do
        switch (state) {
            case RUN:
                counter++;
                if (counter == 2){
                    aStar.createGrid();
                }
                // Make everyone update, then render
                inputHandler.update(player, tetronimo);
                grid.resolveCollisions();
                gameRendered.render();

                if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    state = GameState.PAUSE;
                }
                //state = GameState.PAUSE;
                break;
            case MENU:
                level = 0;
                drawMenu(menu);
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    creator.newGame(levels[level]);
                    state = GameState.RUN;
                }
                break;
            case PAUSE:
                drawMenu(pause);
                if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                    state = GameState.RUN;
                    camera.position.x = player.x;
                    camera.position.y = player.y;
                }
                break;
            case DEAD:
                drawMenu(dead);
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    state = GameState.MENU;
                }
                break;
            case END:
                drawMenu(win);
                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    creator.newGame(levels[level]);
                    state = GameState.RUN;
                }
                break;
        }
    }

    public void playerDead() {
        state = GameState.DEAD;
    }

    public void win() {
        state = GameState.END;
        level ++;
        if(level >= levels.length){
            level = 0;
        }
    }

    public void drawMenu(Texture menuToDraw) {
        // Sets the camera position to 0,0 so we can see the menu
        camera.position.x = 0;
        camera.position.y = 0;
        cameraController.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(menuToDraw, 0, 0);
        batch.end();
    }
}
