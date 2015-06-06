package com.states;


import com.Poker.Poker;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.OnscreenKeyboard;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.connections.ClientConnection;
import com.connections.ServerConnection;
import com.utils.ConnectionStrategy;
import com.utils.GameState;
import com.utils.StateMachine;
import com.utils.StateMachine.States;

public class ChooseNameState implements GameState {
	
	private Texture textureBackgroud;
	private Stage stage;
	private Table table;
	private TextField textField;
	private TextField textFieldLabel;
	private TextureAtlas atlas;
	private Skin skin;
	private BitmapFont fontBlack;
	private BitmapFont fontRed;
	private OrthographicCamera cam;
	private boolean keyboardOnScreen;
	
	public ChooseNameState(){
		keyboardOnScreen = false;
		stage = new Stage(new FitViewport(1920, 1080));
		atlas = new TextureAtlas(Gdx.files.internal("img/buttons.atlas"));
		skin = new Skin(atlas);
		textureBackgroud = new Texture(Gdx.files.internal("img/strategy.png"));
		fontBlack = new BitmapFont(Gdx.files.internal("fonts/trench.fnt"));
		fontRed = new BitmapFont(Gdx.files.internal("fonts/trench_red.fnt"));
	}
	@Override
	public void create() {
		Gdx.input.setInputProcessor(stage);
		
		table = new Table();
        
		table.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		
		TextureRegion region = new TextureRegion(textureBackgroud, 0, 0, textureBackgroud.getWidth(), textureBackgroud.getHeight());          
        Image background = new Image(region);
        stage.addActor(background);
       
        TextFieldStyle style = new TextFieldStyle();
        style.background = skin.getDrawable("button_up");
        style.font = fontBlack;
        style.fontColor = Color.BLACK;
        textField = new TextField("", style);
        textField.setAlignment(Align.center);
        textField.setOnscreenKeyboard(new OnscreenKeyboard() {
            @Override
            public void show(boolean visible) {
            	if(Gdx.app.getType() == ApplicationType.Android && visible){
            		Gdx.input.setOnscreenKeyboardVisible(true);
            		if(!keyboardOnScreen){
            			cam.translate(0, -Gdx.graphics.getHeight() * 0.4f);
            			cam.update();
            		}
            		keyboardOnScreen = true;
            	}
            	else if(Gdx.app.getType() == ApplicationType.Android && !visible){
            		Gdx.input.setOnscreenKeyboardVisible(false);
            		if(keyboardOnScreen){
            			cam.translate(0, Gdx.graphics.getHeight() * 0.4f);
            			cam.update();
            		}
            		keyboardOnScreen = false;
            	}
            }
        });
        textField.addListener(new InputListener(){

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				super.keyDown(event, keycode);
				if(keycode == Keys.ENTER){
					Gdx.app.setLogLevel(Application.LOG_DEBUG);
					ConnectionStrategy connection;
					if(Poker.isServer()){
						connection = new ServerConnection();
					}
					else{
						connection = new ClientConnection();						
					}
					StateMachine.getStateMachine().switchState(States.LOADING);
					connection.connect(textField.getText());
					return true;
				}
				return false;
			}
        	
        });
        
        
        TextFieldStyle styleLabel = new TextFieldStyle();
        styleLabel.fontColor = Color.RED;
        styleLabel.font = fontRed;
        textFieldLabel = new TextField("Insert your name:", styleLabel);
        textFieldLabel.setDisabled(true);
        textFieldLabel.setAlignment(Align.center);
        
        table.add(textFieldLabel).spaceBottom(75.0f).prefWidth(800).row();
        table.add(textField).prefWidth(800).row();
        
        stage.addActor(table);
		
	}

	@Override
	public void render() {
		stage.act();
		stage.draw();
	}

	@Override
	public void dispose() {

	}
	@Override
	public void resize(int x, int y) {
		stage.getViewport().update(x, y, true);	
	}

}
