package com.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.utils.GameState;
import com.utils.StateMachine;

public class InitialState implements GameState, InputProcessor{
	private Sound cards;
	private Texture texture;
	private Stage stage;
	
	public InitialState(){
		stage = new Stage();
		cards = Gdx.audio.newSound(Gdx.files.internal("snd/cardShuffle.wav"));
		texture = new Texture(Gdx.files.internal("img/present.png"));
        TextureRegion region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());          
        Image actor = new Image(region);
        stage.addActor(actor);
        stage.addAction(Actions.alpha(0.0f));
        stage.addAction(Actions.fadeIn(1));
		cards.play(0.5f);
		Thread t = new Thread(){

			@Override
			public void run() {
				super.run();
				try {
					Thread.sleep(4000);
					stage.addAction(Actions.fadeOut(2));
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				StateMachine.getStateMachine().switchState(StateMachine.States.STRATEGY);
			}
		};
		t.start();
	}
	
	@Override
	public boolean keyDown(int keycode) {
		return false;
	}
	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.SPACE)
			cards.play(0.5f);
		return false;
	}
	@Override
	public boolean keyTyped(char character) {
		return false;
	}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		cards.play(0.5f);
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

	@Override
	public void render() {
		stage.act();
		stage.draw();
	}

	@Override
	public void dispose() {
		cards.dispose();
		texture.dispose();
	}

}
