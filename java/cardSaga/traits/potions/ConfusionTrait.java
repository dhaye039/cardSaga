package cardSaga.traits.potions;

import cardSaga.traits.NonUpgradable;

public class ConfusionTrait extends NonUpgradable {
    @Override
    public String getDesc() {
        return "causes enemy to reroll";
    }
}
