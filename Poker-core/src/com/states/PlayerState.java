package com.states;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.Poker.logic.Card;
import com.Poker.logic.Player;
import com.Poker.logic.Player.Action;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
	private Texture cardsTexture;
	private Stage stage;
	private Table tableL;
	private Table tableR;
	private Table table;
	private Image cardBackLeft;
	private Image cardBackRight;
	private TextButtonStyle style;
	private Player player;
	private final int waitResponce = 15;
	private String serverName = null;
	private Skin skinButtons;
	private Skin skinCards;
	private BitmapFont font;
	private TextureAtlas atlasButtons;
	private TextureAtlas atlasCards;
	private TextButton foldButton;
	private TextButton callButton;
	private TextButton raiseButton;
	private TextButton showCards;
	private int maxBet;
	private Timer timer;
	private Table table2;
	
	public PlayerState(){
		timer = new Timer();
		maxBet = 0;
		font = new BitmapFont(Gdx.files.internal("fonts/trench.fnt"));
		tableL = new Table();
		tableR = new Table();
		table = new Table();
		table2 = new Table();
		stage = new Stage(new FitViewport(1920,1080));
		cardsBackTexture = new Texture(Gdx.files.internal("img/Card_back.png"));
		atlasButtons = new TextureAtlas(Gdx.files.internal("img/action_buttons.atlas"));
		atlasCards = new TextureAtlas(Gdx.files.internal("img/cards.txt"));
		skinButtons = new Skin(atlasButtons);
		skinCards = new Skin(atlasCards);
		style = new TextButtonStyle();
        style.font = font;
        style.fontColor = Color.BLACK;
        style.up = skinButtons.getDrawable("action_up");
        style.down = skinButtons.getDrawable("action_over");
        style.over = skinButtons.getDrawable("action_over");
        style.disabled = skinButtons.getDrawable("action_disable");
	}
	@Override
	public void create() {
		Gdx.input.setInputProcessor(stage);
		ClientConnection.getWarpClient().addNotificationListener(new NotifyListener(){

			@Override
			public void onChatReceived(ChatEvent arg0) {
				
				if(arg0.getMessage().contains("END")){
					if(player.inGame()){
						moveUpCards();
					}
					player.setAllIn(false);
					if(player.getMoney() > 0){
						player.setInGame();
					}
				}
				else{
					player.resetIterationBet();
				}
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
						String amount = arg1.substring(("MAX BET: ").length(), arg1.length());
						maxBet = Integer.parseInt(amount);
						chooseAction();
					}
					else if(arg1.startsWith("WIN: ")){
						String amount = arg1.substring(("WIN: ").length(), arg1.length());
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
					Card card =(Card) in.readObject();
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
		table.setBounds(0, 0, stage.getWidth(), stage.getHeight());
		TextureRegion region = new TextureRegion(cardsBackTexture, 0, 0, cardsBackTexture.getWidth(), cardsBackTexture.getHeight());          
		cardBackLeft = new Image(region);
		cardBackRight = new Image(region);
		cardBackLeft.moveBy(82.0f, stage.getHeight());
		cardBackRight.moveBy(82.0f, stage.getHeight());
		
		tableL.addActor(cardBackLeft);
		tableR.addActor(cardBackRight);
		
		showCards = new TextButton("SHOW CARDS",style);
		showCards.setDisabled(true);
		showCards.addListener(new ClickListener(){

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				tableL.clear();
				tableR.clear();
				tableL.addActor(cardBackLeft);
				tableR.addActor(cardBackRight);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if(!showCards.isDisabled()){
					tableL.clear();
					tableR.clear();
					Image img1 = new Image(atlasCards.findRegion(cardFileName(player.getCards().get(0))));
					Image img2 = new Image(atlasCards.findRegion(cardFileName(player.getCards().get(1))));
					tableL.addActor(img1);
					tableR.addActor(img2);
				}
				return super.touchDown(event, x, y, pointer, button);
			}
			
			
		});
		foldButton = new TextButton("FOLD",style);
		foldButton.setDisabled(true);
		foldButton.addListener(new ClickListener(){

			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if(!foldButton.isDisabled()){
					if(player.update(Action.FOLD, maxBet, 0) >= 0){
						timer.cancel();
						foldButton.setDisabled(true);
						callButton.setDisabled(true);
						raiseButton.setDisabled(true);
						moveUpCards();
						ClientConnection.getWarpClient().sendPrivateChat(serverName, Action.FOLD.toString());
					}
				}
			}
			
		});
		
		callButton = new TextButton("CHECK/CALL",style);
		callButton.setDisabled(true);
		callButton.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if(!callButton.isDisabled()){
					if(player.update(Action.CALL, maxBet, 0) >= 0){
						timer.cancel();
						foldButton.setDisabled(true);
						callButton.setDisabled(true);
						raiseButton.setDisabled(true);
						ClientConnection.getWarpClient().sendPrivateChat(serverName, Action.CALL.toString());
					}
				}
			}
			
		});
		
		raiseButton = new TextButton("RAISE",style);
		raiseButton.setDisabled(true);
		
		raiseButton.addListener(new ClickListener(){
			
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				if(!raiseButton.isDisabled()){
					if(player.update(Action.RAISE, maxBet, 100) >= 0){
						timer.cancel();
						foldButton.setDisabled(true);
						callButton.setDisabled(true);
						raiseButton.setDisabled(true);
						ClientConnection.getWarpClient().sendPrivateChat(serverName, Action.RAISE.toString() + " " + 100);
					}
				}
			}
			
		});
		table2.setBounds(0,0, stage.getWidth(),stage.getHeight());
		table2.add(showCards).prefWidth(300.0f).padBottom(0.9f*stage.getHeight());
		table.add(foldButton).spaceLeft(25.0f).spaceRight(25.0f).prefWidth(300.0f).padTop(0.9f*stage.getHeight());
		table.add(callButton).spaceLeft(25.0f).spaceRight(25.0f).prefWidth(300.0f).padTop(0.9f*stage.getHeight());
		table.add(raiseButton).spaceLeft(25.0f).spaceRight(25.0f).prefWidth(300.0f).padTop(0.9f*stage.getHeight());
		
		stage.addActor(tableL);
		stage.addActor(tableR);
		stage.addActor(table2);
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

	}
	
	public void cardRecived(){
		if(player.getCards().size() == 1)
			cardBackLeft.addAction(Actions.moveBy(0, -stage.getHeight(), 1.5f));
		else{
			cardBackRight.addAction(Actions.moveBy(0, -stage.getHeight(), 1.5f));
			showCards.setDisabled(false);
		}
	}
	
	public void moveUpCards(){
		cardBackLeft.addAction(Actions.moveBy(0, stage.getHeight(), 1.0f));
		cardBackRight.addAction(Actions.moveBy(0, stage.getHeight(), 1.0f));
		showCards.setDisabled(true);
	}
	private void chooseAction() {
		Gdx.input.vibrate(1000);
		foldButton.setDisabled(false);
		callButton.setDisabled(false);
		raiseButton.setDisabled(false);
		timer.schedule(new TimerTask(){
			
			@Override
			public void run() {
				player.update(Action.FOLD, maxBet, 0);
				ClientConnection.getWarpClient().sendPrivateChat(serverName, Action.FOLD.toString());
			}
			
		}, waitResponce*1000);
	}
	public static String cardFileName(Card card){
		return Card.rankAsString(card.getRank()) + "_of_" + Card.suitAsString(card.getSuit());
	}
}
