package com.Poker.logic;

import java.util.ArrayList;
import java.util.LinkedList;

import com.Poker.events.GameEvent;
import com.Poker.events.GameListener;
import com.Poker.logic.Player.Action;

/**
 * 
 * Class that defines the Round.
 * 
 * @author Diogo Trindade
 * @author Rui Vilares
 * 
 */

public class PokerGame {
	/** ArrayList with every rounds */
	private ArrayList<Round> rounds;
	/** ArrayList with every players */
	private ArrayList<Player> players;
	/** ArrayList with winners */
	private ArrayList<Player> winners;
	/** LinkedList with every players, like a queue */
	private LinkedList<Player> playersQueue;
	/** GameListener */
	private GameListener listener = null;
	/** Number of iteration */
	private int gameIteration;
	/** Actual maxBet */
	private int maxBet;
	/** pot amount */
	private int pot;
	
	/**
	 * Constructs and initializes a Poker game.
	 * 
	 */
	public PokerGame() {
		gameIteration = 0;
		pot  = 0;
		maxBet = 0;
		players = new ArrayList<Player>();
		winners = new ArrayList<Player>();
		rounds = new ArrayList<Round>();
		playersQueue = new LinkedList<Player>();	
	}
	
	/**
	 * Add Game Listener to listener
	 * 
	 * @param listener
	 * 					specific listener
	 * 
	 */
	public void addGameListener(GameListener listener){
		this.listener = listener;
	}
	
	
	/**
	 * Get players 
	 * 
	 * @return ArrayList with players
	 * 
	 */
	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	/**
	 * Get rounds 
	 * 
	 * @return ArrayList with rounds
	 * 
	 */
	public ArrayList<Round> getRounds(){
		return rounds;
	}
	
	/**
	 * Add a new round
	 * 
	 * @param waitingList
	 * 					 players in game
	 * 
	 */
	public void addRound(ArrayList<Player> waitingList){
		for(Player x: waitingList){
			players.add(x);
		}
		gameIteration = 0;
		pot = 0;
		for(Player y : players){
			y.setAllIn(false);
			if(y.getMoney() > 0)
				y.setInGame();
		}
		prepareQueue();
		rounds.add(new Round(players));
	}
	
	/**
	 * Action chosen by the player
	 * 
	 * @param action
	 * 					action chosen
	 * @param player
	 * 					specific player
	 * @param raiseAmount
	 * 					raise amount
	 * 
	 * @return true if was possible, false otherwise
	 * 
	 */
	public boolean playerAction(Action action, Player player, int raiseAmount){

		int amount = player.update(action, maxBet, raiseAmount);
		if(amount < 0){
			return false;
		}
		else{
			pot += amount;
			if(action == Action.RAISE){
				maxBet = raiseAmount;
				refreshQueue(player);
			}
			return true;
		}
	}
	
	/**
	 * Refresh the players queue
	 * 
	 * @param player
	 * 					specific player
	 * 
	 */
	private void refreshQueue(Player player) {
		playersQueue.clear();
		int index = players.indexOf(player);
		int i;
		if(index == players.size() - 1)
			i = 0;
		else
			i = index + 1;
		
		for(; i != index; i++){
			if(players.get(i).inGame() && !players.get(i).isAllIn()){
				playersQueue.add(players.get(i));
			}
			
			if(i == players.size() - 1){
				i = -1;
			}
		}
			
	}
	
	/**
	 * Go to a new iteration
	 * 
	 */
	public void nextStage(){
		gameIteration++;
		for(Player x: players){
			x.resetIterationBet();
		}
		maxBet = 0;
		switch (gameIteration) {
		case 0:
			break;
		case 1:
			rounds.get(rounds.size()-1).flop();
			break;
		case 2:
			rounds.get(rounds.size()-1).turn();
			break;
		case 3:
			rounds.get(rounds.size()-1).river();
			break;
		case 4:		
			ArrayList<Integer> winnersIndex = rounds.get(rounds.size()-1).getWinners();
			winners.clear();
			for(Integer x : winnersIndex){
				winners.add(players.get(x.intValue()));
			}
			listener.gameEvent(new GameEvent("END",0));
			return;
		default:
			break;
		}
		System.out.println(rounds.get(rounds.size()-1) + "");
		prepareQueue();
		listener.gameEvent(new GameEvent("NEXT TURN",0));
	}

	/**
	 * Get number of players for a new action
	 * 
	 *  @return total 
	 * 
	 */
	public int nrPlayersForAction(){
		int total = 0;
		for(Player p: players){
			if(p.inGame() && !p.isAllIn())
				total++;
		}
		return total;
	}
	
	/**
	 * Get number of players in game
	 * 
	 *  @return total 
	 * 
	 */
	public int nrPlayersInGame(){
		int total = 0;
		for(Player p: players){
			if(p.inGame())
				total++;
		}
		return total;
	}
	
	
	/**
	 * Prepare players queue
	 * 
	 */
	public void prepareQueue(){
		for(Player p: players){
			if(p.inGame() && !p.isAllIn())
				playersQueue.add(p);
		}
	}
	
	
	/**
	 * Get next player 
	 * 
	 * @return next player
	 */
	public Player getNextPlayer(){
		return playersQueue.poll();
	}

	/**
	 * Get Max Bet
	 * 
	 * @return max bet
	 */
	public int getMaxBet() {
		return maxBet;
	}

	/**
	 * Get ArrayList with winners
	 * 
	 * @return winners
	 */
	
	public ArrayList<Player> getWinners() {
		return winners;
	}

	/**
	 * Get pot amount
	 * 
	 * @return pot
	 */
	public int getPot() {
		return pot;
	}
	
	/**
	 * Get actual game iteration
	 * 
	 * @return iteration
	 */
	public int getGameIteration() {
		return gameIteration;
	}
}