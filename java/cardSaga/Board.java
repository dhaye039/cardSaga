package cardSaga;

public class Board {

    private static Board instance = new Board(5, 5);

    private char[][] board;
    private final int rows;
    private final int cols;

    private Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new char[rows][cols];

        // Initialize the board with empty spaces
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = '-'; // Empty cell representation
            }
        }
        board[0][0] = 'p';
    }

    // Method to display the board
    public void displayBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Method to make a move on the board
    public boolean makeMove(int row, int col, char playerSymbol) {
        if (row >= 0 && row < rows && col >= 0 && col < cols && board[row][col] == '-') {
            board[row][col] = playerSymbol;
            return true;
        } else {
            System.out.println("Invalid move.");
            return false;
        }
    }

    public static synchronized Board getInstance() {
        return instance;
    }
}
