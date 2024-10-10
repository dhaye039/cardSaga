package cardSaga;

import java.util.*;

public class Player extends Entity {
    Inventory inventory;

    public Player(String entityType) {
        super(entityType);
        this.inventory = new Inventory(1, 10);
        initCards();
    }

    public List<Integer> getUpgdable() {
        List<Integer> upgdable = new ArrayList<>();
        int i = 0;

        System.out.println("\n\tUpgrade Cards: " + inventory.numUpgdCards);
        System.out.println("\n\tUpgrade Selection:");
        for (var card : cards) {
            System.out.print(String.format("\tCard [%d] %s", ++i, card.name));

            if (card.trait instanceof Upgdable) {
                Upgdable Ucard = (Upgdable) card.trait;
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

    public void viewInventory() {
        int i = 0;

        System.out.println("\n\tCurrent Card List:");
        for (var card : cards) {
            System.out.println(String.format("\t[Card %d] %s (dmg: %d) -- %s", ++i, card.name, card.dmg, card.trait.getDesc()));
        }

        System.out.println("\n\tOther Items:");
        System.out.println("\tGold: " + inventory.gold);
        System.out.println("\tUpgrade Cards: " + inventory.numUpgdCards + "\n");
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
                cards.add(masterList.lookup("Confusion Potion"));
                cards.add(masterList.lookup("Weakness Potion"));
                break;
            case "ranger":
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

    public void remove(Card c) {
        String cName = c.name;
        int i = 0;
        for (Card card : cards) {
            if (card.name == cName) {
                cards.remove(i);
                break;
            }    
            i++;
        }
    }
    
    public List<Card> getCards() {
        return cards;
    }

    public void addCard(Card c) {
        cards.add(c);
    }

    public Inventory getInventory() {
        return inventory;
    }

}

