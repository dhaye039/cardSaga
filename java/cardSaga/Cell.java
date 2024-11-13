package cardSaga;

public class Cell {

    char val;
    CellType cellType;

    public Cell(char value) {
        this.val = value;
        this.cellType = null;
    }

    public char getVal() {
        return val;
    }

    public void setVal(char value) {
        this.val = value;
    }

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        this.cellType = cellType;
    }
}
