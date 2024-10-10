package cardSaga;

public class Inventory {

    int numUpgdCards;
    int gold;

    public Inventory(int i, int gold) {
        this.numUpgdCards = i;
        this.gold = gold;
    }

    public int getnumUpgdCards() {
        return numUpgdCards;
    }
    public void setnumUpgdCards(int numUpgdCards) {
        this.numUpgdCards = numUpgdCards;
    }
    public void decnumUpgdCards() {
        --numUpgdCards;
    }

    public int getGold() {
        return gold;
    }
    public int setGold(int gold) {
        return this.gold = gold;
    }
    public int addGold(int gold) {
        return this.gold += gold;
    }
}
