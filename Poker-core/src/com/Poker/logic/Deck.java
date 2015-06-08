package com.Poker.logic;

import java.util.Collections;
import java.util.ArrayList;

/**
 * 
 * Class that defines the deck.
 * 
 * @author Diogo Trindade
 * @author Rui Vilares
 * 
 */

public class Deck {
	/** ArrayList with the cards in the deck */
	private ArrayList<Card> cards;

	/**
	 * Constructs, initializes and shuffle a Deck.
	 * 
	 */
	Deck() 
	{
		cards = new ArrayList<Card>();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				cards.add(new Card(i, j));
			}
		}
		Collections.shuffle(cards);
	}
	
	
	/**
   	 * Draw a Card from the Deck.
   	 * 
   	 * @return Card last card in the deck
   	 * 
   	 */
	public Card drawFromDeck() {
		return cards.remove(cards.size() - 1);
	}

	
	/**
   	 * Get number of cards in the Deck.
   	 * 
   	 * @return number of cards in the Deck
   	 * 
   	 */
	public int getTotalCards() {
		return cards.size();
	}
	
	
	/**
   	 * Override equals function
   	 * 
   	 * @return true if the deck is the same that obj, false otherwise
   	 * 
   	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Deck other = (Deck) obj;
		if (cards == null) {
			if (other.cards != null)
				return false;
		} else if (!cards.equals(other.cards))
			return false;
		return true;
	}
	
	
}