package com.Poker.logic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.connections.ServerConnection;
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;

public class Round {
	private ArrayList<Player> players;
	private Deck deck;
	private Card[] board = new Card[5];
	
	public Round(ArrayList<Player> p){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		this.players = p;
		deck = new Deck();
		for(int j = 0; j < 2; j++){
			for (int i = 0; i < players.size(); i++) {
				if(players.get(i).inGame()){
					players.get(i).addCard(deck);
					try {
						out = new ObjectOutputStream(bos);   
						out.writeObject(players.get(i).getCards().get(j));
						byte[] yourBytes = bos.toByteArray();
						ServerConnection.getWarpClient().sendPrivateUpdate(players.get(i).getName(), yourBytes);

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
	}
	
	public void flop() {
		deck.drawFromDeck(); 
		for (int i = 0; i < 3; i++)
			board[i] = deck.drawFromDeck();
	}
	
	public void turn(){
		deck.drawFromDeck();
        board[3] = deck.drawFromDeck();
    }
	
	public void river(){
		deck.drawFromDeck();
        board[4] = deck.drawFromDeck();
    }
    
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
    
    public Deck getDeck(){
    	return deck;
    }
}
