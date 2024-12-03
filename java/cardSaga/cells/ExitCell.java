package cardSaga.cells;

public class ExitCell extends Cell {

    boolean isLocked;

    public ExitCell(boolean isLocked) {
        super();
        this.isLocked = isLocked;
        this.val = (isLocked) ? '#' : 'o';
    }

    public void unlock() {
        isLocked = true;
    }

    public boolean isLocked() {
        return isLocked;
    }
}
