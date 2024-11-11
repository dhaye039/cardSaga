package cardSaga.traits.utilities;

import cardSaga.traits.Upgradable;

public class SmokeArrowTrait extends Upgradable {

    public SmokeArrowTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "enemy is blinded, spin again (+" + mod + " dmg to next attack)";
    }
}
