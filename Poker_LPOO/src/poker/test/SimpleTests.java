package poker.test;


import poker.logic.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTests {
	
	@Test
	public void ShufflingTest()
	{
		Poker game = new Poker(4, 200);
		game.addRound();
		game.addRound();
		assertFalse(game.getRounds().get(0).getDeck().equals(game.getRounds().get(1).getDeck()));
	}
	
	public void BestHandTest()
	{
		Card[] board = new Card[5];
		board[0] = new Card(1, 1);
		board[1] = new Card(0, 2);
		board[2] = new Card(3, 3);
		board[3] = new Card(3, 9);
		board[4] = new Card(0, 11);
		
		Card[] hand1 = new Card[2];
		hand1[0] = new Card(2, 4);
		hand1[1] = new Card(2, 5);
		
		Card[] hand2 = new Card[2];
		hand2[0] = new Card(2, 2);
		hand2[1] = new Card(2, 11);
		
		
		Evaluator ev1 = new Evaluator(board, hand1);
		Evaluator ev2 = new Evaluator(board, hand2);
		
		assertFalse(ev1.compareTo(ev2)==1);
	}
	
	@Test
	public void highCardTest()
	{
		Card[] board = new Card[5];
		board[0] = new Card(2, 1);
		board[1] = new Card(1, 3);
		board[2] = new Card(0, 5);
		board[3] = new Card(3, 11);
		board[4] = new Card(0, 0);
		
		Card[] hand1 = new Card[2];
		hand1[0] = new Card(2, 12);
		hand1[1] = new Card(3, 2);
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(ev.getValue(), 1);
		assertEquals("                high card",ev.toString());
	}
	
	@Test
	public void onePairTest()
	{
		Card[] board = new Card[5];
		board[0] = new Card(0, 12);
		board[1] = new Card(1, 12);
		board[2] = new Card(2, 8);
		board[3] = new Card(1, 7);
		board[4] = new Card(0, 3);
		
		Card[] hand1 = new Card[2];
		hand1[0] = new Card(2, 5);
		hand1[1] = new Card(3, 2);
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(2, ev.getValue());
		assertEquals("                pair of King's",ev.toString());
	}
	
	@Test
	public void twoPairTest()
	{
		Card[] board = new Card[5];
		board[0] = new Card(0, 10);
		board[1] = new Card(1, 10);
		board[2] = new Card(2, 8);
		board[3] = new Card(1, 7);
		board[4] = new Card(0, 3);
		
		Card[] hand1 = new Card[2];
		hand1[0] = new Card(2, 5);
		hand1[1] = new Card(3, 8);
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(3, ev.getValue());
		assertEquals(ev.toString(),"                two pair Jack 9");
	}
	
	@Test
	public void threeOfAKindTest()
	{
		Card[] board = new Card[5];
		board[0] = new Card(0, 1);
		board[1] = new Card(1, 10);
		board[2] = new Card(2, 8);
		board[3] = new Card(1, 7);
		board[4] = new Card(0, 8);
		
		Card[] hand1 = new Card[2];
		hand1[0] = new Card(2, 5);
		hand1[1] = new Card(3, 8);
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(4, ev.getValue());
		assertEquals("                three of a kind 9's",ev.toString());
	}
	
	@Test
	public void strightTest()
	{
		Card[] board = new Card[5];
		board[0] = new Card(0, 1);
		board[1] = new Card(1, 10);
		board[2] = new Card(2, 7);
		board[3] = new Card(1, 6);
		board[4] = new Card(0, 5);
		
		Card[] hand1 = new Card[2];
		hand1[0] = new Card(2, 4);
		hand1[1] = new Card(3, 3);
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(5, ev.getValue());
		assertEquals("                Jack high straight", ev.toString());
	}
	
	@Test
	public void flushTest()
	{
		Card[] board = new Card[5];
		board[0] = new Card(0, 9);
		board[1] = new Card(1, 10);
		board[2] = new Card(0, 7);
		board[3] = new Card(1, 6);
		board[4] = new Card(0, 5);
		
		Card[] hand1 = new Card[2];
		hand1[0] = new Card(0, 4);
		hand1[1] = new Card(0, 1);
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(6, ev.getValue());
		assertEquals("                Jack high flush", ev.toString());
	}
	
	@Test
	public void fullHouseTest()
	{
		Card[] board = new Card[5];
		board[0] = new Card(0, 1);
		board[1] = new Card(2, 12);
		board[2] = new Card(0, 7);
		board[3] = new Card(3, 12);
		board[4] = new Card(2, 0);
		
		Card[] hand1 = new Card[2];
		hand1[0] = new Card(1, 0);
		hand1[1] = new Card(0, 0);
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(7, ev.getValue());
		assertEquals("                full house Ace over King", ev.toString());
	}
	
	@Test
	public void fourOfAKindTest()
	{
		Card[] board = new Card[5];
		board[0] = new Card(0, 1);
		board[1] = new Card(1, 10);
		board[2] = new Card(2, 8);
		board[3] = new Card(1, 7);
		board[4] = new Card(0, 8);
		
		Card[] hand1 = new Card[2];
		hand1[0] = new Card(1, 8);
		hand1[1] = new Card(3, 8);
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(8, ev.getValue());
		assertEquals("                four of a kind 9", ev.toString());
	}
	
	@Test
	public void strightFlushTest()
	{
		Card[] board = new Card[5];
		board[0] = new Card(0, 1);
		board[1] = new Card(1, 10);
		board[2] = new Card(3, 7);
		board[3] = new Card(3, 6);
		board[4] = new Card(3, 5);
		
		Card[] hand1 = new Card[2];
		hand1[0] = new Card(3, 4);
		hand1[1] = new Card(3, 3);
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(9, ev.getValue());
		assertEquals("                straight flush Jack high", ev.toString());
	}
	
	@Test
	public void compareHandsTest()
	{
		Card[] board = new Card[5];
		board[0] = new Card(0, 1);
		board[1] = new Card(1, 0);
		board[2] = new Card(3, 4);
		board[3] = new Card(3, 6);
		board[4] = new Card(3, 0);
		
		//Poker
		Card[] hand1 = new Card[2];
		hand1[0] = new Card(2, 0);
		hand1[1] = new Card(0, 0);
		
		//2 Pares
		Card[] hand2 = new Card[2];
		hand2[0] = new Card(3, 10);
		hand2[1] = new Card(1, 1);
		
		//Sequencia
		Card[] hand3 = new Card[2];
		hand3[0] = new Card(2, 2);
		hand3[1] = new Card(1, 3);
		
		//Par
		Card[] hand4 = new Card[2];
		hand4[0] = new Card(3, 12);
		hand4[1] = new Card(0, 11);
		
		
		Evaluator ev1 = new Evaluator(hand1, board);
		Evaluator ev2 = new Evaluator(hand2, board);
		Evaluator ev3 = new Evaluator(hand3, board);
		Evaluator ev4 = new Evaluator(hand4, board);
		
		assertEquals(1, ev1.compareTo(ev2));
		assertEquals(-1, ev2.compareTo(ev1));
		
		assertEquals(1, ev2.compareTo(ev3));
		assertEquals(-1, ev3.compareTo(ev2));
		
		assertEquals(-1, ev3.compareTo(ev1));
		assertEquals(1, ev1.compareTo(ev3));
		
		assertEquals(1, ev1.compareTo(ev4));
		assertEquals(-1, ev4.compareTo(ev1));
	}
	
	@Test
	public void GameTest()
	{
		Poker Game = new Poker(5, 200);
		Game.playersInGame();
		Game.addRound();
		Game.getRounds().get(Game.getRounds().size()-1).flop();
		assertTrue(Game.getPlayers().get(0).inGame());
		Game.playerAction(Player.intToAction(1), Game.getPlayers().get(1), 0);
		assertFalse(Game.getPlayers().get(1).inGame());
		Game.getRounds().get(Game.getRounds().size()-1).turn();
		Game.getRounds().get(Game.getRounds().size()-1).river();
		Game.updatePlayers();
		assertEquals(Game.getPlayers().size(),5);
	}
	
	

}
