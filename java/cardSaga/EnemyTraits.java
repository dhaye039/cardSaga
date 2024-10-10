package cardSaga;

// lwk forget what 'Con' is but i think its for consequence

class KnifeCon extends NonUpgdable {
    
    @Override
    public String getDesc() {
        return "No effect";
    }
}

class StealCon extends NonUpgdable {

    @Override
    public String getDesc() {
        return "Deletes the last card you used";
    }
}

class RobCon extends Upgdable {

    public RobCon(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "You lose " + mod + " additional gold (does not go negative)";
    }
}

