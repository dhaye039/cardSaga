package cardSaga;

// lwk forget what 'Con' is but i think its for consequence

class EnemyWeaponTrait extends NonUpgdable {
    
    @Override
    public String getDesc() {
        return "No effect";
    }
}

class StealCon extends NonUpgdable {

    @Override
    public String getDesc() {
        return "Deletes the last card the opposing player used";
    }
}

class RobCon extends Upgdable {

    public RobCon(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "Opposing player loses " + mod + " additional gold (does not go negative)";
    }
}

