package com.Poker;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.utils.AssetManager;
import com.utils.StateMachine;

public class Poker implements ApplicationListener{ 
    
    @Override
    public void create() {
    	Gdx.graphics.setContinuousRendering(true);
        AssetManager.getAssetManager().getFont().setColor(Color.RED);
        Gdx.graphics.requestRendering();
    }

    @Override
    public void dispose() {
    	AssetManager.getAssetManager().getBatch().dispose();
        AssetManager.getAssetManager().getFont().dispose();
    }

    @Override
    public void render() {        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        AssetManager.getAssetManager().getBatch().begin();
        StateMachine.getStateMachine().getCurrentState().render();
        AssetManager.getAssetManager().getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}