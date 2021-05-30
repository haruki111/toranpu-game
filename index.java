
// import java.util.Arrays;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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

  public Deck(Table table) {
    this.deck = Deck.generateDeck(table);
  }

  public static ArrayList<Card> generateDeck(Table table) {
    ArrayList<Card> newDeck = new ArrayList<>();
    String[] suits = new String[] { "♣", "♦", "♥", "♠" };
    String[] values = new String[] { "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

    HashMap<String, Integer> blackJack = new HashMap<>() {
      {
        put("A", 1);
        put("J", 10);
        put("Q", 10);
        put("K", 10);
      }
    };

    for (int i = 0; i < suits.length; i++) {
      for (int j = 0; j < values.length; j++) {
        if (table.gameMode == "21") {
          newDeck
              .add(new Card(suits[i], values[j], blackJack.get(values[j]) == null ? j + 1 : blackJack.get(values[j])));
        } else {
          newDeck.add(new Card(suits[i], values[j], j + 1));
        }

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

class Table {
  public int amountOfPlayers;
  public String gameMode;

  public Table(int amountOfPlayers, String gameMode) {
    this.amountOfPlayers = amountOfPlayers;
    this.gameMode = gameMode;
  }
}

class Dealer {
  public static ArrayList<ArrayList<Card>> startGame(Table table) {
    Deck deck = new Deck(table);
    deck.shuffleDeck();
    ArrayList<ArrayList<Card>> playerCards = new ArrayList<>();
    for (int i = 0; i < table.amountOfPlayers; i++) {
      ArrayList<Card> playerHand = new ArrayList<Card>(Dealer.initialCards(table.gameMode));
      for (int j = 0; j < Dealer.initialCards(table.gameMode); j++) {
        playerHand.add(deck.draw());
      }
      playerCards.add(playerHand);
    }
    return playerCards;
  }

  public static int initialCards(String gameMode) {
    if (gameMode == "21")
      return 2;
    if (gameMode == "poker")
      return 5;
    if (gameMode == "Pair of cards")
      return 5;
    else
      return 0;
  }

  public static void printTableInformation(ArrayList<ArrayList<Card>> playerCards, Table table) {
    System.out.println(
        "Amount of players: " + table.amountOfPlayers + "... Game mode: " + table.gameMode + ". At this table:");
    for (int i = 0; i < playerCards.size(); i++) {
      System.out.println("Player " + (i + 1) + " hand is:");
      for (int j = 0; j < playerCards.get(i).size(); j++) {
        System.out.println(playerCards.get(i).get(j).getCardString());
      }
    }
  }

  public static int score21Individual(ArrayList<Card> cards) {
    int value = 0;
    for (Card card : cards) {
      value += card.intValue;
    }
    if (value > 21)
      return 0;
    return value;
  }

  public static String winnerOf21(ArrayList<ArrayList<Card>> playerCards) {
    int[] points = new int[playerCards.size()]; // プレーヤーの点数を入れる
    int[] cache = new int[22]; // スコアにプレイヤーが何人いるか

    for (int i = 0; i < playerCards.size(); i++) {
      int point = Dealer.score21Individual(playerCards.get(i));// 各プレイヤーの合計スコアを代入
      points[i] = point;

      if (cache[point] >= 1) // そのスコアに何人プレーヤーがいるかif文で処理
        cache[point] += 1;
      else
        cache[point] = 1;
    }

    System.out.println(Arrays.toString(points));// 各プレイヤーの合計値の一覧

    int winnerIndex = HelperFunctions.maxInArrayIndex(points); // 最大値のプレイヤーの配列の位置
    if (cache[points[winnerIndex]] > 1)// 最大値のキャッシュを判定
      return "It is a draw ";
    else if (cache[points[winnerIndex]] >= 0)
      return "player " + (winnerIndex + 1) + " is the winner";
    else
      return "No winners..";
  }

  private static String winnerPairOfCards(ArrayList<ArrayList<Card>> playerCards) {
    String[] generateNumberArr1 = HelperFunctions.generateNumberArr(playerCards.get(0));
    String[] generateNumberArr2 = HelperFunctions.generateNumberArr(playerCards.get(1));

    final String[] cardPower = new String[] { "A", "K", "Q", "J", "10", "9", "8", "7", "6", "5", "4", "3", "2" };

    HashMap<String, Integer> player1Deck = HelperFunctions.createHashMap(cardPower, generateNumberArr1);
    HashMap<String, Integer> player2Deck = HelperFunctions.createHashMap(cardPower, generateNumberArr2);

    // デフォルトで引き分けを返します。
    String winner = "draw";
    // 同じランクのカードの枚数を記録します。
    int pairOfCards = 0;

    for (int i = 0; i < cardPower.length; i++) {
      // それぞれのhashmapをcardPowerの強い順に比較していきます。0または同じ枚数の時は次のランクへ。
      // プレイヤー1が持つ同じランクのカードが多いとき
      if (player1Deck.get(cardPower[i]) > player2Deck.get(cardPower[i])) {
        // 記録しているカードの枚数よりプレイヤー1が持つカードの枚数が多かったら、
        if (pairOfCards < player1Deck.get(cardPower[i])) {
          // pairOfCardsとwinnerをプレイヤー1に書き換えます。
          pairOfCards = player1Deck.get(cardPower[i]);
          winner = "player1";
        }
      } else if (player1Deck.get(cardPower[i]) < player2Deck.get(cardPower[i])) {
        if (pairOfCards < player2Deck.get(cardPower[i])) {
          pairOfCards = player2Deck.get(cardPower[i]);
          winner = "player2";
        }
      }
    }

    return winner;
  }

  // 卓のゲームの種類によって勝利条件を変更するcheckWinnerというメソッドを作成します。
  public static String checkWinner(ArrayList<ArrayList<Card>> playerCards, Table table) {
    if (table.gameMode == "21")
      return Dealer.winnerOf21(playerCards);
    if (table.gameMode == "Pair of cards")
      return Dealer.winnerPairOfCards(playerCards);
    else
      return "no game";
  }

}

class HelperFunctions {

  public static int maxInArrayIndex(int[] intArr) {// 最大値の配列の位置を返す
    int maxIndex = 0;
    int maxValue = intArr[0];

    for (int i = 0; i < intArr.length; i++) {
      if (intArr[i] > maxValue) {
        maxValue = intArr[i];
        maxIndex = i;
      }
    }
    return maxIndex;
  }

  public static String[] generateNumberArr(ArrayList<Card> cards) {
    String[] generateNumberArr = new String[cards.size()];
    for (int i = 0; i < cards.size(); i++) {
      generateNumberArr[i] = cards.get(i).value;
    }
    return generateNumberArr;
  }

  public static HashMap<String, Integer> createHashMap(String[] cardPower, String[] player) {
    HashMap<String, Integer> playerDeck = new HashMap<>();
    for (int i = 0; i < cardPower.length; i++) {
      playerDeck.put(cardPower[i], 0);
    }
    for (int i = 0; i < player.length; i++) {
      playerDeck.replace(player[i], playerDeck.get(player[i]) + 1);
    }
    return playerDeck;
  }
}

class Main {
  public static void main(String[] args) {
    Table table3 = new Table(2, "Pair of cards");
    ArrayList<ArrayList<Card>> game3 = Dealer.startGame(table3);
    Dealer.printTableInformation(game3, table3);
    System.out.println(Dealer.checkWinner(game3, table3));

    Table table1 = new Table(1, "poker");
    ArrayList<ArrayList<Card>> game1 = Dealer.startGame(table1);

    Table table2 = new Table(3, "21");
    ArrayList<ArrayList<Card>> game2 = Dealer.startGame(table2);

    Dealer.printTableInformation(game1, table1);
    System.out.println(Dealer.checkWinner(game1, table1));

    Dealer.printTableInformation(game2, table2);
    System.out.println(Dealer.checkWinner(game2, table2));
  }
}
