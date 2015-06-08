package com.states;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.Poker.logic.Player;
import com.Poker.logic.Player.Action;
import com.Poker.logic.PokerGame;
import com.Poker.events.GameEvent;
import com.Poker.events.GameListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.connections.ServerConnection;
import com.shephertz.app42.gaming.multiplayer.client.events.ChatEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.LobbyData;
import com.shephertz.app42.gaming.multiplayer.client.events.MoveEvent;
import com.shephertz.app42.gaming.multiplayer.client.events.RoomData;
import com.shephertz.app42.gaming.multiplayer.client.events.UpdateEvent;
import com.shephertz.app42.gaming.multiplayer.client.listener.NotifyListener;
import com.utils.GameState;

public class BoardState implements GameState{

	private PokerGame game;
	private final int money = 1000;
	private boolean inGame;
	private ArrayList<Player> waitingList;
	private Texture background;
	private TextureAtlas circles;
	private ArrayList<TextField> textFields;
	private TextFieldStyle style;
	private Table table;
	private Stage stage;
	
	public BoardState(){
		textFields = new ArrayList<TextField>();
		background = new Texture(Gdx.files.internal("img/board.png"));
		circles = new TextureAtlas(Gdx.files.internal("img/cirlces.atlas"));
		stage = new Stage(new FitViewport(1920,1080));
		table = new Table();
		game = new PokerGame();
		game.addGameListener(new GameListener(){

			@Override
			public synchronized void gameEvent(GameEvent e) {
				if(((String)e.getSource()).equals("END")){
					inGame = false;
					ArrayList<Player> winners = game.getWinners();
					int total = game.getPot()/winners.size();
					for(Player p : winners){
						p.addMoney(total);
						ServerConnection.getWarpClient().sendPrivateChat(p.getName(), "WIN: " + total);
					}
					ServerConnection.getWarpClient().sendChat("END");
					winners.clear();
					try {
						Thread.sleep(15000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					if(waitingList.size() + game.getPlayers().size() >= 2){
						inGame = true;
						game.addRound(waitingList);
						waitingList.clear();
						sendCards();
						startAction();
					}
				}
				if(((String)e.getSource()).equals("NEXT TURN")){
					ServerConnection.getWarpClient().sendChat("NEXT TURN");
					Player current = game.getNextPlayer();
					String message = "MAX BET: " + game.getMaxBet();
					ServerConnection.getWarpClient().sendPrivateChat(current.getName(), message);
				}
			}

			
		});
		inGame = false;
		waitingList = new ArrayList<Player>();
	}
	private void sendCards() {

		ArrayList<Player> players = game.getPlayers();
		for(int j = 0; j < 2; j++){
			for(Player x: players){
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ObjectOutput out = null;
				try {
					out = new ObjectOutputStream(bos);
					out.writeObject(x.getCards().get(j));
					byte[] yourBytes = bos.toByteArray();
					ServerConnection.getWarpClient().sendPrivateUpdate(x.getName(), yourBytes);
	
				} catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					try {
						if (out != null) {
							out.close();
						}
					} catch (IOException ex) {
					}
					try {
						bos.close();
					} catch (IOException ex) {
					}
				}
			}	
		}
	}
	
	@Override
	public void create() {
		ServerConnection.getWarpClient().addNotificationListener(new NotifyListener(){

			@Override
			public void onChatReceived(ChatEvent arg0) {

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
				System.out.println(arg0);
				System.out.println(arg1);
				ArrayList<Player> players = game.getPlayers();
				for(Player y: players){
					if(y.getName().equals(arg0)){
						if(arg1.contains("CALL")){
							game.playerAction(Action.CALL, y, 0);
						}
						else if(arg1.contains("FOLD")){
							game.playerAction(Action.FOLD, y, 0);
						}
						else{
							game.playerAction(Action.RAISE, y, Integer.parseInt(arg1.substring("RAISE ".length(), arg1.length())));
						}
						
						Player x = game.getNextPlayer();
						if(game.nrPlayersForAction() <= 1){
							while(game.getGameIteration() < 4){
								game.nextStage();
							}
						}
						if(x == null){
							game.nextStage();
						}
						else{
							String message = "MAX BET: " + game.getMaxBet();
							ServerConnection.getWarpClient().sendPrivateChat(x.getName(), message);
						}
						break;
					}
				}

			}

			@Override
			public void onPrivateUpdateReceived(String arg0, byte[] arg1,
					boolean arg2) {

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
			public synchronized void onUserJoinedRoom(RoomData arg0, String arg1) {
				waitingList.add(new Player(money,arg1));
				if (!inGame && waitingList.size() + game.getPlayers().size() >= 2){
					inGame = true;
					game.addRound(waitingList);
					waitingList.clear();
					sendCards();
					startAction();
				}
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
		table.setBounds(0, 0, stage.getWidth(), stage.getWidth());
		
		TextureRegion region = new TextureRegion(background, 0, 0, background.getWidth(), background.getHeight());
		Image img = new Image(region);
		img.moveBy((stage.getWidth() - img.getWidth())/2.0f, (stage.getHeight() - img.getHeight())/2.0f);
		table.addActor(img);
		
		stage.addActor(table);
	}
	
	private void startAction() {
		Player current = game.getNextPlayer();
		String message = "MAX BET: " + game.getMaxBet();
		ServerConnection.getWarpClient().sendPrivateChat(current.getName(), message);
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

}
