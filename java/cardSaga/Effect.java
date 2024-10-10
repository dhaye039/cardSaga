package cardSaga;

public class Effect {
    Card card;
    boolean affectOpp, reroll;
    String effect;
    int mod;

    public Effect(Card card, boolean affectOpp, boolean reroll, String effect, int mod) {
        this.card = card;
        this.affectOpp = affectOpp;
        this.reroll = reroll;
        this.effect = effect;
        this.mod = mod;
    }

    // // applies card effect to fight/card
    // public boolean apply() {
    //     // if (affectOpp) {

    //     // } else {
    //     Trait t = card.getTrait();
    //     if (t instanceof WeaponTrait) {
    //         card.incDmg(((WeaponTrait)t).getMod());
    //     } else if (t instanceof StealCon) {
            
    //     }

    //     return false;
    //     // }
    // }

    public Card getCard() {
        return card;
    }

    public boolean isAffectOpp() {
        return affectOpp;
    }

    public boolean isReroll() {
        return reroll;
    }
    
    public String getEffect() {
        return effect;
    }

    public int getMod() {
        return mod;
    }
    public void addMod(int mod) {
        this.mod += mod;
    }
}
