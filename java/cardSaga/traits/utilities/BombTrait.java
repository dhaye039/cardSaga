package cardSaga.traits.utilities;

import cardSaga.traits.Upgradable;

public class BombTrait extends Upgradable {

    int turn;

    public BombTrait(int mod, int upgdCap, int turn) {
        this.mod = mod;
        this.upgdCap = upgdCap;
        this.turn = turn;
    }

    @Override
    public String getDesc() {
        return "explodes after use (restores in " + mod + " turns)";
    }

    @Override
    public boolean upgrade() {
        if (upgdCap > 0) {
            --mod; --upgdCap;
            return true;
        }
        return false;
    }

    public int getTurn() {
        return this.turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
