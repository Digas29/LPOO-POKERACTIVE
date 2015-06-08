package poker.logic;

enum Action{
	RAISE,
	CALL,
	FOLD
}

public class Player {
	private String name;
	private Card[] cards;
	private int money;
	private boolean inGame;
	private boolean allIn;


	public static Action intToAction(int cmd){
		switch(cmd){
		case 1:
			return Action.FOLD;
		case 2:
			return Action.CALL;
		case 3:
			return Action.RAISE;
		default:
			return null;
		}
	}
	
	
	Player(int money, String name)
    {
		allIn = false;
		this.name=name;
		this.money = money;
		cards = new Card[2];
		inGame = true;
    }
	
	public void addCard(Deck d)
    {
		cards[0] = d.drawFromDeck();
		cards[1] = d.drawFromDeck();
    }
	
	public Card[] getCards(){
		return cards;
	}
	
	public String getName(){
		return name;
	}
	
	public boolean inGame(){
		return inGame;
	}
	
	public void setInGame(){
		inGame=true;
	}
	
	public void setOutOfGame(){
		inGame=false;
	}
	
	public String toString(){
		String res = "\n";
		for (int i = 0; i < cards.length; i++){
			res += "\t" + cards[i] + "\n";
		}
		res += "Money: " + money + "$\n";
		return res;
	}
	
	public int update(Action action, int maxBet, int raiseAmount, int lastBet) {
		if (action == Action.FOLD){
			setOutOfGame();
			return 0;
		}
		else if (action == Action.CALL){
			return call(maxBet-lastBet);
		}
		else if (action == Action.RAISE)
			return raise(raiseAmount);
		else{
			return 0;
		}
	}


	private int raise(int raiseAmount) {
		if(raiseAmount > money){
			return -1;
		}
		else{
			money -= raiseAmount;
			if(money == 0){
				allIn = true;
			}
			return raiseAmount;
		}
	}


	private int call(int maxBet) {
		if(money >= maxBet){
			money -= maxBet;
			if(money == 0)
				allIn = true;
			return maxBet;
		}
		else{
			int temp = money;
			allIn = true;
			money = 0;
			return temp;
		}
	}
	
	public int getMoney(){
		return money;
	}
	
	public boolean isAllIn() {
		return allIn;
	}


	public void setAllIn(boolean allIn) {
		this.allIn = allIn;
	}
	
	public void addMoney(int money) {
		this.money += money;
	}
}
