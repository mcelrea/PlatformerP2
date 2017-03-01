package com.mcelrea.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;

public class MyGdxGame extends Game {

    private AssetManager assetManager = new AssetManager();

    @Override
    public void create() {
        //set the start screen of the game
        setScreen(new LoadingScreen(this));
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
