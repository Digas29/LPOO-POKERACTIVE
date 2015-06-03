package com.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.utils.GameState;

public class ChooseRoomState implements GameState {
	private Texture textureBackgroud;
	private Stage stage;
	private Table table;
	private TextureAtlas atlas;
	private Skin skin;
	private BitmapFont fontBlack;
	private BitmapFont fontRed;
	
	public ChooseRoomState(){
		atlas = new TextureAtlas(Gdx.files.internal("img/buttons.atlas"));
		skin = new Skin(atlas);
		textureBackgroud = new Texture(Gdx.files.internal("img/strategy.png"));
		stage = new Stage();
		fontBlack = new BitmapFont(Gdx.files.internal("fonts/trench.fnt"));
		fontRed = new BitmapFont(Gdx.files.internal("fonts/trench_red.fnt"));
		create();
	}
	@Override
	public void create() {

	}

	@Override
	public void render() {

	}

	@Override
	public void dispose() {

	}

}
