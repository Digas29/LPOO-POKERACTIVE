package poker.cli;

import java.util.Scanner;

import poker.logic.Player;
import poker.logic.Poker;

public class Cli {
	private Scanner reader;

	public Cli() {
		int nrPlayers = nrPlayers();
		int money = moneyToStart();
		Poker Game = new Poker(nrPlayers, money);
		int cmd = 0;
		do{
			clearSrc();
			int iteration = 0;
			Game.playersInGame();
			Game.addRound();
			do {
				switch (iteration) {
				case 0:
					break;
				case 1:
					Game.getRounds().get(Game.getRounds().size()-1).flop();
					break;
				case 2:
					Game.getRounds().get(Game.getRounds().size()-1).turn();
					break;
				case 3:
					Game.getRounds().get(Game.getRounds().size()-1).river();
					break;
				default:
					break;
				}
				System.out.println(Game.getRounds().get(Game.getRounds().size()-1));
				Player p = Game.getNextPlayer();
				while(p != null){
					if (p.inGame() && !p.isAllIn()){
						do{
							cmd = readAction(p.getName());
						}
						while(!Game.playerAction(Player.intToAction(cmd),p, cmd == 3 ? getAmout() : 0));
					}
					p = Game.getNextPlayer();
				}
				iteration++;
				Game.refreshQueue();
			} while (iteration < 4);
			System.out.println("\n\nThe winners are :");
			for(Integer i : Game.getRounds().get(Game.getRounds().size()-1).getWinners()){
				System.out.println(Game.getPlayers().get(i.intValue()).getName() + "!!\n\n\n");
				Game.updateWinnerMoney(i, Game.getRounds().get(Game.getRounds().size()-1).getWinners().size());
			}
			readChar();
			Game.updatePlayers();
		}while(Game.getPlayers().size()>1);		
	}

	private int getAmout() {
		int betAmount;
		do {
			System.out.print("How much money will you bet: ");
			reader = new Scanner(System.in);
			try {
				betAmount = reader.nextInt();
			} catch (Exception e) {
				betAmount = 0;
			}
		} while (betAmount <= 0);
		return betAmount;
	}

	public int moneyToStart() {
		int moneyToStart;
		do {
			System.out.print("How much money will players start: ");
			reader = new Scanner(System.in);
			try {
				moneyToStart = reader.nextInt();
			} catch (Exception e) {
				moneyToStart = 100;
			}
		} while (moneyToStart <= 0);
		return moneyToStart;
	}

	public int nrPlayers() {
		int nrPlayers;
		do {
			System.out.print("How many players: ");
			reader = new Scanner(System.in);
			try {
				nrPlayers = reader.nextInt();
			} catch (Exception e) {
				nrPlayers = 100;
			}
		} while (nrPlayers <= 1 || nrPlayers > 6);
		return nrPlayers;
	}

	public char readChar() {
		char read;
		System.out.print("> ");
		read = reader.next().charAt(0);
		return read;
	}

	public void finalize() {
		reader.close();
	}

	public void printString(String str) {
		System.out.print(str);
	}

	public void clearSrc() {
		for (int i = 0; i < 50; i++) {
			System.out.println();
		}
	}

	public int readAction(String name){
		
		int read;
        do {
        	System.out.println("\n" + name + " action: (1) FOLD (2) CHECK/CALL (3) RAISE");
            System.out.print("> ");
    		read = reader.nextInt();
        } while (read<1||read>3);
        return read;
	}
}