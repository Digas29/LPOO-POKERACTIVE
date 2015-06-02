package com.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.utils.GameState;

public class MainMenu implements GameState{
	private Texture texture;
	private Sprite menu;
	String string;
	
	public MainMenu(){
		string = "";
		texture = new Texture(Gdx.files.internal("img/teste.png"));
		menu = new Sprite(texture);
	}
	@Override
	public void render() {
		//menu.draw(AssetManager.getAssetManager().getBatch());
		//AssetManager.getAssetManager().getFont().draw(AssetManager.getAssetManager().getBatch(), string, 200, 200);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}
}
