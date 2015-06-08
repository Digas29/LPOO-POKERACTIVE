package com.states;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.HashMap;

import com.Poker.logic.Card;
import com.Poker.logic.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.connections.ClientConnection;
import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;
import com.utils.GameState;


public class PlayerState implements GameState {

	private Texture cardsBackTexture;
	private Stage stage;
	private Table tableL;
	private Table tableR;
	private Image cardBackLeft;
	private Image cardBackRight;
	private Player player;
	private final int waitResponce = 15;
	private String serverName = null;
	private Thread t = new Thread(){

		@Override
		public void run() {
			super.run();
			try {
				Thread.sleep(waitResponce*1000);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	};
	
	public PlayerState(){
		tableL = new Table();
		tableR = new Table();
		stage = new Stage(new FitViewport(1920,1080));
		cardsBackTexture = new Texture(Gdx.files.internal("img/Card_back.png"));
	}
	@Override
	public void create() {
		ClientConnection.getWarpClient().addNotificationListener(new NotifyListener(){

			@Override
			public void onChatReceived(ChatEvent arg0) {
				player.resetIterationBet();
			}

			@Override
			public void onGameStarted(String arg0, String arg1, String arg2) {

			}

			@Override
			public void onGameStopped(String arg0, String arg1) {

			}

			@Override
			public void onMoveCompleted(MoveEvent arg0) {

			}

			@Override
			public void onPrivateChatReceived(String arg0, String arg1) {
				if(arg0.endsWith("server")){
					if(arg1.startsWith("MAX BET: ")){
						String amount = arg1.substring(("MAX BET: ").length() - 1, arg1.length() - 1);
						int maxBet = Integer.parseInt(amount);
						chooseAction(maxBet);
					}
					else if(arg1.startsWith("WIN: ")){
						String amount = arg1.substring(("WIN: ").length(), arg1.length() - 1);
						int pot = Integer.parseInt(amount);
						player.addMoney(pot);
					}
				}

			}

			@Override
			public void onPrivateUpdateReceived(String arg0, byte[] arg1,
					boolean arg2) {
				if(serverName == null)
					serverName = arg0;
				ByteArrayInputStream bis = new ByteArrayInputStream(arg1);
				ObjectInput in = null;
				try {
					in = new ObjectInputStream(bis);
					Object o = in.readObject();
					Card card = (Card)o;
					player.addCard(card);
					cardRecived();
				} 
				catch (IOException e) {
					e.printStackTrace();
				} 
				catch (ClassNotFoundException e) {
					e.printStackTrace();
				} 
				finally {
					try {
						bis.close();
					} catch (IOException ex) {
					}
					try {
						if (in != null) {
							in.close();
						}
					} catch (IOException ex) {
					}
				}
			}

			@Override
			public void onRoomCreated(RoomData arg0) {

			}

			@Override
			public void onRoomDestroyed(RoomData arg0) {

			}

			@Override
			public void onUpdatePeersReceived(UpdateEvent arg0) {

			}

			@Override
			public void onUserChangeRoomProperty(RoomData arg0, String arg1,
					HashMap<String, Object> arg2, HashMap<String, String> arg3) {

			}

			@Override
			public void onUserJoinedLobby(LobbyData arg0, String arg1) {

			}

			@Override
			public void onUserJoinedRoom(RoomData arg0, String arg1) {

			}

			@Override
			public void onUserLeftLobby(LobbyData arg0, String arg1) {

			}

			@Override
			public void onUserLeftRoom(RoomData arg0, String arg1) {

			}

			@Override
			public void onUserPaused(String arg0, boolean arg1, String arg2) {

			}

			@Override
			public void onUserResumed(String arg0, boolean arg1, String arg2) {

			}

		});
		
		player = new Player(1000, ClientConnection.getName());
		tableL.setBounds(0, 0, stage.getWidth()/2.0f, stage.getHeight());
		tableR.setBounds(stage.getWidth()/2.0f, 0, stage.getWidth()/2.0f, stage.getHeight());
		
		TextureRegion region = new TextureRegion(cardsBackTexture, 0, 0, cardsBackTexture.getWidth(), cardsBackTexture.getHeight());          
		cardBackLeft = new Image(region);
		cardBackRight = new Image(region);
		cardBackLeft.moveBy(82.0f, stage.getHeight());
		cardBackRight.moveBy(82.0f, stage.getHeight());
		
		tableL.addActor(cardBackLeft);
		tableR.addActor(cardBackRight);
		
		
		stage.addActor(tableL);
		stage.addActor(tableR);
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

	}
	
	public void cardRecived(){
		if(player.getCards().size() == 1)
			cardBackLeft.addAction(Actions.moveBy(0, -stage.getHeight(), 1.5f));
		else
			cardBackRight.addAction(Actions.moveBy(0, -stage.getHeight(), 1.5f));
	}
	private void chooseAction(int maxBet) {
		t.start();
		Gdx.input.vibrate(1000);
		ClientConnection.getWarpClient().sendPrivateChat(serverName,Player.Action.CALL.toString());
		t.interrupt();
	}

}
