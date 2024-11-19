package cardSaga;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

import cardSaga.cells.AnvilCell;
import cardSaga.cells.Cell;
import cardSaga.cells.EnemyCell;
import cardSaga.cells.EntranaceCell;
import cardSaga.cells.ExitCell;
import cardSaga.cells.PathCell;
import cardSaga.cells.ShopCell;
import cardSaga.cells.WallCell;


public class Maze {
    private Scanner scanner = new Scanner(System.in);
    MasterList masterlist = MasterList.getInstance();

    private static final char PLAYER_ICON = 'p';

    private Cell[][] maze;
    private int rows, cols;
    private int playerRow = 0;
    private int playerCol = 0;
    private int exitRow, exitCol, level, anvilUses;
    private boolean cardsInShop;
    private Player p;



    public Maze(int rows, int cols, Player p, int level) {
        this.rows = rows;
        this.cols = cols;
        this.p = p;
        this.level = level;
        this.cardsInShop = true;
        this.anvilUses = 2;

        this.maze = new Cell[rows][cols];
        do {
            maze = generateMaze();
        } while (!pathExists());
    }

    public Cell[][] generateMaze() {
        Random random = new Random();

        int mobCap = ((rows * cols) / 25) + 1;

        // Initialize the grid
        // This is where to add new cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                if (random.nextDouble() < 0.3)
                    maze[i][j] = new WallCell(); // 30% chance of wall
                else
                    maze[i][j] = new PathCell();

                if (maze[i][j] instanceof PathCell) {
                    if (mobCap > 0 && random.nextDouble() < 0.2) {
                        maze[i][j] = new EnemyCell("goblin"); // 20% chance an enemy
                        --mobCap;
                    }
                }
            }
        }

        if (level > 1) {
            maze[0][0] = new EntranaceCell();
        } else {
            maze[0][0] = new PathCell();
        }

        // Set start and exit positions
        maze[0][0].setVal(PLAYER_ICON);

        // Place exit in random last column row
        this.exitRow = random.nextInt(rows);
        this.exitCol = cols - 1;
        maze[exitRow][cols - 1] = new ExitCell();

        // Place special cells
        placeCell(new ShopCell());

        if (level > 0) 
            placeCell(new AnvilCell());

        return maze;
    }

    private boolean pathExists() {
        // Use BFS or DFS to check if there's a path from start to exit
        int rows = maze.length;
        int cols = maze[0].length;
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();

        // Start from the start position
        queue.add(new int[] {0, 0});
        visited[0][0] = true;

        // BFS directions for up, down, left, right
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int row = pos[0];
            int col = pos[1];

            // Check if we've reached any cell in the last column with the exit
            if (col == cols - 1 && maze[row][col] instanceof ExitCell) {
                return true;
            }

            // Explore neighboring cells
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                // Check if the new position is within bounds and is a path, and not visited
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols 
                        && !(maze[newRow][newCol] instanceof WallCell) 
                        && !visited[newRow][newCol] 
                        && !(maze[newRow][newCol] instanceof ShopCell)
                    ) {
                    visited[newRow][newCol] = true;
                    queue.add(new int[] {newRow, newCol});
                }
            }
        }

        // If we finish BFS without finding the exit, no path exists
        return false;
    }

    public boolean movePlayer(String direction, int turn) {
        int newRow = playerRow;
        int newCol = playerCol;

        switch (direction) {
            case "w": newRow--; break; // Up
            case "s": newRow++; break; // Down
            case "a": newCol--; break; // Left
            case "d": newCol++; break; // Right
        }

        // Check if the new position is within bounds and is a path
        if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length) {

            // perform action
            if (maze[newRow][newCol] instanceof EnemyCell) {
                Enemy enemy = ((EnemyCell) maze[newRow][newCol]).getEnemy();
                boolean playerWon = new Fight(p, enemy, turn).startFight(); // Start with turn = 0
    
                if (playerWon) {
                    System.out.println("You defeated the enemy and moved into their space!\n");
                } else {
                    return false;
                }
            } else if (maze[newRow][newCol] instanceof ShopCell) {
                // TODO: add y/n enter shop
                if (cardsInShop)
                    ((ShopCell) maze[newRow][newCol]).visitShop(p);
                else 
                    System.out.println("\tThere are currently no cards in the shop.\n");
            } else if (maze[newRow][newCol] instanceof AnvilCell) {
                if (p.getInventory().getnumUpgdCards() == 0)
                    System.out.println("\tYou have no Upgrade Cards.\n");
                else 
                    upgdCard(p);
            } else if (maze[newRow][newCol] instanceof WallCell) {
                WallCell wall = (WallCell)maze[newRow][newCol];
                int waitTime = wall.getToughness() * 40;
                waitTime = waitTime / 4;
                if (!wall.isMined()) {
                    System.out.print("Mining wall"); waiting(waitTime);
                    System.out.print("."); waiting(waitTime);
                    System.out.print("."); waiting(waitTime);
                    System.out.println("."); waiting(waitTime);
                    wall.changeIsMined();

                    int gold = wall.getGold();
                    if (gold > 0) {
                        System.out.println("You found " + gold + " gold in the wall!");
                        p.inventory.addGold(wall.getGold());
                    }

                    System.out.print("\n");
                }
            }
            
            // Clear previous player position
            if (maze[playerRow][playerCol] instanceof ShopCell) {
                maze[playerRow][playerCol].setVal('s');
            } else if (maze[playerRow][playerCol] instanceof AnvilCell && anvilUses != 0) {
                if (anvilUses == 2)
                    maze[playerRow][playerCol].setVal('n');
                else
                    maze[playerRow][playerCol].setVal('r');
            } else if (maze[playerRow][playerCol] instanceof EntranaceCell || maze[playerRow][playerCol] instanceof ExitCell) {
                maze[playerRow][playerCol].setVal('o');
            } else if (maze[playerRow][playerCol] instanceof WallCell) {
                maze[playerRow][playerCol].setVal(',');
            } else /* (maze[playerRow][playerCol] instanceof PathCell) */ {
                maze[playerRow][playerCol] = new PathCell(); 
            }

            playerRow = newRow;
            playerCol = newCol;
            maze[playerRow][playerCol].setVal(PLAYER_ICON); // Update new player position
            return true;
        }
        System.out.println("Can't move in that direction.\n");
        return false;
    }
        
    // special cell stuff
    private void upgdCard(Player player) {

        Inventory inventory = player.getInventory();
        String input = "";

        do {
            List<Integer> upgdable = inventory.getUpgdable();
            int cardNum = -1;
            boolean isValidInput = false;

            if (upgdable.isEmpty()) {
                System.out.println("\tYou have no upgradable cards!");
                break;
            } else {
                System.out.print("What card would you like to upgrade? [number]: ");
                input = scanner.nextLine().toLowerCase();

                while (!isValidInput && !input.equals("x") && (anvilUses > 0)) {
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
                        --anvilUses;
                    }
                }
            }
        } while (!input.equals("x") && player.getInventory().getnumUpgdCards() != 0 && (anvilUses > 0));

        if (player.getInventory().getnumUpgdCards() == 0) { 
            System.out.println("\n\tYou've used all your upgrade cards.\n");
        } else {
            System.out.println();
        }
    }
    
    // logic/functional methods
    private int randBetween(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
    
    private void placeCell(Cell cell) {
        int cellRow, cellCol;

        do {
            cellRow = randBetween(0, rows - 1);
            cellCol = randBetween(0, cols - 1);
        } while (
            maze[cellRow][cellCol].getVal() == PLAYER_ICON 
         || maze[cellRow][cellCol] instanceof ExitCell
         || maze[cellRow][cellCol] instanceof ShopCell
         || maze[cellRow][cellCol] instanceof AnvilCell
        );

        if (cell instanceof ShopCell)
            maze[cellRow][cellCol] = new ShopCell();
        else if (cell instanceof AnvilCell)
            maze[cellRow][cellCol] = new AnvilCell();
    }

    private void waiting(int millis) {
        try {
            Thread.sleep(millis); // Pauses for 1 second (1000 milliseconds)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // UI stuff
    public void print() {
        // Print the generated maze
        // System.out.println();
        for (Cell[] row : maze) {
            System.out.print("\t");
            for (Cell cell : row) {
                System.out.print(cell.getVal() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void setPlayerAtExit() {
        playerRow = exitRow;
        playerCol = exitCol;
        maze[playerRow][playerCol].setVal(PLAYER_ICON);
    }
    
    public boolean isAtExit() {
        return maze[playerRow][playerCol] instanceof ExitCell;
    }

    public void setPlayerAtEntrance() {
        playerRow = 0;
        playerCol = 0;
        maze[playerRow][playerCol].setVal(PLAYER_ICON);
    }

    public boolean isAtEntrance() {
        return maze[playerRow][playerCol] instanceof EntranaceCell;
    }
   
}

