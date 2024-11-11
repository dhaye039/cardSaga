package cardSaga.traits.consequences;

import cardSaga.traits.NonUpgradable;

public class StealCon extends NonUpgradable {

    @Override
    public String getDesc() {
        return "Deletes the last card the opposing player used";
    }
}
