package cardSaga;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import cardSaga.traits.*;


public class Inventory {

    MasterList masterList = MasterList.getInstance();

    List<Card> cards;
    HashMap<Integer, Card> returnMap = new HashMap<>();

    String entityType;
    int numUpgdCards;
    int gold;

    public Inventory(String entityType, int i, int gold) {
        this.cards = new ArrayList<>();
        this.entityType = entityType;
        this.numUpgdCards = i;
        this.gold = gold;
        initCards();
    }

    private void initCards() {
        switch (entityType) {
            case "knight":
                cards.add(masterList.lookup("Sword"));
                cards.add(masterList.lookup("Sheild"));
                cards.add(masterList.lookup("Strength Potion"));
                break;
            case "wizard":
                cards.add(masterList.lookup("Fireball"));
                cards.add(masterList.lookup("Magic Mirror"));
                // cards.add(masterList.lookup("Confusion Potion"));
                cards.add(masterList.lookup("Weakness Potion"));
                break;
            case "archer":
                cards.add(masterList.lookup("Bow"));
                cards.add(masterList.lookup("Bomb"));
                cards.add(masterList.lookup("Smoke Arrow"));
                cards.add(masterList.lookup("Luck Potion"));
                break;
            case "rogue":
                cards.add(masterList.lookup("Dagger"));
                cards.add(masterList.lookup("Cloak"));
                cards.add(masterList.lookup("Crit Potion"));
                break;
        }
    }

    public List<Integer> getUpgdable() {
        List<Integer> upgdable = new ArrayList<>();
        int i = 0;

        System.out.println("\n\tUpgrade Cards: " + numUpgdCards);
        System.out.println("\n\tUpgrade Selection:");
        for (var card : cards) {
            System.out.print(String.format("\tCard [%d] %s", ++i, card.name));

            if (card.trait instanceof Upgradable) {
                Upgradable Ucard = (Upgradable) card.trait;
                System.out.println(" has " + Ucard.getUpgdCap() + " upgrades left");
                if (Ucard.getUpgdCap() > 0) {
                    upgdable.add(i);
                } 
            } else {
                System.out.println(" is not upgradable");
            }
        }
        if (upgdable.size() != 0) System.out.println("\n\tEnter 'x' to Exit.\n");

        return upgdable;
    }

    
    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card c) {
        cards.add(c);
    }

    public void remove(Card c) {
        // String cName = c.name;
        int i = 0;
        for (Card card : cards) {
            if (card == c) {
                cards.remove(i);
                break;
            }    
            i++;
        }
    }


    public int getnumUpgdCards() {
        return numUpgdCards;
    }

    public void setnumUpgdCards(int numUpgdCards) {
        this.numUpgdCards = numUpgdCards;
    }

    public void incnumUpgdCards() {
        ++numUpgdCards;
    }

    public void decnumUpgdCards() {
        --numUpgdCards;
    }


    public int getGold() {
        return gold;
    }

    public int setGold(int gold) {
        return this.gold = gold;
    }

    public int addGold(int gold) {
        return this.gold += gold;
    }
}
