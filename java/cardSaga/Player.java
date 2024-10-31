package cardSaga;

import java.util.List;

public class Player extends Entity {

    Inventory inventory;
    int hp;
    int level;

    public Player(String entityType) {
        super(entityType);
        this.inventory = new Inventory(entityType, 1, 10);
        this.hp = 1;
        this.level = 0;
    }
    
    public Inventory getInventory() {
        return inventory;
    }

    public List<Card> getCards() {
        return inventory.cards;
    }

    public void viewInventory() {
        int i = 0;
        String borrowedUses = "";

        System.out.println("\n\tCurrent Card List:");
        for (var card : inventory.cards) {
            if (card.isBorrowed) {
                borrowedUses = " [" + card.numUses + " uses left]";
            }
            System.out.println(String.format("\t  [Card %d] %s (dmg: %d) -- %s%s", ++i, card.name, card.dmg, card.trait.getDesc(), borrowedUses));
        }

        System.out.println("\n\tOther Items:");
        System.out.println("\t  Gold: " + inventory.gold);
        System.out.println("\t  Upgrade Cards: " + inventory.numUpgdCards);
        System.out.println("\t  Health: " + hp);
        System.out.println("\t  Level -" + level + "-\n");

    }

}

