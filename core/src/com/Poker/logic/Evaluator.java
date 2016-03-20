package com.Poker.logic;

import java.util.ArrayList;

/**
 * 
 * Class that defines the hand evaluator.
 * 
 * @author Diogo Trindade
 * @author Rui Vilares
 * 
 */

public class Evaluator {
	/** Array with every cards(board+hand) */
	private Card[] cards;
	/** Auxiliary array */
	private int[] value;
	
	/**
	 * Constructs and initializes a hand evaluator.
	 * 
	 * @param arrayList 
	 * 			ArrayList with every player cards.
	 * @param board 
	 * 			Array with every board cards.
	 * 
	 */
	public Evaluator(ArrayList<Card> arrayList, Card[] board)
    {
        value = new int[6];
        cards = new Card[7];
        cards[0] = arrayList.get(0);
        cards[1] = arrayList.get(1);
        for (int i=0; i < 5; i++){
        		cards[i+2] = board[i];
   
        }
        rank();
    }
	
	/**
	 * Auxiliary function that determines the type of hand that player had.
	 */
	private void rank() {
		int[] ranks = new int[13];
		int[] suits = new int[4];
        //miscellaneous cards that are not otherwise significant
        int[] orderedRanks = new int[7];
        boolean flush = false;
        boolean straight=false;
        int sameCards=1,sameCards2=1;
        int largeGroupRank=0,smallGroupRank=0;
        int index=0;

        for (int x=0; x <= 6; x++)
        {
            ranks[cards[x].getRank()]++;
        }
        for (int x=0; x <= 6; x++) {
        	suits[cards[x].getSuit()]++;
        }
        for(int x = 0; x < 4; x++){
        	if(suits[x] >= 5){
        		flush = true;
        		break;
        	}
        }

        for (int x = 12; x >= 0; x--)
        {
                 if (ranks[x] > sameCards)
                 {
                     if (sameCards != 1)//if sameCards was not the default value
                     {
                         sameCards2 = sameCards;
                         smallGroupRank = largeGroupRank;
                     }

                     sameCards = ranks[x];
                     largeGroupRank = x;

                 } 
                 else if (ranks[x] > sameCards2)
                 {
                     sameCards2 = ranks[x];
                     smallGroupRank = x;
                 }
        }

        if (ranks[0] == 1) //if ace, run this before because ace is highest card
        {
            orderedRanks[index] = 0;
            index++;
        }

        for (int x = 12; x >= 1; x--)
        {
            if (ranks[x] == 1 )
            {
                orderedRanks[index] = x; //if ace
                index++;
            }
        }
        
        for (int x = 0; x <=8 ; x++)
        //can't have straight with lowest value of more than 10
        {
            if (ranks[x]==1 && ranks[x+1]==1 && ranks[x+2]==1 && 
                ranks[x+3]==1 && ranks[x+4]==1)
            {
                straight=true;
                break;
            }
        }

        if (ranks[9]==1 && ranks[10]==1 && ranks[11]==1 && 
            ranks[12]==1 && ranks[0]==1) //ace high
        {
            straight=true;
        }
        
        for (int x=0; x<=5; x++)
        {
            value[x]=0;
        }


        //start hand evaluation
        if ( sameCards==1 ) {
            value[0]=1;
            value[1]=orderedRanks[0];
            value[2]=orderedRanks[1];
            value[3]=orderedRanks[2];
            value[4]=orderedRanks[3];
            value[5]=orderedRanks[4];
        }

        if (sameCards==2 && sameCards2==1)
        {
            value[0]=2;
            value[1]=largeGroupRank; //rank of pair
            value[2]=orderedRanks[0];
            value[3]=orderedRanks[1];
            value[4]=orderedRanks[2];
        }

        if (sameCards==2 && sameCards2==2) //two pair
        {
            value[0]=3;
            //rank of greater pair
            value[1]= largeGroupRank>smallGroupRank ? largeGroupRank : smallGroupRank;
            value[2]= largeGroupRank<smallGroupRank ? largeGroupRank : smallGroupRank;
            value[3]=orderedRanks[0];  //extra card
        }

        if (sameCards==3 && sameCards2!=2)
        {
            value[0]=4;
            value[1]= largeGroupRank;
            value[2]=orderedRanks[0];
            value[3]=orderedRanks[1];
        }

        if (straight && !flush)
        {
            value[0]=5;
            value[1]=orderedRanks[0];
        }

        if (flush && !straight)
        {
            value[0]=6;
            value[1]=orderedRanks[0]; //tie determined by ranks of cards
            value[2]=orderedRanks[1];
            value[3]=orderedRanks[2];
            value[4]=orderedRanks[3];
            value[5]=orderedRanks[4];
        }

        if (sameCards==3 && sameCards2==2)
        {
            value[0]=7;
            value[1]=largeGroupRank;
            value[2]=smallGroupRank;
        }

        if (sameCards==4)
        {
            value[0]=8;
            value[1]=largeGroupRank;
            value[2]=orderedRanks[0];
        }

        if (straight && flush)
        {
            value[0]=9;
            value[1]=orderedRanks[0];
        }
	}
	
	
	/**
	 * Draws hand information to String.
	 * 
	 * @return String with hand information
	 * 
	 */
	public String toString() {
		String s;
		switch (value[0]) {

		case 1:
			s = "high card";
			break;
		case 2:
			s = "pair of " + Card.rankAsString(value[1]) + "\'s";
			break;
		case 3:
			s = "two pair " + Card.rankAsString(value[1]) + " "
					+ Card.rankAsString(value[2]);
			break;
		case 4:
			s = "three of a kind " + Card.rankAsString(value[1]) + "\'s";
			break;
		case 5:
			s = Card.rankAsString(value[1]) + " high straight";
			break;
		case 6:
			s = Card.rankAsString(value[1]) + " high flush";
			break;
		case 7:
			s = "full house " + Card.rankAsString(value[1]) + " over "
					+ Card.rankAsString(value[2]);
			break;
		case 8:
			s = "four of a kind " + Card.rankAsString(value[1]);
			break;
		case 9:
			s = "straight flush " + Card.rankAsString(value[1]) + " high";
			break;
		default:
			s = "error in Hand.display: value[0] contains invalid value";
		}
		s = "                " + s;
		return s;
	}
	
	
	/**
	 * Compare hand with another hand.
	 * 
	 * @param ev 
	 * 			Evaluator to compare
	 * 
	 * @return int with the result
	 * 
	 */
	public int compareTo(Evaluator ev) {
		if(ev == null)
			return 1;
		for (int i = 0; i < 6; i++) {
			if (this.value[i] > ev.value[i])
				return 1;
			else if (this.value[i] < ev.value[i])
				return -1;
		}
		return 0; // if hands are equal
	}
	
	
	/**
	 * Get type of hand.
	 * 
	 * @return int with the type of hand
	 * 
	 */
	public int getValue(){
		return value[0];
	}
	
}
