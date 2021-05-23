
// import java.util.Arrays;
import java.util.ArrayList;

class Card {
  public String suit;
  public String value;
  public int intValue;

  public Card(String suit, String value, int intValue) {
    this.suit = suit;
    this.value = value;
    this.intValue = intValue;
  }

  public String getCardString() {
    return this.suit + this.value + "(" + this.intValue + ")";
  }
}

class Deck {
  public ArrayList<Card> deck;

  public Deck() {
    this.deck = Deck.generateDeck();
  }

  public static ArrayList<Card> generateDeck() {
    ArrayList<Card> newDeck = new ArrayList<>();
    String[] suits = new String[] { "♣", "♦", "♥", "♠" };
    String[] values = new String[] { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

    for (int i = 0; i < suits.length; i++) {
      for (int j = 0; j < values.length; j++) {
        newDeck.add(new Card(suits[i], values[j], j + 1));
      }
    }
    return newDeck;
  }

  public void printDeck() {
    for (int i = 0; i < this.deck.size(); i++) {
      System.out.println(this.deck.get(i).getCardString());
    }
  }

  public void shuffleDeck(){
    for (int i = this.deck.size() - 1; i >= 0; i--) {
      int j = (int) Math.floor(Math.random() * (i + 1));
      Card temp = this.deck.get(i);
      this.deck.set(i, this.deck.get(j));
      this.deck.set(j, temp);
    }
  }
}

class Main {
  public static void main(String[] args) {
    Deck deck1 = new Deck();
    deck1.printDeck();

    deck1.shuffleDeck();
    deck1.printDeck();
  }
}
