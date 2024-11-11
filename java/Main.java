import java.util.*;

import cardSaga.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Random rand = new Random();
    static MasterList MasterList = cardSaga.MasterList.getInstance(); 
    static Board board = cardSaga.Board.getInstance();
    static boolean cardsInShop = true;


    public static void main(String[] args) {
        boolean gameover = false;
        int turn = 1;

        // Start game
        Player p = startGame();
        
        int numTurn = 0;

        // Game loop
        while (!gameover && numTurn != 100) {
            System.out.println("\t\t\t\t\t    Turn " + turn + "\n");
            System.out.println("Current Options: ");
            System.out.println("[D]ie | [F]ight | Check [I]nventory | [R]oll | Visit the [S]hop | [U]pgrade Card | Spin [W]heel\n");
            switch (procTurn()) {
                case "f":
                    List<Card> pCards = p.getCards();
                    boolean hasCards = !pCards.isEmpty();
                    boolean canPlay = pCards.stream().anyMatch(c -> !c.isReroll());
                    System.out.println("\n");
                    
                    if (hasCards && canPlay) {  
                        Enemy e = new Enemy("goblin", 5);
                        new Fight(p, e, turn).startFight();
                    } else {
                        System.out.println("\n\tYou do not have sufficient cards to fight.\n");
                    }
                    ++turn;
                    break;
                case "i":
                    p.viewInventory();
                    break;
                case "r":                    
                    System.out.println("\n\tYou rolled a " + (rand.nextInt(6) + 1) + ".\n");
                    MasterList.populateShop();
                    cardsInShop = true;
                    break;
                case "s":
                    if (cardsInShop)
                        visitShop(p);
                    else 
                        System.out.println("\n\tThere are currently no cards in the shop.\n");
                    break;
                case "u":
                    if (p.getInventory().getnumUpgdCards() == 0)
                        System.out.println("\n\tYou have no Upgrade Cards.\n");
                    else 
                        upgdCard(p);
                    break;
                case "w":
                    spin(p);
                    break;
                case "d":
                    System.out.println();
                    gameover = true;
                    break;
            }

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");


            numTurn++;
            // gameover = true;
        }

        scanner.close();
    }

    private static Player startGame() {
        String playerType;
        List<String> playerTypes = new ArrayList<>(Arrays.asList("knight", "wizard", "archer", "rogue"));

        String classAvailibility = """

        Available Classes: 
        Knight | Wizard | Archer | Rogue 

        """;
        System.out.print(classAvailibility + "Enter a Class: ");
        playerType = scanner.nextLine().toLowerCase();
        while (!playerTypes.contains(playerType)) {
            System.out.print(classAvailibility + "Please Enter a Valid Class: ");
            playerType = scanner.nextLine();
        }
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Welcome to Card Saga~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        Player p = new Player(playerType);
        return p;
    }

    private static String procTurn() {

        List<String> validOptions = new ArrayList<>(Arrays.asList(
            "d"     // die
            , "f"   // fight
            , "i"   // inventory
            , "r"   // roll
            , "s"   // shop
            , "u"   // upgrade
            , "w"   // wheel
        ));

        // board.displayBoard();
        System.out.print("What would you like to do: ");
        String turn = scanner.nextLine().toLowerCase();

        while (!validOptions.contains(turn)) {
            System.out.print("Please enter 'd', 'i', 'r', 's', 'u', or 'w': ");
            turn = scanner.nextLine();
        }
        return turn;
    }

    private static void visitShop(Player player) {
        List<Card> shop = MasterList.getShop();
        Inventory pInventory = player.getInventory();
        String input = "";

        System.out.println("\n\tWelcome to the Shop!");

        do {
            List<Integer> buyable = new ArrayList<>();
            int i = 0;
            Inventory inven = player.getInventory();
            int playerGold = inven.getGold();
            boolean isValidInput = false;
            boolean afrdable = false;
            int cardNum = -1;

            for (var card : shop) {
                System.out.println(String.format("\n\tCard [%d]: %s (%d dmg) -- %s\n\tCost: %d gold", ++i, card.getName(), card.getDmg(), card.getTrait().getDesc(), card.getCost()));
                buyable.add(i);
            }
            System.out.println("\n\tYou have " + playerGold + " gold. Enter 'x' to Exit.\n");
            
            System.out.print("What would you like to buy? [number]: ");
            input = scanner.nextLine().toLowerCase();

            while (!isValidInput && !afrdable && !input.equals("x")) { // while input isn't valid and card isn't affordable
                try {
                    cardNum = Integer.parseInt(input);
                    isValidInput = buyable.contains(cardNum);
                } catch (NumberFormatException e) {
                    System.out.print("Please enter a valid card [number], or enter 'x' to exit: ");
                    input = scanner.nextLine().toLowerCase();
                    continue;
                }

                if (!isValidInput) {
                    System.out.print("Please enter a valid card [number], or enter 'x' to exit: ");
                    input = scanner.nextLine().toLowerCase();
                    continue;
                }

                Card reqCard = shop.get(cardNum-1);

                if (playerGold >= reqCard.getCost()) { // buy card
                    pInventory.addCard(shop.remove(cardNum-1));
                    inven.addGold(reqCard.getCost() * (-1));
                    if (shop.size() == 0) cardsInShop = false;
                } else {
                    System.out.print("\n\tYou do not have enough gold to buy this card.\n\nPlease enter a valid card [number], or enter 'x' to exit: ");
                    input = scanner.nextLine().toLowerCase();
                    afrdable = false;
                }
            }
        } while (!input.equals("x") && cardsInShop);
        if (!cardsInShop) System.out.println("\n\tThere are no more cards in the shop. Come back later!");
        System.out.println();

    }

    private static void upgdCard(Player player) {

        Inventory inventory = player.getInventory();
        String input = "";

        do {
            List<Integer> upgdable = inventory.getUpgdable();
            int cardNum = -1;
            boolean isValidInput = false;

            if (upgdable.isEmpty()) {
                System.out.println("\n\tYou have no upgradable cards!");
                break;
            } else {
                System.out.print("What card would you like to upgrade? [number]: ");
                input = scanner.nextLine().toLowerCase();

                while (!isValidInput && !input.equals("x")) {
                    try {
                        cardNum = Integer.parseInt(input);
                        isValidInput = upgdable.contains(cardNum);
                    } catch (NumberFormatException e) {
                        System.out.print("Please enter a valid card [number], or enter 'x' to exit: ");
                        input = scanner.nextLine().toLowerCase();
                        continue;
                    }
    
                    if (!isValidInput) {
                        System.out.print("Please enter a valid card [number], or enter 'x' to exit: ");
                        input = scanner.nextLine().toLowerCase();
                        continue;
                    } else {
                        Card c = player.getCards().get(cardNum-1);
                        c.getTrait().upgrade();
                        System.out.println("\n\tUpgraded " + c.getName() + "!");
                        player.getCards().set(cardNum-1, c);
                        player.getInventory().decnumUpgdCards();
                    }
                }
            }
        } while (!input.equals("x") && player.getInventory().getnumUpgdCards() != 0);

        if (player.getInventory().getnumUpgdCards() == 0) { 
            System.out.println("\n\tYou've used all your upgrade cards.\n");
        } else {
            System.out.println();
        }
    }
      
    private static int spin(Player player) {
        int totalProb = 0;
        int inc = 0;
        boolean isCardFound = false;
        boolean reroll = false;
        int totalDmg = 0;
        List<Card> cards = player.getCards();

        for (var card : cards) {
            totalProb += card.getProb();
        }

        do {
            int cardselect = rand.nextInt(totalProb) + 1;

            for (var card : cards) {
                if ((inc += card.getProb()) > cardselect && !isCardFound) {
                    isCardFound = true;
                    // card.use(totalDmg += card.getDmg());
                    reroll = card.isReroll();
                }
            }
            inc = 0;
            isCardFound = false;
        } while (reroll);

        return totalDmg;
    }

}


