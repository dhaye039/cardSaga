import java.util.*;

import cardSaga.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Random rand = new Random();
    static MasterList MasterList = cardSaga.MasterList.getInstance(); 
    static int level = 1, currMazeLvlIdx = 0, turn = 1;
    static int rows = 5, cols = 4; // set to 4 but will get incremented to 5
    static Maze maze;
    static boolean cardsInShop = true, isLevelCompleted = false;
    static ArrayList<Maze> mazeList = new ArrayList<>();
    static boolean gameover = false;

    public static void main(String[] args) {
        

        // Start game
        Player p = startGame();
        
        int numTurn = 0;

        // Game loop
        while (!gameover && numTurn != 100) {

            System.out.println("\t\t\t\t\t    Turn " + turn + "\n");
            System.out.println("\t\t\t\t\t   Level  " + level + "\n");
            System.out.println("Current Options: ");
            System.out.println("Check [I]nventory | [H]elp | [M]ove | E[x]it Game\n");
            
            System.out.println("Level " + level + " Map:\n");
            maze.print();

            String strTurn = procTurn();
            
            switch (strTurn) {
                case "i":
                    p.viewInventory();
                    break;
                case "h":
                    help();
                    break;
                case "w":
                case "a":
                case "s":
                case "d":
                case "m":
                    move(p, strTurn);
                    ++turn;
                    break;
                case "x":
                    System.out.println();
                    gameover = true;
                    break;
                default:
                    System.out.println("Not implemented yet.");
            }

            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

            if (p.getHp() < 0) {
                System.out.println("You died. :(");
                gameover = true;
            }
            numTurn++;
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
            "x"     // exit
            // , "f"   // fight
            , "h"   // help
            , "i"   // inventory
            , "m"   // move
            , "w"
            , "a"
            , "s"
            , "d"
            // , "r"   // roll
            // , "s"   // shop
            // , "u"   // upgrade
        ));

        // board.displayBoard();
        System.out.print("What would you like to do: ");
        String turn = scanner.nextLine().toLowerCase();

        while (!validOptions.contains(turn)) {
            System.out.print("Please enter 'i', 'h', 'm', or 'x': ");
            turn = scanner.nextLine();
        }
        return turn;
    }
        
    private static void move(Player p, String strTurn) {
        String dir;

        List<String> dirOptions = new ArrayList<>(Arrays.asList(
            "w", "a", "s", "d", "x", "i"
        ));

        System.out.println();
        // maze.print();

        while (!gameover) {
            if (strTurn.equals("m")) {
                System.out.print("Key - up [w] | down [s] | left [a] | right [d]\nEnter a Direction or press [x] to return to the option screen: ");
                dir = scanner.nextLine().toLowerCase();
                dir = dir.isEmpty() ? "" : String.valueOf(dir.charAt(dir.length() - 1));
                while(!dirOptions.contains(dir)) {
                    System.out.print("Please enter a valid a direction (w, s, a, d) or 'x': ");
                    dir = scanner.nextLine().toLowerCase();
                    dir = dir.isEmpty() ? "" : String.valueOf(dir.charAt(dir.length() - 1));
                }
    
                System.out.println();
    
                if (dir.equals("x"))
                    return;

                if (dir.equals("i")) {
                    p.viewInventory(); maze.print(); continue; 
                }

            } else {
                dir = strTurn;
                strTurn = "m";
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
                    // return;
                } else if (maze.isAtEntrance()) {
                    System.out.println("\nLevel " + currMazeLvlIdx + " Map:\n");
                    maze = mazeList.get(currMazeLvlIdx - 1);
                    maze.setPlayerAtExit();
                    currMazeLvlIdx--;
                } else {
                    // u lost fight L
                }

            } else {
                
            }

            maze.print();

            if (p.getHp() <= 0) {
                gameover = true;
            }
        }
    }

    private static void help() {
        String help = """

        ----------------------------------------------------------------------------------------------------
        
                                                    Help Page:
        Description:
            Card Saga is a turn based rogue-like where you try to make it as far as you can into the maze!

            Each level increases in size and monster count, and monsters grow stronger with each new level,
            with bosses on every 5th level. 
        
        Fighting:
            For every enemy encounter, you will randomly draw cards that determine your damage (the enemy
            will do the same). Upon defeat, the enemy's card trait will be applied to you, otherwise, you
            will not receive consequences aside from health decrements. Certain cards have certain affects
            that will allow you to redraw a card from your inventory, while affecting player/enemy damage.

        Health:
            Health is gain/lost via enemy encounters: 
                health gain/lost = player damage - enemy damage

        Cards:
            Cards contain a unique trait that is applied when fighting enemies.

        Map Key:
            P - player (you)
            . - path
            X - wall
            , - broken wall path
            o - entrance/exit (exits always appear on the last column)
            e - enemy
            b - boss
            s - shop
            n - anvil
            r - broken anvil

        Controls:
            m - move
            w - up
            a - left
            s - down
            d - right
            x - go back/exit
            i - inventory
                """;
        System.out.println(help);
        String input;
        do {
            System.out.print("Enter x to return: ");
            input = scanner.nextLine().toLowerCase();
        } while (!input.equals("x"));
    }
}