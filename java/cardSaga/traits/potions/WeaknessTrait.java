package cardSaga.traits.potions;

import cardSaga.traits.Upgradable;

public class WeaknessTrait extends Upgradable {
    
    public WeaknessTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "enemy attack recieves " + (mod * -1) + " dmg";
    }
}
