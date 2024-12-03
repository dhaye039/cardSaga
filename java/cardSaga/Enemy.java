package cardSaga;
import java.util.*;

public class Enemy extends Entity {
    List<Card> cards = new ArrayList<>();
    int gold;
    boolean isBoss;
    int bossLife;

    public Enemy(String entityType, int gold, boolean isBoss) {
        super(entityType);
        this.gold = gold;
        this.isBoss = isBoss;
        this.bossLife = 2;
        initCards();
    }

    private void initCards() {
        switch (entityType) {
            case "goblin":
                cards.add(masterList.lookup("Knife"));
                cards.add(masterList.lookup("Rob"));
                cards.add(masterList.lookup("Steal"));
                // cards.add(masterList.lookup("Weakness Potion"));
                break;
            case "slime":
                break;
            case "ogre":
                break;
        }

        // for (var card : cards) {
        //     System.out.println(card.name);
        // }
    }

    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card c) {
        cards.add(c);
    }

    // TODO: this doesnt work: "c is null"
    // public void remove(Card c) {
    //     String cName = c.name;
    //     int i = 0;
    //     for (Card card : cards) {
    //         if (card.name == cName) {
    //             cards.remove(i);
    //             break;
    //         }    
    //         i++;
    //     }
    // }
}
