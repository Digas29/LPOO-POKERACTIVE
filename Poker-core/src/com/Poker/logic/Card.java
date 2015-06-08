package com.Poker.logic;

import java.io.Serializable;

/**
 * 
 * Class that defines the cards.
 * 
 * @author Diogo Trindade
 * @author Rui Vilares
 * 
 */

public class Card implements Serializable{
	private static final long serialVersionUID = 7705461535044127210L;
	/** Card rank */
	private int rank;
	/** Card suit */
    private int suit;

    /** Array with all suits */
    private static String[] suits = { "hearts", "spades", "diamonds", "clubs"};
    /** Array with all ranks */
    private static String[] ranks  = { "ace", "2", "3", "4", "5", "6", "7", 
                   "8", "9", "10", "jack", "queen", "king" };
    
    
    /**
	 * Draws Card rank to String.
	 * 
	 * @param rank 
	 * 			number of rank position in rank array.
	 * 
	 * @return String with a rank
	 * 
	 */
    public static String rankAsString(int rank)
    {
        return ranks[rank];
    }
    
    
    /**
   	 * Draws Card suit to String.
   	 * 
   	 * @param suit 
   	 * 			number of suit position in suit array.
   	 * 
   	 * @return String with a suit
   	 * 
   	 */
    public static String suitAsString(int suit)
    {
        return suits[suit];
    }

    
    /**
	 * Constructs and initializes a Card.
	 * 
	 * @param suit 
	 * 			number of suit position in suit array.
	 * @param rank 
	 * 			number of rank position in rank array.
	 * 
	 */
    
    public Card(int suit, int rank)
    {
        this.rank=rank;
        this.suit=suit;
    }
    
    /**
	 * Draws card information to String.
	 * 
	 * @return String with card details
	 * 
	 */
    public @Override String toString()
    {
          return ranks[rank] + " of " + suits[suit];
    }

    
    /**
	 * Get card rank. 
	 * 
	 * @return int with the rank position in rank array
	 * 
	 */
    public int getRank()
    {
         return rank;
    }
    
    /**
	 * Get card suit. 
	 * 
	 * @return int with the suit position in suit array
	 * 
	 */
    public int getSuit()
    {
        return suit;
    }
}
