package com.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.utils.GameState;
import com.utils.StateMachine;
import com.utils.StateMachine.States;

public class StrategyState implements GameState{
	private Texture textureBackgroud;
	private Stage stage;
	private Table table;
	private BitmapFont font;
	private Skin skin;
	private TextButton serverButton;
	private TextButton clientButton;
	private TextureAtlas atlas;
	
	public StrategyState(){
		font = new BitmapFont(Gdx.files.internal("fonts/trench.fnt"));
		textureBackgroud = new Texture(Gdx.files.internal("img/strategy.png"));
		atlas = new TextureAtlas(Gdx.files.internal("img/buttons.atlas"));
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		create();
	}
	@Override
	public void render() {
		stage.act();
		stage.draw();
	}

	@Override
	public void dispose() {
		textureBackgroud.dispose();
		stage.dispose();
	}
	@Override
	public void create() {
		TextureRegion region = new TextureRegion(textureBackgroud, 0, 0, textureBackgroud.getWidth(), textureBackgroud.getHeight());          
        Image actor = new Image(region);
        stage.addActor(actor);
        
        skin = new Skin(atlas);
        table = new Table(skin);
        table.setBounds(0, -Gdx.graphics.getHeight() * 0.05f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        TextButtonStyle style = new TextButtonStyle();
        style.font = font;
        style.fontColor = Color.BLACK;
        style.up = skin.getDrawable("button_up");
        style.down = skin.getDrawable("button_down");
        style.over = skin.getDrawable("button_down");
        
        serverButton = new TextButton("MESA", style);
        serverButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				StateMachine.getStateMachine().switchState(States.INITIAL);
			}
        	
        });
        clientButton = new TextButton("JOGADOR", style);
        table.add(serverButton).row();
        table.getCell(serverButton).spaceBottom(75.0f);
        table.add(clientButton).row();
        stage.addActor(table);
		
	}

}
