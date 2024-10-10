package cardSaga;

public class Cell {
    private boolean canMoveUp;
    private boolean canMoveDown;
    private boolean canMoveLeft;
    private boolean canMoveRight;

    public Cell(boolean canMoveUp, boolean canMoveDown, boolean canMoveLeft, boolean canMoveRight) {
        this.canMoveUp = canMoveUp;
        this.canMoveDown = canMoveDown;
        this.canMoveLeft = canMoveLeft;
        this.canMoveRight = canMoveRight;
    }

    public boolean canMoveUp() {
        return canMoveUp;
    }

    public boolean canMoveDown() {
        return canMoveDown;
    }

    public boolean canMoveLeft() {
        return canMoveLeft;
    }

    public boolean canMoveRight() {
        return canMoveRight;
    }
}

