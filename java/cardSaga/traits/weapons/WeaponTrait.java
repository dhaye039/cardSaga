package cardSaga.traits.weapons;

import cardSaga.traits.Upgradable;

public class WeaponTrait extends Upgradable {

    public WeaponTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "+" + mod + " dmg every use";
    }
}
