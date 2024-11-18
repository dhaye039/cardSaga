import java.util.*;

import cardSaga.*;
import cardSaga.cells.Cell;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Random rand = new Random();
    static MasterList MasterList = cardSaga.MasterList.getInstance(); 
    // static Maze maze = new Maze(11, 11);
    public static int level = 1, currMazeLvlIdx = 0, turn = 1;
    static int rows = 5, cols = 4; // set to 4 but will get incremented to 5
    static Maze maze;
    static boolean cardsInShop = true, isLevelCompleted = false;
    static ArrayList<Maze> mazeList = new ArrayList<>();

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
            System.out.println("Check [I]nventory | [M]ove | [E]xit Game\n");

            System.out.println("\nLevel " + level + " Map:\n");
            maze.print();
            
            switch (procTurn()) {
                case "i":
                    p.viewInventory();
                    break;
                case "m":  
                    move(p);
                    ++turn;
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
        mazeList.add(maze);

        return p;
    }

    private static void generateLevel(Player... players) {
        cols += 1;

        if (level % 4 == 0) {
            rows += 2;
            cols = rows;
        }
        
        MasterList.populateShop();
        cardsInShop = true;

        maze = new Maze(rows, cols, players[0], level);
        isLevelCompleted = false;
    }
    
    private static String procTurn() {

        List<String> validOptions = new ArrayList<>(Arrays.asList(
            "e"     // exit
            // , "f"   // fight
            , "i"   // inventory
            , "m"   // move
            // , "r"   // roll
            // , "s"   // shop
            // , "u"   // upgrade
        ));

        // board.displayBoard();
        System.out.print("What would you like to do: ");
        String turn = scanner.nextLine().toLowerCase();

        while (!validOptions.contains(turn)) {
            System.out.print("Please enter 'e', 'i', or 'm': ");
            turn = scanner.nextLine();
        }
        return turn;
    }
        
    private static void move(Player p) {
        String dir;

        List<String> dirOptions = new ArrayList<>(Arrays.asList(
            "w", "a", "s", "d", "e"
        ));

        System.out.println();
        maze.print();

        while (true) {
            System.out.print("Key - up [w] | down [s] | left [a] | right [d]\nEnter a Direction or press [e] return to option screen: ");
            dir = scanner.nextLine().toLowerCase();
            while(!dirOptions.contains(dir)) {
                System.out.print("Please enter a valid a direction (w, s, a, d) or 'e': ");
                dir = scanner.nextLine().toLowerCase();
            }

            System.out.println();

            if (dir.equals("e")) {
                return;
            }

            if (maze.movePlayer(dir, turn)) {
                if (maze.isAtExit()) {
                    // System.out.println("You've reached the exit\n");

                    // check if player is on most recently generated maze
                    if (level == (currMazeLvlIdx + 1)) {
                        level++; currMazeLvlIdx++;
                        generateLevel(p);
                        mazeList.add(maze);
                    } else {
                        System.out.println("\nLevel " + (currMazeLvlIdx + 1) + " Map:\n");
                        maze = mazeList.get(currMazeLvlIdx + 1);
                        maze.setPlayerAtEntrance();
                        currMazeLvlIdx++;
                    }
                    return;
                } else if (maze.isAtEntrance()) {
                    System.out.println("\nLevel " + currMazeLvlIdx + " Map:\n");
                    maze = mazeList.get(currMazeLvlIdx - 1);
                    maze.setPlayerAtExit();
                    currMazeLvlIdx--;
                }
            } else {
                System.out.println("Can't move in that direction.\n");
            }

            maze.print();
        }
    }
}