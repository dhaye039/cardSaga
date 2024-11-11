package cardSaga.traits.potions;

import cardSaga.traits.NonUpgradable;

public class CritTrait extends NonUpgradable {
    @Override
    public String getDesc() {
        return "doubles next attack (stacks 1 time)";
    }
}
