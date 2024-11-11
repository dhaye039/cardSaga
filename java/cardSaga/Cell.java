package cardSaga;

public class Cell {
    private boolean canMoveUp;
    private boolean canMoveDown;
    private boolean canMoveLeft;
    private boolean canMoveRight;
    private boolean visited;

    public Cell(boolean up, boolean down, boolean left, boolean right) {
        this.canMoveUp = up;
        this.canMoveDown = down;
        this.canMoveLeft = left;
        this.canMoveRight = right;
        this.visited = false;
    }

    public void setCanMoveUp(boolean canMoveUp) {
        this.canMoveUp = canMoveUp;
    }

    public void setCanMoveDown(boolean canMoveDown) {
        this.canMoveDown = canMoveDown;
    }

    public void setCanMoveLeft(boolean canMoveLeft) {
        this.canMoveLeft = canMoveLeft;
    }

    public void setCanMoveRight(boolean canMoveRight) {
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

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}

