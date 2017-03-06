package com.mcelrea.platformer;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class MyGdxGame extends Game {

    private AssetManager assetManager = new AssetManager();

    @Override
    public void create() {
        //set type of assets to load
        assetManager.setLoader(TiledMap.class,
                new TmxMapLoader(new InternalFileHandleResolver()));

        //set the start screen of the game
        setScreen(new LoadingScreen(this));
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
