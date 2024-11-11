package cardSaga.traits.potions;

import cardSaga.traits.Upgradable;

public class StrengthTrait extends Upgradable {
    
    public StrengthTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "+" + mod + " dmg to next attack";
    }
}
