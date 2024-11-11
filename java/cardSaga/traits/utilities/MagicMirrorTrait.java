package cardSaga.traits.utilities;

import cardSaga.traits.Upgradable;

public class MagicMirrorTrait extends Upgradable {

    public MagicMirrorTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "copy the card the opposing player used for and add it to your inventory for " + mod + " use";
    }
}
