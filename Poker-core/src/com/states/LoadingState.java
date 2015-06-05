package com.states;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.utils.GameState;

public class LoadingState implements GameState {
	
	private Texture textureChip;
	private Stage stage;
	private Table table;
	private TextField textField;
	private BitmapFont fontRed;
	
	public LoadingState(){
		stage = new Stage(new StretchViewport(1920, 1080));
		textureChip = new Texture(Gdx.files.internal("img/chipRed.png"));
		fontRed = new BitmapFont(Gdx.files.internal("fonts/trench_red.fnt"));
	}
	@Override
	public void create() {
		Gdx.input.setInputProcessor(stage);
		        
		table = new Table();
		table.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		
		TextureRegion region = new TextureRegion(textureChip, 0, 0, textureChip.getWidth(), textureChip.getHeight());          
		Image chip = new Image(region);
		chip.setOrigin(textureChip.getWidth()/2, textureChip.getHeight()/2);
		chip.setPosition(Gdx.graphics.getWidth()/2 - textureChip.getWidth()/2, Gdx.graphics.getHeight()/2 - textureChip.getHeight()/2);
		chip.addAction(Actions.forever(Actions.rotateBy(2.5f)));
		stage.addActor(chip);
		TextFieldStyle styleLabel = new TextFieldStyle();
        styleLabel.fontColor = Color.RED;
        styleLabel.font = fontRed;
        textField = new TextField("Establishing connection", styleLabel);
        textField.setDisabled(true);
        textField.setAlignment(Align.center);
        
        table.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(1.0f), Actions.fadeIn(1.0f))));
        
        table.add(textField).padTop(Gdx.graphics.getHeight() * 0.9f).prefWidth(800).row();
        
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
