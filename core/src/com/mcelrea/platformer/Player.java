package com.mcelrea.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    public static final float COLLISION_WIDTH=50;
    public static final float COLLISION_HEIGHT=70;
    private static final float MAX_X_SPEED = 2;
    private static final float MAX_Y_SPEED = 2;
    private static final float MAX_JUMP_DISTANCE = 3 * COLLISION_HEIGHT;
    private Rectangle collisionRectangle;
    private float x;
    private float y;
    private float xSpeed;
    private float ySpeed;
    private boolean blockJump = false;
    private float jumpYDistance = 0;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        collisionRectangle = new Rectangle(x,y,
                COLLISION_WIDTH,
                COLLISION_HEIGHT);
    }

    public void updatePosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionRectangle();
    }

    public void updateCollisionRectangle() {
        collisionRectangle.setPosition(x,y);
    }

    public void update() {
        Input input = Gdx.input;
        //right
        if(input.isKeyPressed(Input.Keys.D)) {
            xSpeed = MAX_X_SPEED;
        }
        //left
        else if(input.isKeyPressed(Input.Keys.A)) {
            xSpeed = -MAX_X_SPEED;
        }
        //no x-movement
        else {
            xSpeed = 0;
        }


        //jump
        if(input.isKeyPressed(Input.Keys.W) && !blockJump) {
            ySpeed = MAX_Y_SPEED;
            jumpYDistance += ySpeed;
            blockJump = jumpYDistance > MAX_JUMP_DISTANCE;
        }
        //he's not jumping
        else {
            ySpeed = -MAX_Y_SPEED;
            blockJump = jumpYDistance > 0;
        }

        x += xSpeed;
        y += ySpeed;
        updateCollisionRectangle();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void drawDebug(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(collisionRectangle.x,
                collisionRectangle.y,
                collisionRectangle.width,
                collisionRectangle.height);
    }

    public Rectangle getCollisionRectangle() {
        return collisionRectangle;
    }
}
