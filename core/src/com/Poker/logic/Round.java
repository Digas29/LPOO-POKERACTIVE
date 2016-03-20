package com.Poker.logic;

import java.util.ArrayList;

/**
 * 
 * Class that defines the Round.
 * 
 * @author Diogo Trindade
 * @author Rui Vilares
 * 
 */

public class Round {
	/** ArrayList with every players */
	private ArrayList<Player> players;
	/** Deck used in the round */
	private Deck deck;
	/** Board cards */
	private Card[] board = new Card[5];
	
	
	/**
	 * Constructs and initializes a Round.
	 * 
	 * @param p 
	 * 			ArrayList with every players.
	 * 
	 */
	public Round(ArrayList<Player> p){
		this.players = p;
		deck = new Deck();
		for(int j = 0; j < 2; j++){
			for (int i = 0; i < players.size(); i++) {
				if(players.get(i).inGame()){
					players.get(i).addCard(deck);
				}
			}
		}
	}
	
	/**
	 * Flop action.
	 */
	public void flop() {
		deck.drawFromDeck(); 
		for (int i = 0; i < 3; i++)
			board[i] = deck.drawFromDeck();
	}
	
	/**
	 * Turn action.
	 */
	public void turn(){
		deck.drawFromDeck();
        board[3] = deck.drawFromDeck();
    }
	
	/**
	 * River action.
	 */
	public void river(){
		deck.drawFromDeck();
        board[4] = deck.drawFromDeck();
    }
    
	/**
	 * Draws Round information to String.
	 * 
	 * @return String with round details
	 * 
	 */
    public String toString(){
		String res = "";
		res += "Board:";
		for (int i = 0; i < 5; i++){
			if(board[i] != null)
				res += "\n" + board[i];
		}
		
		res += "\n\nPlayers:\n";
		for (int i = 0; i < players.size(); i++){
			if (players.get(i).inGame())
				res += "-->" + players.get(i).getName() + ": " + players.get(i) + "\n";
		}
		return res;
	}
    
    
    /**
	 * Compare all hands in game and choosse the winners
	 * 
	 * @return ArrayList with the winners
	 * 
	 */
    public ArrayList<Integer> getWinners(){
    	ArrayList<Integer> winners = new ArrayList<Integer>();
    	ArrayList<Evaluator> temp = new ArrayList<Evaluator>();
    	for (int i = 0; i < players.size(); i++){
    		if(players.get(i).inGame()){
    			temp.add(new Evaluator(players.get(i).getCards(), board));
    			if (!winners.isEmpty() && temp.get(i).compareTo(temp.get(winners.get(0).intValue())) == 1){
    				winners.clear();
    				winners.add(i);
    			}
    			else if ((!winners.isEmpty() && temp.get(i).compareTo(temp.get(winners.get(0).intValue())) == 0) 
    					|| winners.isEmpty()){
    				winners.add(i);
    			}
    		}
    		else{
    			temp.add(null);
    		}
		}
    	return winners;
    }
    
    /**
	 * Get deck in this round. 
	 * 
	 * @return Deck in the round
	 * 
	 */
    public Deck getDeck(){
    	return deck;
    }
    
    /**
	 * Get table cards for this round
	 * 
	 * @return table cards
	 * 
	 */
	public Card[] getBoard() {
		return board;
	}
}
