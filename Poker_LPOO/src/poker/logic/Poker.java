package poker.logic;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;


public class Poker {
	private ArrayList<Round> rounds;
	private ArrayList<Player> players;
	private ArrayList<Integer> bets;
	private LinkedList<Player> playersQueue;
	private int firstPlayer;
	private int moneyToStart;
	private int maxBet;
	private int lastBet;
	private int pot;
	
	public Poker(int nrPlayers, int money) {
		pot  = 0;
		maxBet = 0;
		lastBet = 0;
		Random gerador = new Random();
		firstPlayer = gerador.nextInt(nrPlayers);
		moneyToStart = money;
		players = new ArrayList<Player>();
		bets = new ArrayList<Integer>();
		rounds = new ArrayList<Round>();
		playersQueue = new LinkedList<Player>();
		for (int i = 0; i < nrPlayers; i++) {
			Player p = new Player(moneyToStart, ("Player " + (i+1)));
			players.add(p);
			bets.add(0);
		}
		refreshQueue();
		
	}
	
	public ArrayList<Player> getPlayers(){
		return players;
	}
	
	public ArrayList<Round> getRounds(){
		return rounds;
	}
	
	public void playersInGame(){
		for (int i = 0; i < players.size(); i++) {
			players.get(i).setInGame();
			players.get(i).setAllIn(false);
			playersQueue.add(players.get(i));
		}
	}
	
	public void addRound(){
		rounds.add(new Round(players));
	}
	
	public boolean playerAction(Action action, Player player, int raiseAmount){
		if (action == Action.RAISE && raiseAmount < maxBet){
			return false;
		}
		int amount = player.update(action, maxBet, raiseAmount, lastBet);
		if(amount < 0){
			return false;
		}
		else{
			pot += amount;
			if(action == Action.RAISE && raiseAmount > maxBet){
				lastBet = maxBet;
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
	
	public void refreshQueue() {
		lastBet = 0;
		for(int i = 0; i < players.size(); i++){
			if(players.get(i).inGame() && !players.get(i).isAllIn()){
				playersQueue.add(players.get(i));
			}
		}
		maxBet = 0;	
	}
	
	public void nextStage(){
		
	}

	public int nrPlayersForAction(){
		int total = 0;
		for(Player p: players){
			if(p.inGame() && !p.isAllIn())
				total++;
		}
		return total;
	}
	public Player getNextPlayer(){
		return playersQueue.poll();
	}
	
	
	public void updatePlayers(){
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getMoney() <= 0)
				players.remove(i);
		}
	}
	
	public void updateWinnerMoney(int player, int size){
		players.get(player).addMoney((int)(pot/size));
	}
	
	public int getLastBet(){
		return lastBet;
	}
}