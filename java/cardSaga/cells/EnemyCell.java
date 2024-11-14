package cardSaga.cells;

import cardSaga.Enemy;
import cardSaga.Fight;
import cardSaga.Player;

public class EnemyCell extends Cell {

    Enemy enemy;

    public EnemyCell(String enemyType) {
        super();
        this.val = 'e';
        this.enemy = new Enemy(enemyType, 3);
    }

    public Enemy getEnemy() {
        return enemy;
    }
}
