package com.Poker.logic;

import java.util.ArrayList;



public class Player {
	public enum Action{
		RAISE,
		CALL,
		FOLD;
		
		public static Action byteToAction(int cmd){
			return Action.values()[cmd];
		}
	}
	private String name;
	private ArrayList<Card> cards;
	private int money;
	private int iterationBet;
	private boolean inGame;
	private boolean allIn;
	
	
	public Player(int money, String name){
		iterationBet = 0;
		allIn = false;
		this.name=name;
		this.money = money;
		cards = new ArrayList<Card>();
		inGame = true;
    }
	
	public void addCard(Deck d)
    {
		if(cards.size() == 2)
			cards.clear();
		cards.add(d.drawFromDeck());
    }
	public void addCard(Card card)
    {
		if(cards.size() == 2)
			cards.clear();
		cards.add(card);
    }
	
	public ArrayList<Card> getCards(){
		return cards;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean inGame(){
		return inGame;
	}
	
	public void setInGame(){
		inGame=true;
	}
	
	public void setOutOfGame(){
		inGame=false;
	}
	
	public String toString(){
		String res = "\n";
		for (int i = 0; i < cards.size(); i++){
			res += "\t" + cards.get(i) + "\n";
		}
		res += "Money: " + money + "$\n";
		return res;
	}
	
	public int update(Action action, int maxBet, int raiseAmount) {
		if (action == Action.FOLD){
			setOutOfGame();
			return 0;
		}
		else if (action == Action.CALL){
			return call(maxBet-iterationBet);
		}
		else if (action == Action.RAISE){
			return raise(raiseAmount, maxBet);
		}
		else{
			return 0;
		}
	}


	private int raise(int raiseAmount, int maxBet) {
		if(raiseAmount + maxBet - iterationBet > money){
			return -1;
		}
		else{
			money -= (raiseAmount - iterationBet);
			if(money == 0)
				allIn = true;
			return raiseAmount;
		}
	}


	private int call(int moneyToCall) {
		if(money >= moneyToCall){
			money -= moneyToCall;
			if(money == 0)
				allIn = true;
			return moneyToCall;
		}
		else{
			int temp = money;
			allIn = true;
			money = 0;
			return temp;
		}
	}
	
	public int getMoney(){
		return money;
	}
	
	public boolean isAllIn() {
		return allIn;
	}


	public void setAllIn(boolean allIn) {
		this.allIn = allIn;
	}


	public void addMoney(int pot) {
		money += pot;
	}
	public void resetIterationBet(){
		iterationBet = 0;
	}
}
