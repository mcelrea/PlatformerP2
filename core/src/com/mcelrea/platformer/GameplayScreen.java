package com.mcelrea.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;

public class GameplayScreen implements Screen {

    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 600;
    private static final int CELL_SIZE = 40;
    private SpriteBatch batch; //draw graphics
    private ShapeRenderer shapeRenderer; //draw shapes
    private OrthographicCamera camera; //the players view of the world
    private Viewport viewport; //control the view of the world
    private TiledMap map1;
    private MyGdxGame game;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Player player;
    BitmapFont font;
    String output = "TEST";

    public GameplayScreen(MyGdxGame myGdxGame) {
        game = myGdxGame;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(); //2D camera
        //camera.position.set(WORLD_WIDTH/2,WORLD_HEIGHT/2,0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT,camera);
        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        batch = new SpriteBatch();
        map1 = game.getAssetManager().get("map1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map1,batch);
        mapRenderer.setView(camera);
        player = new Player(200,200,(Texture)game.getAssetManager().get("playerSpriteSheet.png"));
        font = new BitmapFont();
        //keep text at the same location on the screen
    }

    @Override
    public void render(float delta) {
        clearScreen();

        output = "";
        userInput();
        player.update(delta);
        handlePlayerCollision();

        camera.position.set(player.getX(),WORLD_HEIGHT/2,0);
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        //batch.setProjectionMatrix(camera.projection);
        //batch.setTransformMatrix(camera.view);
        //all graphics drawing goes here
        batch.begin();
        //keep text at the same location on the screen
        //keep text at the same location on the screen
        Vector3 screenCoords = new Vector3(5,5,0);
        Vector3 worldCoords = camera.unproject(screenCoords);
        font.draw(batch,output, worldCoords.x,worldCoords.y);
        player.draw(batch);
        batch.end();

        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        //all graphics drawing goes here
        shapeRenderer.begin();
        player.drawDebug(shapeRenderer);
        shapeRenderer.end();
    }

    private void userInput() {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            Vector3 screenCoords = new Vector3(x,y,0);
            Vector3 worldCoords = camera.unproject(screenCoords);
            CollisionCell cell = getCell(worldCoords.x, worldCoords.y);
            output = "clicked on cell id: " + cell.getId();
        }
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

    public Array<CollisionCell> whichCellsDoesPlayerCover() {
        float x = player.getX();
        float y = player.getY();
        Array<CollisionCell> cellsCovered = new Array<CollisionCell>();
        float cellRow = x / CELL_SIZE;
        float cellCol = y / CELL_SIZE;

        int bottomLeftCellRow = MathUtils.floor(cellRow);
        int bottomLeftCellCol = MathUtils.floor(cellCol);

        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer)map1.getLayers().get(0);

        cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(bottomLeftCellRow,
                bottomLeftCellCol), bottomLeftCellRow, bottomLeftCellCol));

        //if(cellRow % 1 != 0 && cellCol % 1 != 0) {
            //int topRightCellRow = bottomLeftCellRow + 1;
            //int topRightCellCol = bottomLeftCellCol + 1;
            //cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(topRightCellRow,
                    //topRightCellCol), topRightCellRow, topRightCellCol));
            cellsCovered.add(getCell(player.getX()+player.COLLISION_WIDTH,player.getY()+player.COLLISION_HEIGHT));
            cellsCovered.add(getCell(player.getX()+player.COLLISION_WIDTH,player.getY()+player.COLLISION_HEIGHT/2));
            cellsCovered.add(getCell(player.getX(),player.getY()+player.COLLISION_HEIGHT/2));
        //}
        //if(cellRow % 1 != 0) {
            int bottomRightCellRow = bottomLeftCellRow + 1;
            int bottomRightCellCol = bottomLeftCellCol;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(bottomRightCellRow,
                    bottomRightCellCol), bottomRightCellRow, bottomRightCellCol));

        //}
        //if(cellCol % 1 != 0) {
            int topLeftCellRow = bottomLeftCellRow;
            int topLeftCellCol = bottomLeftCellCol + 1;
            cellsCovered.add(new CollisionCell(tiledMapTileLayer.getCell(topLeftCellRow,
                    topLeftCellCol), topLeftCellRow, topLeftCellCol));
        //}

        return cellsCovered;
    }

    private Array<CollisionCell> filterOutNonCollisionCells(Array<CollisionCell> cells) {
        for(Iterator<CollisionCell> iter = cells.iterator(); iter.hasNext();) {
            CollisionCell collisionCell = iter.next();

            if(collisionCell.isEmpty()) {
                iter.remove();
            }
            else if(collisionCell.getId() == 4) {
                iter.remove();
            }
        }

        return cells;
    }

    private int convertXtoRow(float x) {
        float cellRow = x / CELL_SIZE;
        return MathUtils.floor(cellRow);
    }

    private int convertYtoCol(float y) {
        float cellCol = y / CELL_SIZE;
        return MathUtils.floor(cellCol);
    }

    private CollisionCell getCell(float x, float y) {
        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer)map1.getLayers().get(0);
        int cellRow = convertXtoRow(x);
        int cellCol = convertYtoCol(y);

        return new CollisionCell(tiledMapTileLayer.getCell(cellRow,
                cellCol), cellRow, cellCol);
    }

    public void handlePlayerCollision() {
        Array<CollisionCell> playerCells = whichCellsDoesPlayerCover();
        output += "     on cells: ";
        for(int i=0; i < playerCells.size; i++) {
            output += (playerCells.get(i).getId() + ", ");
        }
        playerCells = filterOutNonCollisionCells(playerCells);
        for(CollisionCell cell: playerCells) {
            float cellLevelX = cell.getCellRow() * CELL_SIZE;
            float cellLevelY = cell.getCellCol() * CELL_SIZE;
            Rectangle intersection = new Rectangle();
            Intersector.intersectRectangles(player.getCollisionRectangle(),
                    new Rectangle(cellLevelX,cellLevelY,CELL_SIZE,CELL_SIZE),
                    intersection);
            if(intersection.getHeight() < intersection.getWidth()) {

                if(intersection.getY() == player.getY()) {
                    player.updatePosition(player.getX(), intersection.getY() + intersection.getHeight());
                    player.landed();
                }
                if(intersection.getY() > player.getY()) {
                    player.updatePosition(player.getX(), intersection.getY() - player.COLLISION_HEIGHT);
                }
            }
            else if (intersection.getWidth() < intersection.getHeight()) {
                if(intersection.getX() == player.getX()) {
                    player.updatePosition(intersection.getX() + intersection.getWidth(),
                            player.getY());
                }
                if(intersection.getX() > player.getX()) {
                    player.updatePosition(intersection.getX() - player.COLLISION_WIDTH,
                            player.getY());
                }
            }
        }
    }
}

