package com.states;


import com.Poker.Poker;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
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
		stage = new Stage(new FitViewport(1920, 1080));
	}
	@Override
	public void render() {
		stage.act();
		stage.draw();
	}

	@Override
	public void dispose() {
		textureBackgroud.dispose();
		skin.dispose();
		stage.dispose();
	}
	@Override
	public void create() {
		Gdx.input.setInputProcessor(stage);
		TextureRegion region = new TextureRegion(textureBackgroud, 0, 0, textureBackgroud.getWidth(), textureBackgroud.getHeight());          
        Image actor = new Image(region);
        stage.addActor(actor);
        
        skin = new Skin(atlas);
        table = new Table(skin);
        table.setBounds(0, 0, stage.getWidth(), stage.getHeight());
        
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
				Poker.setServer(true);
				StateMachine.getStateMachine().switchState(States.CHOOSE_NAME);
			}
        	
        });
        clientButton = new TextButton("JOGADOR", style);
        clientButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				Poker.setServer(false);
				StateMachine.getStateMachine().switchState(States.CHOOSE_NAME);
			}
        	
        });
        table.add(serverButton).padTop(Gdx.graphics.getHeight()*0.15f).center().row();
        table.getCell(serverButton).spaceBottom(75.0f);
        table.add(clientButton).center().row();
        stage.addActor(table);
		
	}
	
	@Override
	public void resize(int x, int y) {
		stage.getViewport().update(x, y, true);
	}

}
