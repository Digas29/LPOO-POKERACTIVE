package com.Poker.logic;

import java.util.ArrayList;
import java.util.LinkedList;

import com.Poker.events.GameEvent;
import com.Poker.events.GameListener;
import com.Poker.logic.Player.Action;


public class PokerGame {
	private ArrayList<Round> rounds;
	private ArrayList<Player> players;
	private ArrayList<Player> winners;
	private LinkedList<Player> playersQueue;
	private GameListener listener = null;
	private int gameIteration;
	private int maxBet;
	private int pot;
	
	public PokerGame() {
		gameIteration = 0;
		pot  = 0;
		maxBet = 0;
		players = new ArrayList<Player>();
		rounds = new ArrayList<Round>();
		playersQueue = new LinkedList<Player>();	
	}
	public void addGameListener(GameListener listener){
		this.listener = listener;
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	public ArrayList<Round> getRounds(){
		return rounds;
	}
	
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
			for(Integer x : winnersIndex){
				winners.add(players.get(x));
			}
			listener.gameEvent(new GameEvent("END",0));
			return;
		default:
			break;
		}
		System.out.println(rounds.get(rounds.size()-1) + "");
		prepareQueue();
	}

	public int nrPlayersForAction(){
		int total = 0;
		for(Player p: players){
			if(p.inGame() && !p.isAllIn())
				total++;
		}
		return total;
	}
	
	public int nrPlayersInGame(){
		int total = 0;
		for(Player p: players){
			if(p.inGame())
				total++;
		}
		return total;
	}
	
	public void prepareQueue(){
		for(Player p: players){
			if(p.inGame() && !p.isAllIn())
				playersQueue.add(p);
		}
	}
	
	public Player getNextPlayer(){
		return playersQueue.poll();
	}


	public int getMaxBet() {
		return maxBet;
	}


	public ArrayList<Player> getWinners() {
		return winners;
	}


	public int getPot() {
		return pot;
	}
	public int getGameIteration() {
		return gameIteration;
	}
}