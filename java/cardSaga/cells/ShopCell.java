package cardSaga.cells;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import cardSaga.Card;
import cardSaga.Inventory;
import cardSaga.MasterList;
import cardSaga.Player;

public class ShopCell extends Cell {
    private List<Card> shop; // A unique shop for this ShopCell
    private boolean cardsInShop;
    private Scanner scanner = new Scanner(System.in);


    public ShopCell() {
        super();
        this.val = 's';
        this.shop = new ArrayList<>(MasterList.getInstance().getShop()); // Copy the shop for uniqueness
        this.cardsInShop = true;
    }

    public List<Card> getShop() {
        return new ArrayList<>(this.shop); // Return a copy to ensure immutability
    }

    public void setShop(List<Card> shop) {
        this.shop = new ArrayList<>(shop); // Allow setting a unique shop for this cell
    }

    public void visitShop(Player player) {
        Inventory pInventory = player.getInventory();
        String input = "";
    
        System.out.println("\n\tWelcome to the Shop!");
    
        do {
            // Shop setup
            List<Integer> buyable = new ArrayList<>();
            int i = 0;
            Inventory inven = player.getInventory();
            int playerGold = inven.getGold();
            cardsInShop = !shop.isEmpty(); // Ensure cardsInShop is correctly set
    
            for (var card : shop) {
                System.out.println(String.format("\n\tCard [%d]: %s (%d dmg) -- %s\n\tCost: %d gold",
                        ++i, card.getName(), card.getDmg(), card.getTrait().getDesc(), card.getCost()));
                buyable.add(i);
            }
            System.out.println("\n\tYou have " + playerGold + " gold. Enter 'x' to Exit.\n");
    
            // Prompt user for input
            System.out.print("What would you like to buy? [number]: ");
            input = scanner.nextLine().toLowerCase();
    
            if (input.equals("x")) {
                break; // Exit shop if 'x' is entered
            }
    
            // Process input
            int cardNum;
            try {
                cardNum = Integer.parseInt(input);
                if (!buyable.contains(cardNum)) {
                    System.out.println("Invalid card number. Please try again.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a card number or 'x' to exit.");
                continue;
            }
    
            // Attempt to buy the card
            Card reqCard = shop.get(cardNum - 1);
            if (playerGold >= reqCard.getCost()) {
                pInventory.addCard(shop.remove(cardNum - 1)); // Add card to inventory, remove from shop
                inven.addGold(-reqCard.getCost()); // Deduct gold
                System.out.println("\n\tYou bought " + reqCard.getName() + "!");
                cardsInShop = !shop.isEmpty();
            } else {
                System.out.println("\n\tYou do not have enough gold to buy this card.");
            }
    
        } while (!input.equals("x") && cardsInShop);
    
        if (!cardsInShop) {
            System.out.println("\n\tThere are no more cards in the shop. Come back later!");
        }
        System.out.println();
    }

    public boolean isCardsInShop() {
        return cardsInShop;
    }
}
