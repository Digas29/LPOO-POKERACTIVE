package poker.logic;



public class Card {
    private int rank;
    private int suit;

    private static String[] suits = { "Hearts", "Spades", "Diamonds", "Clubs"};
    private static String[] ranks  = { "Ace", "2", "3", "4", "5", "6", "7", 
                   "8", "9", "10", "Jack", "Queen", "King" };

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
    
    
    // Alterar se necessário
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
