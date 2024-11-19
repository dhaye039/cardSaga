package cardSaga.cells;

import java.util.Random;

public class WallCell extends Cell {

    private Random random = new Random();

    int toughness;
    boolean isMined = false;
    int gold;


    public WallCell() {
        super();
        this.val = 'X';
        this.toughness = 50;
        this.gold = 0;
        if (random.nextDouble() < 0.2) 
            this.gold = random.nextInt(10);
    } 

    public int getToughness() {
        return toughness;
    }

    public void setToughness(int toughness) {
        this.toughness = toughness;
    }

    public boolean isMined() {
        return isMined;
    }

    public void changeIsMined() {
        this.isMined = !isMined;
    }

    public int getGold() {
        return gold;
    }
}