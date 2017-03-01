package com.mcelrea.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameplayScreen implements Screen {

    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;
    private SpriteBatch batch; //draw graphics
    private ShapeRenderer shapeRenderer; //draw shapes
    private Camera camera; //the players view of the world
    private Viewport viewport; //control the view of the world


    public GameplayScreen(MyGdxGame myGdxGame) {
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(); //2D camera
        camera.position.set(WORLD_WIDTH/2,WORLD_HEIGHT/2,0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        clearScreen();

        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        //all graphics drawing goes here
        batch.begin();
        batch.end();
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

