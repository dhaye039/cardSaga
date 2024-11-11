package cardSaga.traits;

public abstract class Upgradable implements Trait {
    public int mod;
    public int upgdCap;

    @Override
    public boolean upgrade() {
        if (upgdCap > 0) {
            ++mod; --upgdCap;
            return true;
        }
        return false;
    }

    public int getMod() {
        return mod;
    }

    public int getUpgdCap() {
        return upgdCap;
    }

    public void setUpgdCap(int upgdCap) {
        this.upgdCap = upgdCap;
    }
}
