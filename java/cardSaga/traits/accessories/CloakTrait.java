package cardSaga.traits.accessories;

import cardSaga.traits.NonUpgradable;

public class CloakTrait extends NonUpgradable {
    @Override
    public String getDesc() {
        return "you can choose to skip current fight";
    }
}
