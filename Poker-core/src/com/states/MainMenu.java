package com.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.utils.AssetManager;
import com.utils.GameState;

public class MainMenu implements GameState, InputProcessor{
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
		menu.draw(AssetManager.getAssetManager().getBatch());
		AssetManager.getAssetManager().getFont().draw(AssetManager.getAssetManager().getBatch(), string, 200, 200);
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if(screenX >= 420 && screenX <= 780){
			if(screenY >= 160 && screenY <= 250)
				string = "Button1";
			if(screenY >= 335 && screenY <= 430)
				string = "Button2";
			if(screenY >= 735 && screenY <= 830)
				string = "Button3";
		}
		else{
			string = "";
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
