package cardSaga.traits.consequences;

import cardSaga.traits.Upgradable;

public class RobCon extends Upgradable {

    public RobCon(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "Opposing player loses " + mod + " additional gold (does not go negative)";
    }
}
