package cardSaga;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import cardSaga.cells.Cell;
import cardSaga.cells.EnemyCell;
import cardSaga.cells.ExitCell;
import cardSaga.cells.PathCell;
import cardSaga.cells.WallCell;


public class Maze {
    private static final char PLAYER_ICON = 'p';

    private Cell[][] maze;
    private int rows, cols;
    private int playerRow = 0;
    private int playerCol = 0;
    private int exitRow, level;
    // int turn, level;
    private Player p;


    public Maze(int rows, int cols, Player p, int level) {
        this.rows = rows;
        this.cols = cols;
        this.p = p;
        this.level = level;

        this.maze = new Cell[rows][cols];
        do {
            maze = generateMaze();
        } while (!pathExists());
    }

    public Cell[][] generateMaze() {
        Random random = new Random();

        int mobCap = ((rows * cols) / 25) + 1;

        // Initialize the grid
        // THis is where to add new cells
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

        // Set start and exit positions
        maze[0][0].setVal(PLAYER_ICON);
        // maze[0][0].setCellType(CellType.PLAYER);

        // Place exit in random last column row
        this.exitRow = random.nextInt(rows);
        // maze[exitRow][cols - 1].setVal(EXIT);
        maze[exitRow][cols - 1] = new ExitCell();

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
                        && !(maze[newRow][newCol] instanceof WallCell) && !visited[newRow][newCol]) {
                    visited[newRow][newCol] = true;
                    queue.add(new int[] {newRow, newCol});
                }
            }
        }

        // If we finish BFS without finding the exit, no path exists
        return false;
    }

    public boolean movePlayer(String direction) {
        int newRow = playerRow;
        int newCol = playerCol;

        switch (direction) {
            case "w": newRow--; break; // Up
            case "s": newRow++; break; // Down
            case "a": newCol--; break; // Left
            case "d": newCol++; break; // Right
        }

        if (maze[newRow][newCol] instanceof EnemyCell) {
            Enemy enemy = ((EnemyCell) maze[newRow][newCol]).getEnemy();
            boolean playerWon = new Fight(p, enemy, 0).startFight(); // Start with turn = 0

            if (playerWon) {
                System.out.println("You defeated the enemy and moved into their space!\n");
                maze[playerRow][playerCol] = new PathCell(); // Clear previous position
                playerRow = newRow;
                playerCol = newCol;
                maze[playerRow][playerCol] = new PathCell(); // Enemy "dies"
            } else {
                // System.out.println("You lost the fight and stay in your current position.\n");
                return true;
            }
        }

        // Check if the new position is within bounds and is a path
        if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length 
                && !(maze[newRow][newCol] instanceof WallCell)) {
            maze[playerRow][playerCol] = new PathCell(); // Clear previous player position
            playerRow = newRow;
            playerCol = newCol;
            maze[playerRow][playerCol].setVal(PLAYER_ICON); // Update new player position
            return true;
        }

        return false;
    }

    public boolean isAtExit() {
        return maze[playerRow][playerCol] instanceof ExitCell;
    }

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
}

