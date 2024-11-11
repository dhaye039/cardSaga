package cardSaga;

import java.util.Random;
import java.util.Scanner;

public class MazeGame {
    private MazeGenerator maze;
    private int playerRow;
    private int playerCol;
    private Random random;

    public MazeGame(int rows, int cols) {
        maze = new MazeGenerator(rows, cols);
        playerRow = 0;
        playerCol = 0;  // Start position at the top-left corner
        random = new Random();
    }

    // Roll a dice to determine how many spaces the player can move
    // public int rollDice() {
    //     return random.nextInt(6) + 1;  // Returns a random number between 1 and 6
    // }

    // Move the player based on the number of spaces rolled
    public boolean movePlayer(String direction, int spaces) {
            switch (direction.toLowerCase()) {
                case "w":
                    if (playerRow > 0 && maze.getMaze()[playerRow][playerCol].canMoveUp()) {
                        playerRow--;
                        spaces--;
                    } else {
                        return false;  // Can't move in this direction
                    }
                    break;
                case "s":
                    if (playerRow < maze.getMaze().length - 1 && maze.getMaze()[playerRow][playerCol].canMoveDown()) {
                        playerRow++;
                        spaces--;
                    } else {
                        return false;  // Can't move in this direction
                    }
                    break;
                case "a":
                    if (playerCol > 0 && maze.getMaze()[playerRow][playerCol].canMoveLeft()) {
                        playerCol--;
                        spaces--;
                    } else {
                        return false;  // Can't move in this direction
                    }
                    break;
                case "d":
                    if (playerCol < maze.getMaze()[0].length - 1 && maze.getMaze()[playerRow][playerCol].canMoveRight()) {
                        playerCol++;
                        spaces--;
                    } else {
                        return false;  // Can't move in this direction
                    }
                    break;
            }
        return true;  // Successfully moved the full number of spaces
    }

    public void displayMaze() {
        for (int i = 0; i < maze.getMaze().length; i++) {
            for (int j = 0; j < maze.getMaze()[i].length; j++) {
                if (i == playerRow && j == playerCol) {
                    System.out.print("p ");  // Player's position
                } else {
                    System.out.print(". ");  // Empty space
                }
            }
            System.out.println();
        }
    }
}
