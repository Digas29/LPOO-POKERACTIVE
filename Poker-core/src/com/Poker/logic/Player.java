package com.Poker.logic;

import java.util.ArrayList;

/**
 * 
 * Class that defines the Player.
 * 
 * @author Diogo Trindade
 * @author Rui Vilares
 * 
 */

public class Player {
	/**
	 * Represents a Player action
	 */
	public enum Action{
		/**
         * Increase the bet.
         */
		RAISE,
		/**
         * Match a bet or match a raise.
         */
		CALL,
		/**
         * Out of the round.
         */
		FOLD;
		
		/**
		 * Put an int command to an Action.
		 * 
		 * @param cmd 
		 * 			int value of a action
		 * 
		 * @return Action choose.
		 * 
		 */
		public static Action byteToAction(int cmd){
			return Action.values()[cmd];
		}
	}
	/** Player name */
	private String name;
	/** Player cards */
	private ArrayList<Card> cards;
	/** Player money */
	private int money;
	/** Auxiliary variable to help call action  */
	private int iterationBet;
	/** Flag to verify if player was in game */
	private boolean inGame;
	/** Flag to verify if player was in allIn */
	private boolean allIn;
	
	/**
	 * Constructs and initializes a Player.
	 * 
	 * @param money 
	 * 			player start money.
	 * @param name
	 * 			Player name.
	 * 
	 */
	public Player(int money, String name){
		iterationBet = 0;
		allIn = false;
		this.name=name;
		this.money = money;
		cards = new ArrayList<Card>();
		inGame = true;
    }
	
	/**
	 * Add a card to the player hand.
	 * 
	 * @param d
	 * 			Deck for remove a card from her.
	 * 
	 */
	public void addCard(Deck d)
    {
		if(cards.size() == 2)
			cards.clear();
		cards.add(d.drawFromDeck());
    }
	
	/**
	 * Add a specific Card.
	 * 
	 * @param card
	 * 			specific card.
	 * 
	 */
	public void addCard(Card card)
    {
		if(cards.size() == 2)
			cards.clear();
		cards.add(card);
    }
	
	/**
	 * Get player cards.
	 * 
	 * @return player hand cards.
	 * 
	 */
	public ArrayList<Card> getCards(){
		return cards;
	}
	
	/**
	 * Get player name.
	 * 
	 * @return String with player name.
	 * 
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Check of the player was in game.
	 * 
	 * @return inGame flag.
	 * 
	 */
	public boolean inGame(){
		return inGame;
	}
	
	/**
	 * Put player in game.
	 * 
	 */
	public void setInGame(){
		inGame=true;
	}
	
	/**
	 * Put player out of game.
	 * 
	 */
	public void setOutOfGame(){
		inGame=false;
	}
	
	/**
	 * Draws Player information to String.
	 * 
	 * @return String with player details
	 * 
	 */
	public String toString(){
		String res = "\n";
		for (int i = 0; i < cards.size(); i++){
			res += "\t" + cards.get(i) + "\n";
		}
		res += "Money: " + money + "$\n";
		return res;
	}
	
	
	/**
	 * Update player action.
	 * 
	 * @param action
	 * 			action chosen by player.
	 * @param maxBet
	 * 			actual max bet.
	 * @param raisAmount
	 * 			raise amount chosen from player.
	 * 
	 */
	public int update(Action action, int maxBet, int raiseAmount) {
		if (action == Action.FOLD){
			if(maxBet == 0){
				return -1;
			}
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

	/**
	 * Raise action.
	 * 
	 * @param raisAmount
	 * 			raise amount chosen from player.
	 * @param maxBet
	 * 			actual max bet.
	 * 
	 */
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

	/**
	 * Call action.
	 * 
	 * @param moneyToCall
	 * 			money needed to call.
	 * 
	 */
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
	
	/**
	 * Get money.
	 * 
	 * @return actual money
	 * 
	 */
	public int getMoney(){
		return money;
	}
	
	/**
	 * Get all in flag.
	 * 
	 * @return true if player was in all in.
	 * 
	 */
	public boolean isAllIn() {
		return allIn;
	}

	/**
	 * Set player in all.
	 * 
	 * @param allIn
	 * 				boolean status to allIn option
	 * 
	 */
	public void setAllIn(boolean allIn) {
		this.allIn = allIn;
	}

	/**
	 * Add money to a pot.
	 * 
	 * @param pot
	 * 				specific pot.
	 * 
	 */
	public void addMoney(int pot) {
		money += pot;
	}
	
	/**
	 * Put IterationBet with 0.
	 * 
	 */
	public void resetIterationBet(){
		iterationBet = 0;
	}
}
