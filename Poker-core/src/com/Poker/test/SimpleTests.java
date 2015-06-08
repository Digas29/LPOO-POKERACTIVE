package com.Poker.test;


import java.util.ArrayList;

import com.Poker.logic.*;
import com.Poker.events.*;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleTests {
	
	@Test
	public void ShufflingTest()
	{
		PokerGame game = new PokerGame();
		ArrayList<Player> list = new ArrayList<Player>();
		list.add(new Player(200, "Player 1"));
		list.add(new Player(200, "Player 2"));
		list.add(new Player(200, "Player 3"));
		list.add(new Player(200, "Player 4"));
		game.addRound(list);
		game.addRound(list);
		assertFalse(game.getRounds().get(0).getDeck().equals(game.getRounds().get(1).getDeck()));
	}
	
	@Test
	public void BestHandTest()
	{
		Card[] board = new Card[5];
		board[0] = new Card(1, 1);
		board[1] = new Card(0, 2);
		board[2] = new Card(3, 3);
		board[3] = new Card(3, 9);
		board[4] = new Card(0, 11);
		
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add(new Card(2, 4));
		hand1.add(new Card(2, 5));
		
		ArrayList<Card> hand2 = new ArrayList<Card>();
		hand2.add(new Card(2, 2));
		hand2.add(new Card(2, 11));
		
		
		Evaluator ev1 = new Evaluator(hand1, board);
		Evaluator ev2 = new Evaluator(hand2, board);
		
		assertEquals(1, ev1.compareTo(ev2));
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
		
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add(new Card(2, 12));
		hand1.add(new Card(3, 2));
		
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
		
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add(new Card(2, 5));
		hand1.add(new Card(3, 2));
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(2, ev.getValue());
		assertEquals("                pair of king's",ev.toString());
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
		
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add(new Card(2, 5));
		hand1.add(new Card(3, 8));
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(3, ev.getValue());
		assertEquals(ev.toString(),"                two pair jack 9");
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
		
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add(new Card(2, 5));
		hand1.add(new Card(3, 8));
		
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
		
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add(new Card(2, 4));
		hand1.add(new Card(3, 3));
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(5, ev.getValue());
		assertEquals("                jack high straight", ev.toString());
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
		
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add(new Card(0, 4));
		hand1.add(new Card(0, 1));
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(6, ev.getValue());
		assertEquals("                jack high flush", ev.toString());
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
		
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add(new Card(1, 0));
		hand1.add(new Card(0, 0));
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(7, ev.getValue());
		assertEquals("                full house ace over king", ev.toString());
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
		
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add(new Card(1, 8));
		hand1.add(new Card(3, 8));
		
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
		
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add(new Card(3, 4));
		hand1.add(new Card(3, 3));
		
		Evaluator ev = new Evaluator(hand1, board);
		
		assertEquals(9, ev.getValue());
		assertEquals("                straight flush jack high", ev.toString());
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
		ArrayList<Card> hand1 = new ArrayList<Card>();
		hand1.add(new Card(2, 0));
		hand1.add(new Card(0, 0));
		
		//2 Pares
		ArrayList<Card> hand2 = new ArrayList<Card>();
		hand2.add(new Card(3, 10));
		hand2.add(new Card(1, 1));
		
		//Sequencia
		ArrayList<Card> hand3 = new ArrayList<Card>();
		hand3.add(new Card(2, 2));
		hand3.add(new Card(1, 3));
		
		//Par
		ArrayList<Card> hand4 = new ArrayList<Card>();
		hand4.add(new Card(3, 12));
		hand4.add(new Card(0, 11));
		
		
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
		PokerGame game = new PokerGame();
		ArrayList<Player> list = new ArrayList<Player>();
		list.add(new Player(200, "Player 1"));
		list.add(new Player(200, "Player 2"));
		list.add(new Player(200, "Player 3"));
		list.add(new Player(200, "Player 4"));
		game.prepareQueue();
		game.addRound(list);
		assertEquals(4,game.nrPlayersForAction());
		
		assertTrue(game.getPlayers().get(0).inGame());
		game.playerAction(Player.Action.CALL, game.getNextPlayer(), 0);
		assertTrue(game.getPlayers().get(0).inGame());
		assertEquals(4, game.nrPlayersInGame());
		
		assertEquals(200, game.getPlayers().get(1).getMoney());
		game.playerAction(Player.Action.RAISE, game.getNextPlayer(), 100);
		assertEquals(100, game.getPlayers().get(1).getMoney());
		
		assertEquals(200, game.getPlayers().get(2).getMoney());
		game.playerAction(Player.Action.CALL, game.getNextPlayer(), 0);
		assertEquals(100, game.getPlayers().get(1).getMoney());
		
		assertEquals(200, game.getPlayers().get(3).getMoney());
		game.playerAction(Player.Action.CALL, game.getNextPlayer(), 0);
		assertEquals(100, game.getPlayers().get(1).getMoney());
		
		game.addRound(new ArrayList<Player>());
		assertEquals(4, game.getPlayers().size());
		
		
		
		
		game.addGameListener(new GameListener(){
			@Override
			public void gameEvent(GameEvent e) {
				// TODO Auto-generated method stub
			}});
		
		
		game.nextStage();
		game.nextStage();
		game.nextStage();
		game.nextStage();
		
		ArrayList<Integer> temp = game.getRounds().get(1).getWinners();
		assertTrue(temp.size() <= game.nrPlayersInGame());
	}
}
