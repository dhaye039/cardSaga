package cardSaga;
import java.util.*;

public class Fight {
    MasterList masterList = MasterList.getInstance();

    private Player p;
    private Enemy e;
    private Card pCard, eCard;
    private Random rand = new Random();
    private boolean pImmune = false;
    private int bombLost = -3;
    private int turn;

    public Fight(Player player, Enemy enemy, int turn) {
        this.p = player;
        this.e = enemy;
        this.pCard = null; // only contains last player card
        this.eCard = null; // only contains last enemy card (change these later to contain all cards drawn?)
        this.turn = turn;
    }

    public void startFight() {
        int pTotDmg = 0;
        int eTotDmg = 0;
        boolean reroll = false;

        if (p.entityType.equals("ranger")) {
            System.out.println("turn: " + turn + " bombLost: " + bombLost);
            if (turn == bombLost + 2) {
                p.getCards().add(masterList.lookup("Bomb"));
            }
        }

        // for (var c : enemy.getCards()) {
        //     System.out.println(c.name + " " + c.dmg);
        // }

        while (true) {
            String pTurn = "";
            String eTurn = "";
            pImmune = false;

            // Player's turn
            System.out.println("Player's drawings:");
            do {
                pCard = spinWheel(p.getCards());
                // System.out.println("Player drew " + pCard.name);
                if (pCard.effect.affectOpp) {
                    eTotDmg += calcDmg(pCard);
                } else {
                    pTotDmg += calcDmg(pCard);
                }
                pTurn += String.format("\n\t+%d dmg - %s: %s", pCard.getDmg(), pCard.name, pCard.trait.getDesc());
                apply(pCard);
                reroll = pCard.reroll;
            } while (reroll);
            System.out.println(pTurn + "\n");

            System.out.println("-------------------------------------------------\n");

            // Enemy's turn
            System.out.println("Enemy's drawings:");
            do {
                eCard = spinWheel(e.getCards());
                // System.out.println("Enemy drew " + eCard.name);
                if (eCard.effect.affectOpp) {
                    pTotDmg += calcDmg(eCard);
                } else {
                    eTotDmg += calcDmg(eCard);
                }
                eTurn += String.format("\n\t+%d dmg - %s: %s", eCard.getDmg(), eCard.name, eCard.trait.getDesc());
                reroll = eCard.reroll;
            } while (reroll);
            System.out.println(eTurn + "\n");

            // print outcome
            System.out.println(String.format("\tFinal Totals:\n\tPlayer [%s] | Enemy [%s]", pTotDmg, eTotDmg));
            // Compare total damages
            if (pTotDmg > eTotDmg) {
                System.out.println("\tPlayer wins! Enemy dropped " + e.gold + " gold\n");
                p.inventory.addGold(e.gold);
                break;
            } else if (eTotDmg > pTotDmg) {
                System.out.println("\tEnemy wins.\n");
                if (!(eCard.getTrait() instanceof WeaponTrait) && pImmune != true)
                    apply(eCard);
                if (p.inventory.addGold(-3) < 0) p.inventory.setGold(0); // -3 gold for losing
                break;
            } else {
                System.out.println("\nIt's a tie! Restarting fight...\n");
                // Reset total damages for a tie
                pTotDmg = 0;
                eTotDmg = 0;
            }
        }
    }

    private Card spinWheel(List<Card> cards) {
        int inc = 0;
        boolean isCardFound = false;
        int totalProb = 0;
        // Card picked;

        for (var card : cards) {
            // System.out.println(card.name);
            totalProb += card.getProb();
        }
        // System.out.println(totalProb);
        
        int cardselect = rand.nextInt(totalProb) + 1;

        // System.out.println("Number selected: " + cardselect + " total prob: " + totalProb);

        for (var card : cards) {
            if ((inc += card.getProb()) >= cardselect && !isCardFound) { // card selection
                isCardFound = true;
                // System.out.println(card.name + " " + card.dmg);
                return card;
            }
        }
        return null; // TODO: This breaks sometimes?
    }

    private int calcDmg(Card card) {
        Effect e = card.getEffect();
        int dmgModifier = 0;

        Trait t = card.trait;

        if (t instanceof StrengthTrait) {
            dmgModifier = ((StrengthTrait)t).getMod();
        } else if (t instanceof WeaknessTrait) {
            dmgModifier = ((WeaknessTrait)t).getMod();
        } else {
            dmgModifier = card.getDmg();
        }

        // if (t instanceof WeaponTrait) {
        //     dmgModifier = card.dmg; 
        // } else if (t instanceof SheildTrait) {
        //     dmgModifier = card.dmg;
        // } else if (t instanceof StrengthTrait) {
        //     dmgModifier = ((StrengthTrait)t).getMod();
        // } else if (t instanceof WeaknessTrait) {
        //     dmgModifier = ((WeaknessTrait)t).getMod();
        // } else if (t instanceof MagicMirrorTrait) {
        //     dmgModifier = card.dmg;
        // } else if (t instanceof ConfusionTrait) {
        //     dmgModifier = card.dmg;
        // }
        
        // // Cons
        // else if (t instanceof KnifeCon) {
        //     dmgModifier = card.dmg; 
        // } else if (t instanceof RobCon) {
        //     dmgModifier = card.dmg; 
        // } else if (t instanceof StealCon) {
        //     dmgModifier = card.dmg; 
        // } 

        dmgModifier = (e.affectOpp) ? dmgModifier * -1 : dmgModifier;

    /* TODO: Apply card effects based on card type and character
    * For example, increase/decrease damage, reroll, etc.
    * Implement logic for applying card effects
    */ 

        return dmgModifier;
    }

    /**
     * This function will be very long :')... 
     * @param c is the Card being used
     */
    public void apply(Card c) {
        Trait t = c.getTrait();

        // Player Traits 
        if (t instanceof WeaponTrait) {
            c.incDmg(((WeaponTrait)t).getMod());
        } else if (t instanceof SheildTrait) {
            pImmune = true;
        } else if (t instanceof MagicMirrorTrait) {

        } else if (t instanceof ConfusionTrait) {
            // reroll(e);
        } else if (t instanceof BombTrait) {
            bombLost = turn;
            p.inventory.remove(pCard);
        } else if (t instanceof LuckTrait) {
            c.prob = c.prob * (float) 1.05;
        }
        
        // Consequences
        else if (t instanceof StealCon) {
            p.inventory.remove(pCard);
        } else if (t instanceof RobCon) {
            if (p.inventory.addGold(-3) < 0) p.inventory.setGold(0);
        }
    }
}
