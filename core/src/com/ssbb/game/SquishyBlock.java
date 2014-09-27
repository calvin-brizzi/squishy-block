package com.ssbb.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * SquishyBlock, a game about not much, so far
 * basically a "first playable" to check that collision works
 * by Calvin Brizzi
 */

public class SquishyBlock extends ApplicationAdapter {

    public static final int FRICTION = 1;
    public static final int GRAVITY = 1;

    // Used to draw sprites and their bounding boxes
    public SpriteBatch batch;
    public ShapeRenderer shapeRenderer;

    // Our entities and the list to store them
    public Player player;
    public GroundEnemy blocky;
    public FlyingEnemy bee;
    public Block tetronimo;
    public ArrayList<Collidable> colliders = new ArrayList<Collidable>();

    public InputHandler pc = new InputHandler();
    public Renderer r = new Renderer(this);

    // Some Textures
    public Texture background;
    public Texture terrain;
    public OrthographicCamera cam;
    public TiledMap map;

    int mapWidth;
    int mapHeight;
    MapLayer layer;
    MapObjects objects;
    Array<RectangleMapObject> obstacles;
    OrthogonalTiledMapRenderer renderer;
    CameraController cameraController;

    // Constants and a grid for collision detection
    public Grid grid = new Grid(480, 640, this);


    @Override
    public void create() {
        map = new TmxMapLoader().load("t.tmx");
        layer = map.getLayers().get("collisio");
        objects = layer.getObjects();
        obstacles = objects.getByType(RectangleMapObject.class);
        renderer = new OrthogonalTiledMapRenderer(map, 1);

        MapProperties properties = map.getProperties();
        mapWidth = properties.get("width", Integer.class) * 64;
        mapHeight = properties.get("height", Integer.class) * 64;

        System.out.print(mapWidth +" --- " + mapHeight);

        // Usual initializers
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        player = new Player("player.png", this);
        blocky = new GroundEnemy("enemy.png", player);
        bee = new FlyingEnemy("bee.png", player);

        cam = new OrthographicCamera();
        cam.setToOrtho(false, 1200, 640);
        cam.update();

        cameraController = new CameraController(cam, player, mapWidth, mapHeight);
        tetronimo = new Block("block.png", cameraController);
        tetronimo.setPolygon(tetrominoVert());
        tetronimo.x = 500;
        tetronimo.y = 500;
        colliders.add(tetronimo);
        colliders.add(player);
       colliders.add(blocky);
       colliders.add(bee);

        background = new Texture("background.png");
        terrain = new Texture("terrain.png");

        // Initial positions
        player.x = 40;
        player.y = 20;


        blocky.x = 900;
        blocky.y = 64;

        bee.x = 2000;
        bee.y = 200;
        bee.flip(true);


    }

    @Override
    public void render() {

        pc.update(player, tetronimo);

        player.update();
        blocky.update();
        bee.update();
        tetronimo.update();

        grid.resolveCollisions();
        for(RectangleMapObject o: obstacles){
            if (player.sprite.getBoundingRectangle().overlaps(o.getRectangle())) {
                player.resolve(o.getRectangle());
            }
            if (tetronimo.sprite.getBoundingRectangle().overlaps(o.getRectangle())){
                tetronimo.resolve(o.getRectangle());
            }
        }

        r.render();


        // Render bounding polygons if SPACE is pressed
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            shapeRenderer.setProjectionMatrix(cam.combined );
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.polygon(tetronimo.bounding.getTransformedVertices());
            shapeRenderer.polygon(player.bounding.getTransformedVertices());
            shapeRenderer.polygon(blocky.bounding.getTransformedVertices());
            shapeRenderer.polygon(bee.bounding.getTransformedVertices());
//            shapeRenderer.polygon( Collidable.rectToPoly(obstacles.get(0).getRectangle()).getTransformedVertices());
            shapeRenderer.end();
        }
    }

    public float[] tetrominoVert() {
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
