package cardSaga.traits.potions;

import cardSaga.traits.Upgradable;

public class LuckTrait extends Upgradable {
    
    public LuckTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "increases chance of rolling highest dmg card by " + mod + "%";
    }

    @Override
    public boolean upgrade() {
        if (upgdCap > 0) {
            mod += 5; --upgdCap;
            return true;
        }
        return false;
    }
}
