package com.Poker;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.utils.StateMachine;

public class Poker implements ApplicationListener{ 
    private static boolean server = false;
    public static boolean isServer() {
		return server;
	}

	public static void setServer(boolean server) {
		Poker.server = server;
	}

	@Override
    public void create() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {        
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        StateMachine.getStateMachine().getCurrentState().render();
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