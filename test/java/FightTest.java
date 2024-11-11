import org.junit.jupiter.api.Test;

import cardSaga.MasterList;
import cardSaga.Player;
import cardSaga.Card;
import cardSaga.Enemy;
import cardSaga.Fight;

import static org.junit.jupiter.api.Assertions.*;

public class FightTest {

    @Test
    public void testRemoveCard() {

        Player p = new Player("wizard");
        Enemy e = new Enemy("goblin", 5);
        MasterList ml = MasterList.getInstance();
        
        Card magic_mirror = ml.lookup("Magic Mirror");

        Card steal1 = ml.lookup("Steal");
        Card steal2 = ml.lookup("Steal");

        steal1.setBorrowed(true, 1);
        steal2.setBorrowed(true, 1);

        p.getInventory().remove(magic_mirror);
        p.getInventory().addCard(steal1);
        p.getInventory().addCard(steal2);

        new Fight(p, e, 1).startFight();

        p.viewInventory();
        
        // assertFalse(p.getCards().contains(magic_mirror));
    }
}
