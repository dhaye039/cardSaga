package cardSaga.cells;

public class WallCell extends Cell {

    int toughness;

    public WallCell() {
        super();
        this.val = 'X';
        this.toughness = 50;
    } 

    public int getToughness() {
        return toughness;
    }

    public void setToughness(int toughness) {
        this.toughness = toughness;
    }
}