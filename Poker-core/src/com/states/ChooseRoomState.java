package com.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Align;
import com.utils.GameState;

public class ChooseRoomState implements GameState {
	private Texture textureBackgroud;
	private Stage stage;
	private Table table;
	private Table rows;
	private Table rowsHeaders;
	private ScrollPane pane;
//	private TextureAtlas atlas;
//	private Skin skin;
//	private BitmapFont fontBlack;
	private BitmapFont fontRed;
	private TextField headerField;
	private TextField nameColumn;
	private TextField ocupationColumn;
	
	public ChooseRoomState(){
		//atlas = new TextureAtlas(Gdx.files.internal("img/buttons.atlas"));
		//skin = new Skin(atlas);
		textureBackgroud = new Texture(Gdx.files.internal("img/strategy.png"));
		stage = new Stage();
		//fontBlack = new BitmapFont(Gdx.files.internal("fonts/trench.fnt"));
		fontRed = new BitmapFont(Gdx.files.internal("fonts/trench_red.fnt"));
		create();
	}
	@Override
	public void create() {
Gdx.input.setInputProcessor(stage);
		
		table = new Table();
		rows = new Table();
		rowsHeaders = new Table();
		//ScrollPaneStyle scrollStyle = new ScrollPaneStyle();
		
		pane = new ScrollPane(rows);
		
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		rows.setBounds(0, 0, Gdx.graphics.getWidth()*20, Gdx.graphics.getHeight()*20);
		TextureRegion region = new TextureRegion(textureBackgroud, 0, 0, textureBackgroud.getWidth(), textureBackgroud.getHeight());          
        Image background = new Image(region);
        stage.addActor(background);
       
        TextFieldStyle styleLabel = new TextFieldStyle();
        styleLabel.fontColor = Color.RED;
        styleLabel.font = fontRed;
        headerField = new TextField("Choose a room:", styleLabel);
        headerField.setAlignment(Align.center);
        
        nameColumn = new TextField("Name", styleLabel);
        nameColumn.setAlignment(Align.center);
        
        ocupationColumn = new TextField("Ocupation", styleLabel);
        ocupationColumn.setAlignment(Align.center);
        
        
        rowsHeaders.add(nameColumn).prefWidth(400);
        rowsHeaders.add(ocupationColumn).prefWidth(400);
        
        for(int i = 0; i < 50; i++){
        	TextField a = new TextField("FEUP" + ((Integer)i).toString(), styleLabel);
        	a.setDisabled(true);
        	a.setAlignment(Align.center);
        	TextField b = new TextField(((Integer)i).toString() + "/300", styleLabel);
        	b.setDisabled(true);
        	b.setAlignment(Align.center);
        	rows.add(a).prefWidth(400);
        	rows.add(b).prefWidth(400).row();
        }
        
        
        table.add(headerField).prefWidth(800).padTop(Gdx.graphics.getHeight()*0.25f).row();
        table.add(rowsHeaders).prefWidth(800).spaceBottom(20.0f).row();
        table.add(pane).prefWidth(800).row();
        
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
