package cardSaga;
import java.util.*;

public class Fight {
    MasterList masterList = MasterList.getInstance();

    private Player p;
    private Enemy e;
    private Card pCard, eCard;
    private Random rand = new Random();
    private boolean pImmune = false;
    private boolean pHasCrit, eHasCrit, pUsedCrit, eUsedCrit;
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
        pHasCrit = false;
        eHasCrit = false;
        pUsedCrit = false;
        eUsedCrit = false;

        for (Card c : p.getCards()) {
            if (c.name.equals("Crit Potion")) {
                pHasCrit = true;
            }
        }

        for (Card c : e.getCards()) {
            
        }

        if (p.entityType.equals("ranger")) {
            System.out.println("turn: " + turn + " bombLost: " + bombLost);
            if (turn == bombLost + 2) {
                p.getCards().add(masterList.lookup("Bomb"));
            }
        }

        // for (Card card : cards) {
        //     if (card.name.equals("Crit Potion")) {
        //         hasCrit = true;
        //     }
        // }

        while (true) {
            String pTurn = "";
            String eTurn = "";
            pImmune = false;

            // Player's turn
            System.out.println("Player's drawings:");
            do {
                pCard = spinWheel(p.getCards());
                if (pCard.effect.affectOpp) {
                    eTotDmg = calcDmg(pCard, eTotDmg);
                } else {
                    pTotDmg = calcDmg(pCard, pTotDmg);
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
                if (eCard.effect.affectOpp) {
                    pTotDmg = calcDmg(eCard, pTotDmg);
                } else {
                    eTotDmg = calcDmg(eCard, eTotDmg);
                }
                eTurn += String.format("\n\t+%d dmg - %s: %s", eCard.getDmg(), eCard.name, eCard.trait.getDesc());
                reroll = eCard.reroll;
            } while (reroll);
            System.out.println(eTurn + "\n");

            if (pUsedCrit) pTotDmg *= 2;
            else if (eUsedCrit) eTotDmg *= 2;

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
                pUsedCrit = false;
                eUsedCrit = false;
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

    private int calcDmg(Card card, int TotDmg) {
        Effect e = card.getEffect();
        int newDmg = 0;

        Trait t = card.trait;

        if (t instanceof StrengthTrait) {
            newDmg = TotDmg + ((StrengthTrait)t).getMod();
        } else if (t instanceof WeaknessTrait) {
            newDmg = TotDmg + ((WeaknessTrait)t).getMod();
        } else if (t instanceof CritTrait) {
            newDmg = TotDmg * 2;
        }
        
        else {
            newDmg = TotDmg + card.getDmg();
        }

        /* TODO: Apply card effects based on card type and character
        * For example, increase/decrease damage, reroll, etc.
        * Implement logic for applying card effects
        */ 

        return newDmg = (e.affectOpp) ? newDmg * -1 : newDmg;
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
        } else if (t instanceof CritTrait) {
            p.inventory.remove(c); // gets added back next turn
        }
        
        // Consequences
        else if (t instanceof StealCon) {
            p.inventory.remove(pCard);
        } else if (t instanceof RobCon) {
            if (p.inventory.addGold(-3) < 0) p.inventory.setGold(0);
        }
    }
}
