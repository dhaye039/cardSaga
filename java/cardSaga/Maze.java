package cardSaga;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Maze {
    private static final char PATH = '.';
    private static final char WALL = 'X';
    private static final char PLAYER = 'p';
    private static final char EXIT = 'o';
    private static final char ENEMY = 'e';

    private Cell[][] maze;
    private int rows, cols;
    private int playerRow = 0;
    private int playerCol = 0;
    private int exitRow, exitCol;


    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.exitCol = cols - 1;
        this.maze = new Cell[rows][cols];
        do {
            maze = generateMaze();
        } while (!pathExists());
        maze[playerRow][playerCol].setValue(PLAYER); // Start player position
    }

    public Cell[][] generateMaze() {
        Random random = new Random();

        int mobCap = ((rows * cols) / 25) + 1;

        // Initialize the grid with paths and walls
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                // 30% chance of wall
                if (random.nextDouble() < 0.3)
                    maze[i][j].setValue(WALL);
                else
                    maze[i][j].setValue(PATH);
                if (maze[i][j].getValue() == PATH) {
                    if (mobCap > 0 && random.nextDouble() < 0.2) {
                        maze[i][j].setValue(ENEMY); // 20% chance an enemy
                        --mobCap;
                    }
                }
            }
        }

        // Set start and exit positions
        maze[0][0].setValue(PLAYER);

        // Place exit in random last column row
        this.exitRow = random.nextInt(rows);
        maze[exitRow][cols - 1].setValue(EXIT);

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
            if (col == cols - 1 && maze[row][col].getValue() == EXIT) {
                return true;
            }

            // Explore neighboring cells
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                // Check if the new position is within bounds and is a path, and not visited
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols 
                        && maze[newRow][newCol].getValue() != WALL && !visited[newRow][newCol]) {
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

        // Check if the new position is within bounds and is a path
        if (newRow >= 0 && newRow < maze.length && newCol >= 0 && newCol < maze[0].length 
                && maze[newRow][newCol].getValue() != WALL) {
            maze[playerRow][playerCol].setValue(PATH); // Clear previous player position
            playerRow = newRow;
            playerCol = newCol;
            maze[playerRow][playerCol].setValue(PLAYER); // Update new player position
            return true;
        }

        return false;
    }

    public boolean isAtExit() {
        return playerRow == exitRow && playerCol == exitCol;
    }

    public void print() {
        // Print the generated maze
        for (Cell[] row : maze) {
            for (Cell cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}

