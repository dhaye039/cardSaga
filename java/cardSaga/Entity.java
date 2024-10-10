package cardSaga;

import java.util.*;

public class Entity {
    String entityType;
    List<Card> cards;
    // int gold;

    MasterList masterList = MasterList.getInstance();

    Random rand = new Random();

    public Entity(String entityType) {
        this.entityType = entityType;
        this.cards = new ArrayList<>();
    }

    // public List<Card> getCards() {
    //     return cards;
    // }

    // public void addCard(Card c) {
    //     cards.add(c);
    // }

    public Effect turn(int totalProb, List<Card> cards) {
        int inc = 0;
        boolean isCardFound = false;

        int cardselect = rand.nextInt(totalProb) + 1;

        for (var card : cards) {
            if ((inc += card.getProb()) > cardselect && !isCardFound) { // card selection
                isCardFound = true;
                return card.getStats();
            }
        }
        return null;
    }
}