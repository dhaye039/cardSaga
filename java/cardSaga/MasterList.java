package cardSaga;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import cardSaga.traits.*;
import cardSaga.traits.weapons.*;
import cardSaga.traits.utilities.*;
import cardSaga.traits.potions.*;
import cardSaga.traits.accessories.*;
// import cardSaga.traits.armor.*;
import cardSaga.traits.consequences.*;


public class MasterList {
    private static MasterList instance = new MasterList();
    private List<Card> masterList = new ArrayList<Card>();
    private List<Card> shop = new ArrayList<>();
        private Random rand = new Random();
        
        private MasterList() {
            initMasterList();
            int cardToAdd;
    
            for (int i = 0; i < 3; ++i) {
                cardToAdd = rand.nextInt(masterList.size());
                shop.add(masterList.get(cardToAdd));
            }
        }
    
        public void initMasterList() {
            // Knight Starting Cards
            masterList.add(new Card("Sword", Type.WEAPON, 5, (Trait) new WeaponTrait(1, 2), false, 40, 5, false));
            masterList.add(new Card("Sheild", Type.ACCESSORY, 4, (Trait) new SheildTrait(), false, 40, 7, false));
            masterList.add(new Card("Strength Potion", Type.POTION, 3, (Trait) new StrengthTrait(3, 3), true, 20, 7, false));
    
            // Wizard Starting Cards
            masterList.add(new Card("Fireball", Type.WEAPON, 6, (Trait) new WeaponTrait(1, 2), false, 27, 5, false));
            masterList.add(new Card("Magic Mirror", Type.UTILITY, 6, (Trait) new MagicMirrorTrait(1, 2), false, 27, 5, false));
            // masterList.add(new Card("Confusion Potion", Type.POTION, 5, (Trait) new ConfusionTrait(), false, 27, 5, false));
            masterList.add(new Card("Weakness Potion", Type.POTION, 0, (Trait) new WeaknessTrait(1, 2), true, 19, 5, true));
    
            // Archer Starting Cards
            masterList.add(new Card("Bow", Type.WEAPON, 3, (Trait) new WeaponTrait(1, 2), false, 27, 5, false));
            masterList.add(new Card("Bomb", Type.UTILITY, 8, (Trait) new BombTrait(2, 2, 0), false, 27, 7, false));
            masterList.add(new Card("Smoke Arrow", Type.UTILITY, 2, (Trait) new SmokeArrowTrait(2, 2), true, 27, 7, false));
            masterList.add(new Card("Luck Potion", Type.POTION, 0, (Trait) new LuckTrait(5, 2), true, 19, 5, false));
    
            // Rogue Starting Cards
            masterList.add(new Card("Dagger", Type.WEAPON, 4, (Trait) new WeaponTrait(1, 2), false, 40, 5, false));
            masterList.add(new Card("Cloak", Type.ACCESSORY, 4, (Trait) new CloakTrait(), false, 40, 7, false));
            masterList.add(new Card("Crit Potion", Type.POTION, 0, (Trait) new CritTrait(), true, 20, 5, false));
    
            // Enemy Cards
            // Goblin
            masterList.add(new Card("Knife", Type.ENEMY, 6, (Trait) new EnemyWeaponTrait(), false, 40, 5, false));
            masterList.add(new Card("Rob", Type.ENEMY, 8, (Trait) new RobCon(3, 2), false, 40, 5, false)); // RobCon(1, 2)* probably but check this later
            masterList.add(new Card("Steal", Type.ENEMY, 5, (Trait) new StealCon(), false, 20, 5, false));
    
    
            // Other Cards
            // masterList.add(new card("Grapple", Type.UTILITY, 4, new GrappleTrait(1, 2), false, 27, 7));
        }
    
        public void populateShop() {
            int cardsToAdd = Math.max(3, shop.size());
    
            shop.clear();
    
            while (shop.size() < cardsToAdd) {
                int cardToAdd = rand.nextInt(masterList.size());
                Card c = masterList.get(cardToAdd);
                if (!c.type.equals(Type.WEAPON) && !c.type.equals(Type.ENEMY)) {
                    shop.add(c);
                }
            }
        }
    
        public void extendShop() {
            int cardToAdd = rand.nextInt(masterList.size());
    
            shop.add(masterList.get(cardToAdd));
        }
    
        public List<Card> getShop() {
            return shop;
    }

    public Card lookup(String name) {
        for (var card : masterList) {
            if (card.name.equals(name)) {
                Trait cardTrait = createTraitInstance(card.trait);
                return new Card(card.name, card.type, card.dmg, cardTrait, card.reroll, card.prob, card.cost, card.affectOpp);
            }
        }
        return null;
    }

    private Trait createTraitInstance(Trait trait) {
        if (trait instanceof WeaponTrait) {
            WeaponTrait origTrait = (WeaponTrait) trait;
            return new WeaponTrait(origTrait.getMod(), origTrait.getUpgdCap());
        } else if (trait instanceof MagicMirrorTrait) {
            MagicMirrorTrait origTrait = (MagicMirrorTrait) trait;
            return new MagicMirrorTrait(origTrait.getMod(), origTrait.getUpgdCap());
        } else if (trait instanceof BombTrait) {
            BombTrait origTrait = (BombTrait) trait;
            return new BombTrait(origTrait.getMod(), origTrait.getUpgdCap(), origTrait.getTurn());
        } else if (trait instanceof SmokeArrowTrait) {
            SmokeArrowTrait origTrait = (SmokeArrowTrait) trait;
            return new SmokeArrowTrait(origTrait.getMod(), origTrait.getUpgdCap());
        } else if (trait instanceof GrappleTrait) {
            GrappleTrait origTrait = (GrappleTrait) trait;
            return new GrappleTrait(origTrait.getMod(), origTrait.getUpgdCap());
        } else if (trait instanceof StrengthTrait) {
            StrengthTrait origTrait = (StrengthTrait) trait;
            return new StrengthTrait(origTrait.getMod(), origTrait.getUpgdCap());
        } else if (trait instanceof ConfusionTrait) {
            return new ConfusionTrait();
        } else if (trait instanceof WeaknessTrait) {
            WeaknessTrait origTrait = (WeaknessTrait) trait;
            return new WeaknessTrait(origTrait.getMod(), origTrait.getUpgdCap());
        } else if (trait instanceof LuckTrait) {
            LuckTrait origTrait = (LuckTrait) trait;
            return new LuckTrait(origTrait.getMod(), origTrait.getUpgdCap());
        } else if (trait instanceof CritTrait) {
            return new CritTrait();
        } else if (trait instanceof SheildTrait) {
            return new SheildTrait();
        } else if (trait instanceof CloakTrait) {
            return new CloakTrait();
        } 
        
        // Enemy Traits
        else if (trait instanceof EnemyWeaponTrait) {
            return new EnemyWeaponTrait();
        } else if (trait instanceof StealCon) {
            return new StealCon();
        } else if (trait instanceof RobCon) {
            RobCon origTrait = (RobCon) trait;
            return new RobCon(origTrait.getMod(), origTrait.getUpgdCap());
        } 
        
        // Catch if card trait has not been added
        else {
            System.err.println("No known trait");
            return null;
        } 
    }

    public static synchronized MasterList getInstance() {
        return instance;
    }
}
