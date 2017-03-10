package com.mcelrea.platformer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Player {
    public static final float COLLISION_WIDTH=50;
    public static final float COLLISION_HEIGHT=70;
    private static final float MAX_X_SPEED = 5;
    private static final float MAX_Y_SPEED = 5;
    private static final float MAX_JUMP_DISTANCE = 3 * COLLISION_HEIGHT;
    private Rectangle collisionRectangle;
    private float x;
    private float y;
    private float xSpeed;
    private float ySpeed;
    private boolean blockJump = false;
    private float jumpYDistance = 0;
    private Animation walking;
    private TextureRegion jumping;
    private TextureRegion standing;
    private TextureRegion ducking;
    private float animationTimer = 0;
    private boolean amDucking = false;

    public Player(float x, float y, Texture t) {
        this.x = x;
        this.y = y;
        collisionRectangle = new Rectangle(x,y,
                COLLISION_WIDTH,
                COLLISION_HEIGHT);
        TextureRegion[] regions = TextureRegion.split(t,(int)COLLISION_WIDTH,
                (int)COLLISION_HEIGHT)[0];
        walking = new Animation(0.25f,regions[1],regions[2]);
        walking.setPlayMode(Animation.PlayMode.LOOP);
        standing = regions[0];
        ducking = regions[3];
        jumping = regions[4];
    }

    public void updatePosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionRectangle();
    }

    public void updateCollisionRectangle() {
        collisionRectangle.setPosition(x,y);
    }

    public void update(float delta) {
        animationTimer += delta;
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

        //duck
        if(input.isKeyPressed(Input.Keys.S)) {
            amDucking = true;
        }
        else {
            amDucking = false;
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

    public void landed() {
        blockJump = false;
        jumpYDistance = 0;
        ySpeed = 0;
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

    public void draw(SpriteBatch batch) {
        TextureRegion toDraw = standing;

        if(xSpeed == 0 && amDucking) {
            toDraw = ducking;
        }

        if(ySpeed != 0) {
            toDraw = jumping;
        }
        else if(xSpeed > 0) {
            toDraw = walking.getKeyFrame(animationTimer);
            if(toDraw.isFlipX())
                toDraw.flip(true, false);
        }
        else if(xSpeed < 0) {
            toDraw = walking.getKeyFrame(animationTimer);
            if(!toDraw.isFlipX())
                toDraw.flip(true, false);
        }

        batch.draw(toDraw,x,y);
    }

    public Rectangle getCollisionRectangle() {
        return collisionRectangle;
    }
}
