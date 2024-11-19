package cardSaga;

import java.util.List;
import java.util.Scanner;

public class Player extends Entity {
    private Scanner scanner = new Scanner(System.in);

    Inventory inventory;
    int hp;
    int xp;
    int maxHp = 100;

    public Player(String entityType) {
        super(entityType);
        this.inventory = new Inventory(entityType, 1, 10);
        this.hp = 100;
        this.xp = 0;
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
        System.out.println("\t  Health: " + calcHealth());
        System.out.println("\t  Level: " + generateLevelBar() + "\n");
            
        System.out.println("\t  Gold: " + inventory.gold);
        System.out.println("\t  Upgrade Cards: " + inventory.numUpgdCards);
        System.out.println(); 

        String input;
        
        do {
            System.out.print("Press 'x' to return: ");
            input = scanner.nextLine().toLowerCase();
        } while (!input.equals("x"));

        System.out.println();
    }

    private StringBuilder calcHealth() {
        // Calculate the filled and empty portions of the health bar
        double filledLength = (double) (((double)hp / (double)maxHp) * 10);
        double emptyLength = 10 - filledLength;


        // Use StringBuilder for efficient string concatenation
        StringBuilder healthBar = new StringBuilder("[");
        healthBar.append("=".repeat((int)filledLength)); // Filled portion
        healthBar.append("-".repeat((int)emptyLength)); // Empty portion
        healthBar.append("] ");

        // Add the percentage display
        int percentage = (int) (((double)hp / (double)maxHp) * 100);
        healthBar.append(percentage).append("%");

        return healthBar;
    }

    public String generateLevelBar() {
        // Calculate current level
        int level = (int) Math.sqrt(xp);

        // XP thresholds for the current and next levels
        int xpForCurrentLevel = level * level;
        int xpForNextLevel = (level + 1) * (level + 1);

        // XP progress within the current level
        int xpInCurrentLevel = xp - xpForCurrentLevel;
        int xpToNextLevel = xpForNextLevel - xpForCurrentLevel;

        // Percentage of progress
        double progressPercent = (double) xpInCurrentLevel / xpToNextLevel;

        // Filled and empty lengths
        int filledLength = (int) (progressPercent * 10);
        int emptyLength = 10 - filledLength;

        // Build the bar
        StringBuilder bar = new StringBuilder("[");
        bar.append("=".repeat(filledLength));
        bar.append("-".repeat(emptyLength));
        bar.append("]");

        // Insert the level in the middle
        bar.insert(6, String.valueOf(level));

        return bar.toString();
    }

    public void incXP(int xp) {
        this.xp += xp;
    }

    public int getHp() {
        return hp;
    }
}

