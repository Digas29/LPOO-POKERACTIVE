package com.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.utils.GameState;
import com.utils.StateMachine;

public class InitialState implements GameState{
	private Sound cards;
	private Texture texture;
	private Stage stage;
	
	public InitialState(){
		stage = new Stage(new StretchViewport(1920, 1080));
		cards = Gdx.audio.newSound(Gdx.files.internal("snd/cardShuffle.wav"));
		texture = new Texture(Gdx.files.internal("img/present.png"));
	}

	@Override
	public void render() {
		stage.act();
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
		cards.dispose();
		texture.dispose();
	}

	@Override
	public void create() {
        TextureRegion region = new TextureRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());          
        Image actor = new Image(region);
        stage.addActor(actor);
        stage.addAction(Actions.alpha(0.0f));
        stage.addAction(Actions.fadeIn(2));
		cards.play(0.5f);
		Thread t = new Thread(){

			@Override
			public void run() {
				super.run();
				try {
					Thread.sleep(3000);
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
	public void resize(int x, int y) {
		stage.getViewport().update(x, y, true);
	}

}
