package com.Poker.logic;

import java.io.Serializable;



public class Card implements Serializable{
	private static final long serialVersionUID = 7705461535044127210L;
	private int rank;
    private int suit;

    private static String[] suits = { "hearts", "spades", "diamonds", "clubs"};
    private static String[] ranks  = { "ace", "2", "3", "4", "5", "6", "7", 
                   "8", "9", "10", "jack", "queen", "king" };

    public static String rankAsString(int rank)
    {
        return ranks[rank];
    }
    
    public static String suitAsString(int suit)
    {
        return suits[suit];
    }

    public Card(int suit, int rank)
    {
        this.rank=rank;
        this.suit=suit;
    }
    
    
    public @Override String toString()
    {
          return ranks[rank] + " of " + suits[suit];
    }

    public int getRank()
    {
         return rank;
    }

    public int getSuit()
    {
        return suit;
    }
}
