package cardSaga;

public class Card {

    String name;        // Name of card
    Type type;          // Type of card
    int dmg;            // Damage the card adds to attack
    Trait trait;        // The trait refers to what the card actuallt does
    boolean reroll;     // If the card causes the owner to reroll
    float prob;         // The probability this card gets drawn
    int cost;           // How much gold the card costs (for shop)
    boolean affectOpp;  // If the card affects the opposing player
    boolean isBorrowed; // If the card is currently being borrowed
    int numUses;        // how many times the card can be used


    public Card(String name, Type type, int dmg, Trait trait, boolean reroll, float prob, int cost, boolean affectOpp) {
        this.name = name;
        this.type = type;
        this.dmg = dmg;
        this.trait = trait;
        this.reroll = reroll;
        this.prob = prob;
        this.cost = cost;
        this.affectOpp = affectOpp;
        this.isBorrowed = false;
        this.numUses = 0;
    }

    public String getName() {
        return name;
    }
    public Type getType() {
        return type;
    }

    // TODO: Damage incrementer/decrementer Traits
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

    public boolean isAffectOpp() {
        return affectOpp;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean isBorrowed, int numUses) {
        this.isBorrowed = isBorrowed;
        this.numUses = numUses;
    }
}