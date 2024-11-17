package cardSaga;

import java.util.*;
import cardSaga.traits.*;
import cardSaga.traits.weapons.*;
import cardSaga.traits.utilities.*;
import cardSaga.traits.potions.*;
import cardSaga.traits.accessories.*;
// import cardSaga.traits.armor.*;
import cardSaga.traits.consequences.*;

class FightTrait {
    public boolean pImmune, pUsedCrit, eUsedCrit, useDodge, pMirrorCard, eSteal;

    public FightTrait() {
        this.pImmune = false;
    }

    void reset(boolean... flags) {
        for (int i = 0; i < flags.length; i++) {
            flags[i] = false;
        }
    }
}

public class Fight {
    MasterList masterList = MasterList.getInstance();
    FightTrait flags = new FightTrait();

    private Player p;
    private Enemy e;
    private Card pCard, eCard;
    private Random rand = new Random();
    // private boolean pImmune = false;
    // private boolean pUsedCrit, eUsedCrit, useDodge, pMirrorCard, eSteal;
    private int turn, numUses;
    private HashMap<Integer, Card> returnMap;

    public Fight(Player player, Enemy enemy, int turn) {
        this.p = player;
        this.e = enemy;
        this.pCard = null; // only contains last player card
        this.eCard = null; // only contains last enemy card (change these later to contain all cards drawn?)
        this.turn = turn;
        returnMap = p.inventory.returnMap;
    }

    public boolean startFight() {
        int pTotDmg = 0;
        int eTotDmg = 0;
        boolean reroll = false, result = false;
        flags.reset(flags.pUsedCrit, flags.eUsedCrit, flags.useDodge);

        // Check if any card is scheduled to return this turn
        if (returnMap.containsKey(turn)) {
            Card cardToReturn = returnMap.get(turn);
            p.getCards().add(cardToReturn); // Return the card to the inventory
            returnMap.remove(turn); // Remove it from the map since it’s restored
        }

        while (true) {
            String pTurn = "";
            String eTurn = "";
            flags.reset(flags.pImmune, flags.pMirrorCard, flags.eSteal);

            // Player's turn
            System.out.println("\nPlayer's drawings:"); waiting(1000);

            do {
                do { 
                    pCard = spinWheel(p.getCards());
                } while (pCard.name.equals("Crit Potion") && flags.pUsedCrit == true);

                if (pCard.name.equals("Crit Potion")) { flags.pUsedCrit = true; }

                if (pCard.affectOpp) {
                    eTotDmg = calcDmg(pCard, eTotDmg);
                } else {
                    pTotDmg = calcDmg(pCard, pTotDmg);
                }
                pTurn += String.format("\n\t+%d dmg - %s: %s", pCard.getDmg(), pCard.name, pCard.trait.getDesc());
                
                Trait check = pCard.getTrait();
                if (!(check instanceof EnemyWeaponTrait || check instanceof RobCon || check instanceof StealCon)) 
                    apply(pCard, e);

                reroll = pCard.reroll;
            } while (reroll);
            System.out.println(pTurn + "\n"); 

            System.out.println("-------------------------------------------------\n"); waiting(500);

            // Enemy's turn
            System.out.println("Enemy's drawings:"); waiting(1000);

            do {
                do { 
                    eCard = spinWheel(e.getCards());
                } while (
                    eCard.name.equals("Crit Potion") && flags.eUsedCrit == true
                );

                if (eCard.name.equals("Crit Potion")) { flags.eUsedCrit = true; }

                if (eCard.affectOpp) {
                    pTotDmg = calcDmg(eCard, pTotDmg);
                } else {
                    eTotDmg = calcDmg(eCard, eTotDmg);
                }
                eTurn += String.format("\n\t+%d dmg - %s: %s", eCard.getDmg(), eCard.name, eCard.trait.getDesc());
                reroll = eCard.reroll;
            } while (reroll);
            System.out.println(eTurn + "\n");

            System.out.println("-------------------------------------------------\n"); waiting(500);

            // Calculate winner
            if (flags.pUsedCrit) pTotDmg *= 2;
            else if (flags.eUsedCrit) eTotDmg *= 2;

            // Print outcome
            System.out.println(String.format("Final Totals:\n\n\tPlayer [%s] | Enemy [%s]\n", pTotDmg, eTotDmg));

            // Compare total damages
            if (pTotDmg > eTotDmg) { // player wins
                System.out.println("\tYou defeated the enemy and moved into their space!\n\tEnemy dropped " + e.gold + " gold.\n");

                if (pCard.getTrait() instanceof EnemyWeaponTrait) 
                    apply(pCard, e);

                // Mirror card and add to inventory
                if (flags.pMirrorCard) {
                    // Card c = spinWheel(e.getCards());
                    // c.setBorrowed(true, numUses);
                    // System.out.println(c.numUses);
                    // p.getCards().add(c);

                    Card mirrorCard = eCard;
                    mirrorCard.setBorrowed(true, numUses);
                    p.getCards().add(mirrorCard);
                }

                if (rand.nextInt(100) <= 20) {
                    p.inventory.incnumUpgdCards();
                    System.out.println("Enemy dropped an upgrade card!");
                }

                p.incXP(1);
                p.inventory.addGold(e.gold);
                result = true;
            } else if (eTotDmg > pTotDmg) { // enemy wins
                System.out.println("\tYou lost the fight and stay in your current position.\n");

                if (flags.useDodge) {
                    flags.pImmune = isDodging();
                    flags.useDodge = false;
                    System.out.print("\n");
                }

                if (!(eCard.getTrait() instanceof WeaponTrait) && flags.pImmune != true) {
                    apply(eCard, p);
                    if (p.inventory.addGold(-3) < 0) p.inventory.setGold(0); // -3 gold for losing
                }
                result = false;
            } else { // tie
                System.out.println("\nIt's a tie! Restarting fight...\n");
                System.out.println("_________________________________________________\n");
                // Reset total damages for a tie
                pTotDmg = 0;
                eTotDmg = 0;
                flags.pUsedCrit = false;
                flags.eUsedCrit = false;
            }

            p.hp = Math.min(100, p.hp + pTotDmg - eTotDmg);

            if (pCard.isBorrowed) { // TODO: add borrow-ability to card
                --pCard.numUses;
                if (pCard.numUses <= 0)
                    p.inventory.remove(pCard);
            }

            if (pTotDmg == eTotDmg) continue; else {
                waiting(1000);
                break;
            }
        }

        return result;
    }

    private Card spinWheel(List<Card> cards) {
        float inc = 0;
        boolean isCardFound = false;
        float totalProb = 0;

        for (var card : cards) {
            totalProb += card.getProb();
        }
        
        int cardselect = rand.nextInt((int)totalProb) + 1;

        for (var card : cards) {
            if ((inc += card.getProb()) >= cardselect && !isCardFound) { // card selection
                isCardFound = true;
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
            newDmg = TotDmg - ((WeaknessTrait)t).getMod();
        } else if (t instanceof CritTrait) {
            newDmg = TotDmg * 2;
        }
        
        else {
            newDmg = TotDmg + card.getDmg();
        }
    
        return newDmg;
    }

    /**
     * applies card effect
     * @param c card
     * @param entity entity card is being applied to
     */
    public void apply(Card c, Entity entity) {
        Trait t = c.getTrait();

        // Player Traits 
        if (t instanceof WeaponTrait) {
            c.incDmg(((WeaponTrait)t).getMod());
        } else if (t instanceof SheildTrait) {
            flags.pImmune = true;
        } else if (t instanceof MagicMirrorTrait) {
            flags.pMirrorCard = true;
            numUses = ((MagicMirrorTrait)t).mod;
        } else if (t instanceof ConfusionTrait) {
            // reroll(e);
        } else if (t instanceof BombTrait) {
            int returnTurn = turn + 3;
            returnMap.put(returnTurn, pCard); // Schedule card to return after 2 turns
            p.inventory.remove(pCard); 
        } else if (t instanceof LuckTrait) {
            int tempMax = 0, max = 0;
            Card maxDmgCard = null;
            for (var card : p.getCards()) {
                tempMax = card.dmg;
                if (tempMax > max) {
                    max = tempMax;
                    maxDmgCard = card;
                }
            }
            maxDmgCard.prob = maxDmgCard.prob * (float) 1.05;
        } else if (t instanceof CritTrait) {
            p.inventory.remove(c); // gets added back next turn
        } else if (t instanceof CloakTrait) {
            flags.useDodge = true;
        }
        
        // Consequences
        if (entity instanceof Enemy) {
            
            if (t instanceof StealCon) {
                p.inventory.addCard(eCard);
                // e.remove(eCard); // TODO: fix (in Enemy.java)
            } else if (t instanceof RobCon) {
                p.inventory.addGold(3); // remove gold from enemy? or nahh
            }

        } else if (entity instanceof Player) {

            if (t instanceof StealCon) {
                p.inventory.remove(pCard); // add to enemy inventory?
            } else if (t instanceof RobCon) {
                if (p.inventory.addGold(-3) < 0) p.inventory.setGold(0);
            }
        }
    }

    @SuppressWarnings("resource")
    private boolean isDodging() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Would you like to dodge this fight? (y/n): ");
        String response = scanner.nextLine();

        while (!response.startsWith("y") && !response.startsWith("n")) {
            System.out.print("Would you like to dodge this fight? (Please enter 'y' or 'n'): ");
            response = scanner.nextLine();

            if (response.startsWith("y")) 
                return true;
            else if (response.startsWith("n"))
                return false;
        }
        return false;
    }

    public void waiting(int millis) {
        try {
            Thread.sleep(millis); // Pauses for 1 second (1000 milliseconds)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
