package com.utils;

public interface GameState {

	public void create();
	public void render();
	public void dispose();
	public void resize(int x, int y);

	
}
