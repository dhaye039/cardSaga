import java.util.*;

import cardSaga.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Random rand = new Random();
    static MasterList MasterList = cardSaga.MasterList.getInstance(); 
    // static Maze maze = new Maze(11, 11);
    public static int level = 1, turn = 1;
    static int rows = 5, cols = 4; // set to 4 but will get incremented to 5
    static Maze maze;
    static boolean cardsInShop = true, isLevelCompleted = false;

    public static void main(String[] args) {
        boolean gameover = false;

        // Start game
        Player p = startGame();
        
        int numTurn = 0;

        // Game loop
        while (!gameover && numTurn != 100) {

            System.out.println("\t\t\t\t\t    Turn " + turn + "\n");
            System.out.println("\t\t\t\t\t   Level  " + level + "\n");
            System.out.println("Current Options: ");
            System.out.println("Check [I]nventory | View [M]ap | [R]oll | [E]xit Game\n");
            
            if (isLevelCompleted) {
                MasterList.populateShop();
                cardsInShop = true;
                generateLevel(p);
            }
            
            switch (procTurn()) {
                case "i":
                    p.viewInventory();
                    break;
                case "r":  
                    int rollNum = 0;
                    rollNum = (rand.nextInt(6) + 1);               
                    System.out.println("\n\tYou rolled a " + rollNum + ".\n");
                    roll(rollNum);
                    ++turn;
                    break;
                // case "u":
                    // if (p.getInventory().getnumUpgdCards() == 0)
                    //     System.out.println("\n\tYou have no Upgrade Cards.\n");
                    // else 
                    //     upgdCard(p);
                    // break;
                case "m":
                
                    System.out.println("\nLevel " + level + " Map:\n");
                    maze.print();
                    break;
                case "e":
                    System.out.println();
                    gameover = true;
                    break;
                default:
                    System.out.println("Not implemented yet.");
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

        generateLevel(p);

        return p;
    }

    private static void generateLevel(Player... players) {
        cols += 1;

        if (level % 4 == 0) {
            rows += 2;
            cols = rows;
        }

        maze = new Maze(rows, cols, players[0], level);
        isLevelCompleted = false;
    }
    
    private static String procTurn() {

        List<String> validOptions = new ArrayList<>(Arrays.asList(
            "e"     // exit
            // , "f"   // fight
            , "i"   // inventory
            , "m"   // map/maze
            , "r"   // roll
            // , "s"   // shop
            // , "u"   // upgrade
        ));

        // board.displayBoard();
        System.out.print("What would you like to do: ");
        String turn = scanner.nextLine().toLowerCase();

        while (!validOptions.contains(turn)) {
            System.out.print("Please enter 'e', 'i', 'm', 'r', 's', or 'u': ");
            turn = scanner.nextLine();
        }
        return turn;
    }
        
    private static void roll(int diceRoll) {
        String dir;

        List<String> dirOptions = new ArrayList<>(Arrays.asList(
            "w", "a", "s", "d"
        ));

        maze.print();

        while (diceRoll != 0) {
            System.out.print("Enter a Direction (Up [w], down [s], left [a], right [d]): ");
            dir = scanner.nextLine().toLowerCase();
            while(!dirOptions.contains(dir)) {
                System.out.print("Please enter a valid a direction (w, s, a, d): ");
                dir = scanner.nextLine().toLowerCase();
            }

            System.out.println();

            if (maze.movePlayer(dir, turn)) {
                --diceRoll;
                if (maze.isAtExit()) {
                    System.out.println("Congratulations! You've reached the exit!\n");
                    isLevelCompleted = true;
                    level++;
                    break;
                }
            } else {
                System.out.println("Can't move in that direction.\n");
            }

            maze.print();
        }
    }
}