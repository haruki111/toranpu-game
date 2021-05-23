
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

  public Card draw() {
    return this.deck.remove(this.deck.size() - 1);
  }

  public void printDeck() {
    for (int i = 0; i < this.deck.size(); i++) {
      System.out.println(this.deck.get(i).getCardString());
    }
  }

  public void shuffleDeck() {
    for (int i = this.deck.size() - 1; i >= 0; i--) {
      int j = (int) Math.floor(Math.random() * (i + 1));
      Card temp = this.deck.get(i);
      this.deck.set(i, this.deck.get(j));
      this.deck.set(j, temp);
    }
  }
}

class Dealer {
  public static ArrayList<ArrayList<Card>> startGame(int amountOfPlayer) {
    Deck deck = new Deck();
    deck.shuffleDeck();
    ArrayList<ArrayList<Card>> table = new ArrayList<>(2);
    for (int i = 0; i < amountOfPlayer; i++) {
      ArrayList<Card> playerHand = new ArrayList<Card>();
      for (int j = 0; j < 2; j++) {
        playerHand.add(deck.draw());
      }
      table.add(playerHand);
    }
    return table;
  }
}

class Main {
  public static void main(String[] args) {
    ArrayList<ArrayList<Card>> table1 = Dealer.startGame(6);

    for (int i = 0; i < table1.size(); i++) {
      System.out.println(i + 1 + "人目");
      for (Card card : table1.get(i)) {
        System.out.println(card.getCardString());
      }
    }
  }
}
