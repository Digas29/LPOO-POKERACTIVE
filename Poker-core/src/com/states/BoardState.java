package com.states;

import java.util.ArrayList;
import java.util.HashMap;

import com.Poker.logic.Player;
import com.Poker.logic.Player.Action;
import com.Poker.logic.PokerGame;
import com.Poker.events.GameEvent;
import com.Poker.events.GameListener;
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

	
	public BoardState(){
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
						Thread.sleep(5000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					if(waitingList.size() + game.getPlayers().size() >= 2){
						inGame = true;
						game.addRound(waitingList);
						waitingList.clear();
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
						if(x == null){
							game.nextStage();
							if(game.nrPlayersForAction() <= 1){
								while(game.getGameIteration() < 4){
									game.nextStage();
								}
							}
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
	}
	
	private void startAction() {
		Player current = game.getNextPlayer();
		String message = "MAX BET: " + game.getMaxBet();
		ServerConnection.getWarpClient().sendPrivateChat(current.getName(), message);
	}
	
	@Override
	public void render() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void resize(int x, int y) {

	}

}
