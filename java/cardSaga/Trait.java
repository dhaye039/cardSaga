package cardSaga;

public interface Trait {
    String getDesc();
    boolean upgrade();
}

abstract class Upgdable implements Trait {
    public int mod;
    public int upgdCap;

    @Override
    public boolean upgrade() {
        if (upgdCap > 0) {
            ++mod; --upgdCap;
            return true;
        }
        return false;
    }

    public int getMod() {
        return mod;
    }

    public int getUpgdCap() {
        return upgdCap;
    }

    public void setUpgdCap(int upgdCap) {
        this.upgdCap = upgdCap;
    }
}

abstract class NonUpgdable implements Trait {
    @Override
    public boolean upgrade() {
        return false;
    }
}

// Weapons
class WeaponTrait extends Upgdable {

    public WeaponTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "+" + mod + " dmg every use";
    }
}

// Utilities
class MagicMirrorTrait extends Upgdable {

    public MagicMirrorTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "copy a random card from enemy (" + mod + " use)";
    }
}

class BombTrait extends Upgdable {

    public BombTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "explodes after use (restores in " + mod + " turns)";
    }

    @Override
    public boolean upgrade() {
        if (upgdCap > 0) {
            --mod; --upgdCap;
            return true;
        }
        return false;
    }
}

class SmokeArrowTrait extends Upgdable {

    public SmokeArrowTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "enemy is blinded, spin again (+" + mod + " dmg to next attack)";
    }
}

class GrappleTrait extends Upgdable {
    
    public GrappleTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "copy enemy's card (" + mod + " use)";
    }
}

// Potions
class StrengthTrait extends Upgdable {
    
    public StrengthTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "+" + mod + " dmg to next attack";
    }
}

class ConfusionTrait extends NonUpgdable {
    @Override
    public String getDesc() {
        return "causes enemy to reroll";
    }
}

class WeaknessTrait extends Upgdable {
    
    public WeaknessTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "enemy attack recieves " + (mod * -1) + " dmg";
    }
}

class LuckTrait extends Upgdable {
    
    public LuckTrait(int mod, int upgdCap) {
        this.mod = mod;
        this.upgdCap = upgdCap;
    }

    @Override
    public String getDesc() {
        return "increases chance of rolling highest dmg card by " + mod + "%";
    }

    @Override
    public boolean upgrade() {
        if (upgdCap > 0) {
            mod += 5; --upgdCap;
            return true;
        }
        return false;
    }
}

class CritTrait extends NonUpgdable {
    @Override
    public String getDesc() {
        return "doubles next attack (stacks 1 time)";
    }
}

// Armor


// Accessories

class SheildTrait extends NonUpgdable {
    @Override
    public String getDesc() {
        return "ignore consequenses for losing";
    }
}

class CloakTrait extends NonUpgdable {
    @Override
    public String getDesc() {
        return "you can choose to skip current fight";
    }
}