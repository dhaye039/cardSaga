package cardSaga.cells;
public abstract class Cell {
    char val;

    public Cell(char c) {
        this.val = c;
    }

    // Abstract interact method that will be overridden by subclasses
    public abstract void interact();

    public char getVal() {
        return val;
    }

    public void setVal(char val) {
        this.val = val;
    }
}
