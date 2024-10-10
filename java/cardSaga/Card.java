package cardSaga;

// import java.util.*;

public class Card {

    String name;
    Type type;
    int dmg;
    Trait trait;
    boolean reroll;
    float prob;
    int cost;
    Effect effect;

    public Card(String name, Type type, int dmg, Trait trait, boolean reroll, float prob, int cost) {
        this.name = name;
        this.type = type;
        this.dmg = dmg;
        this.trait = trait;
        this.reroll = reroll;
        this.prob = prob;
        this.cost = cost;
        this.effect = getStats();
    }

    public Effect getStats() {
        boolean affectOpp = false;
        String effect = "none";
        int mod = 0;

        if (trait instanceof WeaponTrait) {
            affectOpp = false;
            effect = "dmg";
            mod = dmg;
        } else if (trait instanceof SheildTrait) {
            affectOpp = false;
            effect = "dmg";
            mod = dmg;
        } else if (trait instanceof StrengthTrait) {
            affectOpp = false;
            effect = "reroll";
            mod = dmg;
        } else if (trait instanceof WeaknessTrait) {
            affectOpp = true;
            effect = "dmg";
            mod = ((WeaknessTrait) trait).getMod();
        } 
        
        // Cons
        else if (trait instanceof RobCon) {
            affectOpp = false;
            effect = "rob";
            mod = dmg;
        } else if (trait instanceof StealCon) {
            affectOpp = false;
            effect = "steal";
            mod = dmg;
        }

        // TODO: more Traits/cons

        return new Effect(this, affectOpp, reroll, effect, mod);
    }

    // increases the dmg for weapon types (uses card modifications/traits)
    // public void use(int Totdmg) {
    //     // System.out.println(String.format("\n\t+%d dmg - %s: %s [%d dmg]\n", dmg, name, trait.getDesc(), Totdmg));
    //     if (trait instanceof WeaponTrait) {
    //         dmg = dmg + ((WeaponTrait)trait).getMod();
    //     } // TODO: else if etc etc
    // }

    public String getName() {
        return name;
    }
    public Type getType() {
        return type;
    }

    public int getDmg() {
        if (trait instanceof StrengthTrait
         || trait instanceof SmokeArrowTrait
         || trait instanceof WeaknessTrait)
            dmg = ((Upgdable) trait).getMod();
        return dmg;
    }
    public void setDmg(int dmg) {
        this.dmg = dmg;
    }
    public void incDmg(int dmg) {
        this.dmg += dmg;
    }
    public void decDmg(int dmg) {
        this.dmg = (this.dmg - dmg < 1) ? 1 : (this.dmg - dmg);
    }

    public Trait getTrait() {
        return trait;
    }
    public String getTraitDesc() {
        return trait.getDesc();
    }
    public void setTrait(Trait trait) {
        this.trait = trait;
    }

    public boolean isReroll() {
        return reroll;
    }
    // public void setReroll(boolean reroll) {
    //     this.reroll = reroll;
    // }

    public float getProb() {
        return prob;
    }
    public void setProb(int prob) {
        this.prob = prob;
    }

    public int getCost() {
        return cost;
    }
    public void setCost(int cost) {
        this.cost = cost;
    }

    public Effect getEffect() {
        return effect;
    }
}