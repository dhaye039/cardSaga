package cardSaga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MazeGenerator {
    private final int rows;
    private final int cols;
    private final Cell[][] maze;
    private final Random random;

    public MazeGenerator(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.maze = new Cell[rows][cols];
        this.random = new Random();

        initializeMaze();
        generateMaze(0, 0); // Start maze generation from the top-left corner
    }

    private void initializeMaze() {
        // Initialize each cell with all walls up
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                maze[row][col] = new Cell(true, true, true, true);  // All walls are up initially
            }
        }
    }

    // Generate the maze using recursive backtracking
    private void generateMaze(int row, int col) {
        maze[row][col].setVisited(true); // Mark the starting cell as visited

        // Randomize the order of directions to ensure randomness in path carving
        List<int[]> directions = getRandomDirections();

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            // Check if the new cell is within maze bounds and unvisited
            if (isValidCell(newRow, newCol) && !maze[newRow][newCol].isVisited()) {
                // Remove the wall between the current cell and the chosen neighbor
                removeWall(row, col, newRow, newCol);
                
                // Recursively generate the maze from the chosen cell
                generateMaze(newRow, newCol);
            }
        }
    }

    private List<int[]> getRandomDirections() {
        List<int[]> directions = new ArrayList<>();
        directions.add(new int[]{-1, 0}); // Up
        directions.add(new int[]{1, 0});  // Down
        directions.add(new int[]{0, -1}); // Left
        directions.add(new int[]{0, 1});  // Right
        Collections.shuffle(directions);  // Randomize the directions
        return directions;
    }

    // Check if the cell is within the maze bounds
    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    // Remove the wall between two neighboring cells
    private void removeWall(int row, int col, int newRow, int newCol) {
        if (newRow < row) {  // Neighbor is above
            maze[row][col].setCanMoveUp(true);
            maze[newRow][newCol].setCanMoveDown(true);
        } else if (newRow > row) {  // Neighbor is below
            maze[row][col].setCanMoveDown(true);
            maze[newRow][newCol].setCanMoveUp(true);
        } else if (newCol < col) {  // Neighbor is to the left
            maze[row][col].setCanMoveLeft(true);
            maze[newRow][newCol].setCanMoveRight(true);
        } else if (newCol > col) {  // Neighbor is to the right
            maze[row][col].setCanMoveRight(true);
            maze[newRow][newCol].setCanMoveLeft(true);
        }
    }

    public Cell[][] getMaze() {
        return maze;
    }

    // Display the maze layout in text format (optional)
    public void displayMaze() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Cell cell = maze[row][col];
                System.out.print(cell.isVisited() ? " " : "#");
            }
            System.out.println();
        }
    }
}