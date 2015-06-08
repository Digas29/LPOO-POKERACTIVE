package poker.logic;

import java.util.Collections;
import java.util.ArrayList;

public class Deck {
	private ArrayList<Card> cards;

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

	public Card drawFromDeck() {
		return cards.remove(cards.size() - 1);
	}

	public int getTotalCards() {
		return cards.size();
	}

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