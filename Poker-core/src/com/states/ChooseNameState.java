package com.states;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Align;
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
	
	public ChooseNameState(){
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
		Gdx.input.setInputProcessor(stage);
		
		table = new Table();
        
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		TextureRegion region = new TextureRegion(textureBackgroud, 0, 0, textureBackgroud.getWidth(), textureBackgroud.getHeight());          
        Image background = new Image(region);
        stage.addActor(background);
       
        TextFieldStyle style = new TextFieldStyle();
        style.background = skin.getDrawable("button_up");
        style.font = fontBlack;
        style.fontColor = Color.BLACK;
        textField = new TextField("", style);
        textField.setAlignment(Align.center);
        textField.addListener(new InputListener(){

			@Override
			public boolean keyDown(InputEvent event, int keycode) {
				super.keyDown(event, keycode);
				if(keycode == Keys.ENTER){
					StateMachine.getStateMachine().switchState(States.INITIAL);
					//TODO Guardar nome System.out.print(textField.getText());
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

}
