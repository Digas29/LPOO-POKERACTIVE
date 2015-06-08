package com.states;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.Poker.logic.Card;
import com.Poker.logic.Player;
import com.Poker.logic.Player.Action;
import com.Poker.logic.PokerGame;
import com.Poker.events.GameEvent;
import com.Poker.events.GameListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
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
	private Texture cardsBackTexture;
	private ArrayList<Label> textFields;
	private Table winTable;
	private LabelStyle style1;
	private LabelStyle style2;
	private LabelStyle style3;
	private LabelStyle style4;
	private LabelStyle style5;
	private LabelStyle style6;
	private Table table;
	private Table table2;
	private Table boardTable;
	private Table playersTable;
	private Stage stage;
	private BitmapFont font;
	private BitmapFont fontRed;
	private Skin skin;
	
	public BoardState(){
		cardsBackTexture = new Texture(Gdx.files.internal("img/Card_back.png"));
		font = new BitmapFont(Gdx.files.internal("fonts/trench.fnt"));
		fontRed = new BitmapFont(Gdx.files.internal("fonts/trench_red.fnt"));
		stage = new Stage(new FitViewport(1920,1080));
		style1 = new LabelStyle();
		circles = new TextureAtlas(Gdx.files.internal("img/circle.atlas"));
		table = new Table();
		table2 = new Table();
		boardTable = new Table();
		playersTable = new Table();
		winTable = new Table();
		table.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		table2.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		boardTable.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		playersTable.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		winTable.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		boardTable.setLayoutEnabled(false);
		playersTable.setLayoutEnabled(false);
		skin = new Skin(circles);
		style1.fontColor = Color.BLACK;
		style1.font = font;
		style1.background = skin.getDrawable("circle_up");
		style2 = new LabelStyle(style1);
		style3 = new LabelStyle(style1);
		style4 = new LabelStyle(style1);
		style5 = new LabelStyle(style1);
		style6 = new LabelStyle(style1);
		textFields = new ArrayList<Label>();
		background = new Texture(Gdx.files.internal("img/board.png"));
		game = new PokerGame();
		game.addGameListener(new GameListener(){

			@Override
			public synchronized void gameEvent(GameEvent e) {
				if(((String)e.getSource()).equals("END")){
					inGame = false;
					ArrayList<Player> winners = game.getWinners();
					int total = game.getPot()/winners.size();
					for(Player p : winners){
						LabelStyle style = new LabelStyle();
						style.font = fontRed;
						style.fontColor = Color.RED;
						Label label = new Label(p.getName() + " won " + total + " of " + game.getPot()  +" pot",style); 
						winTable.add(label);
						p.addMoney(total);
						ServerConnection.getWarpClient().sendPrivateChat(p.getName(), "WIN: " + total);
					}
					ServerConnection.getWarpClient().sendChat("END");
					showFrontCards();
					winners.clear();
					try {
						Thread.sleep(15000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					winTable.clear();
					boardTable.clear();
					playersTable.clear();
					if(waitingList.size() + game.getPlayers().size() >= 2){
						inGame = true;
						game.addRound(waitingList);
						waitingList.clear();
						for(int i = 0; i < textFields.size(); i++){
							textFields.get(i).setText("");
						}
						for(int i = 0; i < game.getPlayers().size(); i++){
							Player p = game.getPlayers().get(i);
							textFields.get(i).setText(p.getName() + "\n" + "$" + p.getMoney());
						}
						sendCards();
						startAction();
					}
				}
				if(((String)e.getSource()).equals("NEXT TURN")){
					ServerConnection.getWarpClient().sendChat("NEXT TURN");
					if(game.nrPlayersForAction() <= 1){
						if(game.nrPlayersInGame() >= 1){
							showBoard();
						}
						game.nextStage();
					}
					else{
						showBoard();
						Player current = game.getNextPlayer();
						textFields.get(game.getPlayers().indexOf(current)).getStyle().background = skin.getDrawable("circle_selected");
						String message = "MAX BET: " + game.getMaxBet();
						ServerConnection.getWarpClient().sendPrivateChat(current.getName(), message);
					}
				}
			}

			
		});
		inGame = false;
		waitingList = new ArrayList<Player>();
	}
	
	private void showBoard() {
		Card[] cards= game.getActualBoard();
		if(cards[3] == null){
			Image img1 = new Image(PlayerState.getAtlasCards().findRegion(PlayerState.cardFileName(cards[0])));
			Image img2 = new Image(PlayerState.getAtlasCards().findRegion(PlayerState.cardFileName(cards[1])));
			Image img3 = new Image(PlayerState.getAtlasCards().findRegion(PlayerState.cardFileName(cards[2])));
			img1.setSize(165.0f, 275.0f);
			img2.setSize(165.0f, 275.0f);
			img3.setSize(165.0f, 275.0f);
			img1.setPosition(0.25f*stage.getWidth(), 0.37f*stage.getHeight());
			img2.setPosition(0.35f*stage.getWidth(), 0.37f*stage.getHeight());
			img3.setPosition(0.45f*stage.getWidth(), 0.37f*stage.getHeight());
			boardTable.add(img1);
			boardTable.add(img2);
			boardTable.add(img3);
		}
		else if(cards[4] == null){
			Image img4 = new Image(PlayerState.getAtlasCards().findRegion(PlayerState.cardFileName(cards[3])));
			img4.setSize(165.0f, 275.0f);
			img4.setPosition(0.55f*stage.getWidth(), 0.37f*stage.getHeight());
			boardTable.add(img4);
		}
		else{
			Image img5 = new Image(PlayerState.getAtlasCards().findRegion(PlayerState.cardFileName(cards[4])));
			img5.setSize(165.0f, 275.0f);
			img5.setPosition(0.65f*stage.getWidth(), 0.37f*stage.getHeight());
			boardTable.add(img5);
		}
		
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
	
	public void showBackCards(){
		playersTable.clear();
		for(int i = 0; i < game.getPlayers().size(); i++){
			Player p= game.getPlayers().get(i);
			if(p.inGame()){
				Image img1 = new Image(new TextureRegion(cardsBackTexture, 0, 0, cardsBackTexture.getWidth(), cardsBackTexture.getHeight()));
				Image img2 = new Image(new TextureRegion(cardsBackTexture, 0, 0, cardsBackTexture.getWidth(), cardsBackTexture.getHeight()));
				img1.setSize(99.0f, 165.0f);
				img2.setSize(99.0f, 165.0f);
				switch(i){
				case 0:
					img1.setPosition(0.365f * stage.getWidth(), 0.675f * stage.getHeight());
					img2.setPosition(0.395f * stage.getWidth(), 0.675f * stage.getHeight());
					break;
				case 1:
					img1.setPosition(0.565f * stage.getWidth(), 0.675f * stage.getHeight());
					img2.setPosition(0.595f * stage.getWidth(), 0.675f * stage.getHeight());
					break;
				case 2:
					img1.setPosition(0.8f * stage.getWidth(), 0.4f * stage.getHeight());
					img2.setPosition(0.83f * stage.getWidth(), 0.4f * stage.getHeight());
					break;
				case 3:
					img1.setPosition(0.565f * stage.getWidth(), 0.2f * stage.getHeight());
					img2.setPosition(0.595f * stage.getWidth(), 0.2f * stage.getHeight());
					break;
				case 4:
					img1.setPosition(0.365f * stage.getWidth(), 0.2f * stage.getHeight());
					img2.setPosition(0.395f * stage.getWidth(), 0.2f * stage.getHeight());
					break;
				case 5:
					img1.setPosition(0.15f * stage.getWidth(), 0.4f * stage.getHeight());
					img2.setPosition(0.18f * stage.getWidth(), 0.4f * stage.getHeight());
					break;
				}
				playersTable.add(img1);
				playersTable.add(img2);
			}
		}
	}
	
	public void showFrontCards(){
		playersTable.clear();
		for(int i = 0; i < game.getPlayers().size(); i++){
			Player p= game.getPlayers().get(i);
			if(p.inGame()){
				Image img1 = new Image(PlayerState.getAtlasCards().findRegion(PlayerState.cardFileName(p.getCards().get(0))));
				Image img2 = new Image(PlayerState.getAtlasCards().findRegion(PlayerState.cardFileName(p.getCards().get(1))));
				img1.setSize(99.0f, 165.0f);
				img2.setSize(99.0f, 165.0f);
				switch(i){
				case 0:
					img1.setPosition(0.365f * stage.getWidth(), 0.675f * stage.getHeight());
					img2.setPosition(0.395f * stage.getWidth(), 0.675f * stage.getHeight());
					break;
				case 1:
					img1.setPosition(0.565f * stage.getWidth(), 0.675f * stage.getHeight());
					img2.setPosition(0.595f * stage.getWidth(), 0.675f * stage.getHeight());
					break;
				case 2:
					img1.setPosition(0.8f * stage.getWidth(), 0.4f * stage.getHeight());
					img2.setPosition(0.83f * stage.getWidth(), 0.4f * stage.getHeight());
					break;
				case 3:
					img1.setPosition(0.565f * stage.getWidth(), 0.2f * stage.getHeight());
					img2.setPosition(0.595f * stage.getWidth(), 0.2f * stage.getHeight());
					break;
				case 4:
					img1.setPosition(0.365f * stage.getWidth(), 0.2f * stage.getHeight());
					img2.setPosition(0.395f * stage.getWidth(), 0.2f * stage.getHeight());
					break;
				case 5:
					img1.setPosition(0.15f * stage.getWidth(), 0.4f * stage.getHeight());
					img2.setPosition(0.18f * stage.getWidth(), 0.4f * stage.getHeight());
					break;
				}
				playersTable.add(img1);
				playersTable.add(img2);
			}
		}
	}
	
	@Override
	public void create() {
		Gdx.input.setInputProcessor(stage);
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
				ArrayList<Player> players = game.getPlayers();
				for(Player y: players){
					if(y.getName().equals(arg0)){
						textFields.get(game.getPlayers().indexOf(y)).getStyle().background = skin.getDrawable("circle_up");
						if(arg1.contains("CALL")){
							game.playerAction(Action.CALL, y, 0);
						}
						else if(arg1.contains("FOLD")){
							game.playerAction(Action.FOLD, y, 0);
						}
						else{
							game.playerAction(Action.RAISE, y, Integer.parseInt(arg1.substring("RAISE ".length(), arg1.length())));
						}
						showBackCards();
						textFields.get(game.getPlayers().indexOf(y)).setText(y.getName() + "\n" + "$" + y.getMoney());
						Player x = game.getNextPlayer();
						if(x == null){
							game.nextStage();
						}
						else{
							textFields.get(game.getPlayers().indexOf(x)).getStyle().background = skin.getDrawable("circle_selected");
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
					for(int i = 0; i < textFields.size(); i++){
						textFields.get(i).setText("");
					}
					for(int i = 0; i < game.getPlayers().size(); i++){
						Player p = game.getPlayers().get(i);
						textFields.get(i).setText(p.getName() + "\n" + "$" + p.getMoney());
					}
					showBackCards();
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
		
		textFields.clear();
		TextureRegion region = new TextureRegion(background, 0, 0, background.getWidth(), background.getHeight());
		Image img = new Image(region);
		img.moveBy((stage.getWidth() - img.getWidth())/2.0f, (stage.getHeight() - img.getHeight())/2.0f);
		table.addActor(img);
		
		Label n1 = new Label("", style1);
		Label n2 = new Label("", style2);
		Label n3 = new Label("", style3);
		Label n4 = new Label("", style4);
		Label n5 = new Label("", style5);
		Label n6 = new Label("", style6);
		textFields.add(n1);
		n1.setPosition(0.35f * stage.getWidth(), 0.8125f * stage.getHeight());
		n1.setFontScale(0.5f);
		n1.setAlignment(Align.center);
		n1.setSize(200.0f, 200.0f);
		textFields.add(n2);
		n2.setPosition(0.55f * stage.getWidth(), 0.8125f * stage.getHeight());
		n2.setFontScale(0.5f);
		n2.setAlignment(Align.center);
		n2.setSize(200.0f, 200.0f);
		textFields.add(n3);
		n3.setPosition(0.8825f * stage.getWidth(), 0.4f * stage.getHeight());
		n3.setFontScale(0.5f);
		n3.setAlignment(Align.center);
		n3.setSize(200.0f, 200.0f);
		textFields.add(n4);
		n4.setPosition(0.55f * stage.getWidth(), 0.0f * stage.getHeight());
		n4.setFontScale(0.5f);
		n4.setAlignment(Align.center);
		n4.setSize(200.0f, 200.0f);
		textFields.add(n5);
		n5.setPosition(0.35f * stage.getWidth(), 0.0f * stage.getHeight());
		n5.setFontScale(0.5f);
		n5.setAlignment(Align.center);
		n5.setSize(200.0f, 200.0f);
		textFields.add(n6);
		n6.setPosition(0.0175f * stage.getWidth(), 0.4f * stage.getHeight());
		n6.setFontScale(0.5f);
		n6.setAlignment(Align.center);
		n6.setSize(200.0f, 200.0f);
		table2.setLayoutEnabled(false);
		table2.add(n1);
		table2.add(n2);
		table2.add(n6);
		table2.add(n3);
		table2.add(n5);
		table2.add(n4);
		table2.center();
		stage.addActor(table);
		stage.addActor(table2);
		stage.addActor(boardTable);
		stage.addActor(playersTable);
		stage.addActor(winTable);
	}
	
	private void startAction() {
		Player current = game.getNextPlayer();
		textFields.get(game.getPlayers().indexOf(current)).getStyle().background = skin.getDrawable("circle_selected");
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
