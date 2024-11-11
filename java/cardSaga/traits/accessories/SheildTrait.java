package cardSaga.traits.accessories;

import cardSaga.traits.NonUpgradable;

public class SheildTrait extends NonUpgradable {
    @Override
    public String getDesc() {
        return "ignore consequenses for losing";
    }
}
