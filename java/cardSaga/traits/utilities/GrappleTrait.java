package cardSaga.traits.utilities;

import cardSaga.traits.Upgradable;

public class GrappleTrait extends Upgradable {
    
    public GrappleTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "steal enemy's card for " + mod + " use";
    }
}
