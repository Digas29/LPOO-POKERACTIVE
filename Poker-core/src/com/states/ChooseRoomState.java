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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.connections.ClientConnection;
import com.utils.GameState;

public class ChooseRoomState implements GameState {
	private Texture textureBackgroud;
	private Stage stage;
	private Table table;
	private Table rows;
	private Table rowsHeaders;
	private ScrollPane pane;
	private TextureAtlas atlas;
	private Skin skin;
	private BitmapFont fontBlack;
	private BitmapFont fontRed;
	private TextField headerField;
	private TextField nameColumn;
	private TextField ocupationColumn;
	
	public ChooseRoomState(){
		atlas = new TextureAtlas(Gdx.files.internal("img/table.atlas"));
		skin = new Skin(atlas);
		textureBackgroud = new Texture(Gdx.files.internal("img/strategy.png"));
		stage = new Stage(new StretchViewport(1920, 1080));
		fontBlack = new BitmapFont(Gdx.files.internal("fonts/trench.fnt"));
		fontRed = new BitmapFont(Gdx.files.internal("fonts/trench_red.fnt"));
	}
	@Override
	public void create() {
		Gdx.input.setInputProcessor(stage);
		
		table = new Table();
		rows = new Table();
		rowsHeaders = new Table();
		//ScrollPaneStyle scrollStyle = new ScrollPaneStyle();
		
		pane = new ScrollPane(rows);
		
		table.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		rows.setBounds(0, 0, Gdx.graphics.getWidth()*20, Gdx.graphics.getHeight()*20);
		TextureRegion region = new TextureRegion(textureBackgroud, 0, 0, textureBackgroud.getWidth(), textureBackgroud.getHeight());          
        Image background = new Image(region);
        stage.addActor(background);
       
        TextFieldStyle styleLabel = new TextFieldStyle();
        styleLabel.fontColor = Color.RED;
        styleLabel.font = fontRed;
        headerField = new TextField("Choose a room:", styleLabel);
        headerField.setAlignment(Align.center);
        headerField.setDisabled(true);
        
        nameColumn = new TextField("Name", styleLabel);
        nameColumn.setAlignment(Align.center);
        nameColumn.setDisabled(true);
        
        ocupationColumn = new TextField("Ocupation", styleLabel);
        ocupationColumn.setAlignment(Align.center);
        ocupationColumn.setDisabled(true);
        
        rowsHeaders.add(nameColumn).prefWidth(400);
        rowsHeaders.add(ocupationColumn).prefWidth(400);
        TextButtonStyle styleLeft = new TextButtonStyle();
        styleLeft.font =  fontBlack;
        styleLeft.over = skin.getDrawable("table_down_left");
        styleLeft.down = skin.getDrawable("table_down_left");
        styleLeft.up = skin.getDrawable("table_down_left");
        
        TextButtonStyle styleRight = new TextButtonStyle();
        styleRight.font =  fontBlack;
        styleRight.over = skin.getDrawable("table_down_right");
        styleRight.down = skin.getDrawable("table_down_right");
        styleRight.up = skin.getDrawable("table_down_right");
        for(int i = 0; i < ClientConnection.getRooms().size(); i++){
        	TextButton a = new TextButton(ClientConnection.getRooms().get(i).getName(), styleLeft);
        	TextButton b = new TextButton(ClientConnection.getNrPlayers().get(i) + "/" + ClientConnection.getRooms().get(i).getMaxUsers(), styleRight);
        	a.addCaptureListener(new ClickListener(){

				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
				}
        		
        	});
        	rows.add(a).prefWidth(400).spaceBottom(5.0f);
        	rows.add(b).prefWidth(400).spaceBottom(5.0f).row();
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
	@Override
	public void resize(int x, int y) {
		stage.getViewport().update(x, y, true);
	}

}
