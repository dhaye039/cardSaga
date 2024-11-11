package cardSaga.traits;

public abstract class NonUpgradable implements Trait {
    @Override
    public boolean upgrade() {
        return false;
    }
}
