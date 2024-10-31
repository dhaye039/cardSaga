package cardSaga;
import java.util.*;

public class Fight {
    MasterList masterList = MasterList.getInstance();

    private Player p;
    private Enemy e;
    private Card pCard, eCard;
    private Random rand = new Random();
    private boolean pImmune = false;
    private boolean pUsedCrit, eUsedCrit, useDodge, pSteal, eSteal;
    private int bombLost = -3;
    private int turn, numUses;

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
        pUsedCrit = false;
        eUsedCrit = false;
        useDodge = false;

        if (p.entityType.equals("ranger")) {
            System.out.println("turn: " + turn + " bombLost: " + bombLost);
            if (turn == bombLost + 2) {
                p.getCards().add(masterList.lookup("Bomb"));
            }
        }

        while (true) {
            String pTurn = "";
            String eTurn = "";
            pImmune = false;
            pSteal = false;
            eSteal = false;

            // Player's turn
            System.out.println("Player's drawings:");
            do {
                do { 
                    pCard = spinWheel(p.getCards());
                } while (
                    pCard.name.equals("Crit Potion") && pUsedCrit == true
                );

                if (pCard.name.equals("Crit Potion")) { pUsedCrit = true; }
                
                if (pCard.isBorrowed) {
                    --pCard.numUses;
                    if (pCard.numUses == 0) {
                        p.inventory.remove(pCard);
                    }
                }

                if (pCard.affectOpp) {
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
                do { 
                    eCard = spinWheel(e.getCards());
                } while (
                    eCard.name.equals("Crit Potion") && eUsedCrit == true
                );

                if (eCard.name.equals("Crit Potion")) { eUsedCrit = true; }

                if (eCard.affectOpp) {
                    pTotDmg = calcDmg(eCard, pTotDmg);
                } else {
                    eTotDmg = calcDmg(eCard, eTotDmg);
                }
                eTurn += String.format("\n\t+%d dmg - %s: %s", eCard.getDmg(), eCard.name, eCard.trait.getDesc());
                reroll = eCard.reroll;
            } while (reroll);
            System.out.println(eTurn + "\n");

            // Calculate winner
            if (pUsedCrit) pTotDmg *= 2;
            else if (eUsedCrit) eTotDmg *= 2;

            // Print outcome
            System.out.println(String.format("\tFinal Totals:\n\tPlayer [%s] | Enemy [%s]", pTotDmg, eTotDmg));

            // Compare total damages
            if (pTotDmg > eTotDmg) { // player wins
                System.out.println("\tPlayer wins! Enemy dropped " + e.gold + " gold\n");

                if (pSteal) {
                    Card c = spinWheel(e.getCards());
                    c.setBorrowed(true, numUses);
                    System.out.println(c.numUses);
                    p.getCards().add(c);
                }

                if (rand.nextInt(100) == 50) {
                    p.inventory.incnumUpgdCards();
                    System.out.println("Enemy dropped an upgrade card!");
                }

                p.inventory.addGold(e.gold);
                break;
            } else if (eTotDmg > pTotDmg) { // enemy wins
                System.out.println("\tEnemy wins.\n");

                if (useDodge) {
                    pImmune = isDodging();
                    useDodge = false;
                    System.out.print("\n");
                }

                if (!(eCard.getTrait() instanceof WeaponTrait) && pImmune != true) {
                    apply(eCard);
                    if (p.inventory.addGold(-3) < 0) p.inventory.setGold(0); // -3 gold for losing
                }
                break;
            } else { // tie
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

        return newDmg = (card.affectOpp) ? newDmg * -1 : newDmg;
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
            pSteal = true;
            numUses = ((MagicMirrorTrait)t).mod;
        } else if (t instanceof ConfusionTrait) {
            // reroll(e);
        } else if (t instanceof BombTrait) {
            bombLost = turn;
            p.inventory.remove(pCard);
        } else if (t instanceof LuckTrait) {
            c.prob = c.prob * (float) 1.05;
        } else if (t instanceof CritTrait) {
            p.inventory.remove(c); // gets added back next turn
        } else if (t instanceof CloakTrait) {
            useDodge = true;
        }
        
        // Consequences
        else if (t instanceof StealCon) {
            p.inventory.remove(pCard);
        } else if (t instanceof RobCon) {
            if (p.inventory.addGold(-3) < 0) p.inventory.setGold(0);
        }
    }

    @SuppressWarnings("resource")
    private boolean isDodging() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Would you like to dodge this fight? (y/n): ");
        if (scanner.nextLine().startsWith("y")) 
            return true;
        else 
            return false;
    }
}
