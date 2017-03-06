package com.mcelrea.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameplayScreen implements Screen {

    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;
    private SpriteBatch batch; //draw graphics
    private ShapeRenderer shapeRenderer; //draw shapes
    private OrthographicCamera camera; //the players view of the world
    private Viewport viewport; //control the view of the world
    private TiledMap map1;
    private MyGdxGame game;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Player player;

    public GameplayScreen(MyGdxGame myGdxGame) {
        game = myGdxGame;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(); //2D camera
        camera.position.set(WORLD_WIDTH/2,WORLD_HEIGHT/2,0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);
        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        batch = new SpriteBatch();
        map1 = game.getAssetManager().get("map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map1,batch);
        mapRenderer.setView(camera);
        player = new Player(200,200);
    }

    @Override
    public void render(float delta) {
        clearScreen();



        mapRenderer.render();

        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        //all graphics drawing goes here
        batch.begin();
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        //all graphics drawing goes here
        shapeRenderer.begin();
        player.drawDebug(shapeRenderer);
        shapeRenderer.end();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

