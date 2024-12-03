package cardSaga.cells;

import cardSaga.Enemy;

public class BossCell extends Cell {

    Enemy enemy;

    public BossCell(String enemyType) {
        super();
        this.val = 'b';
        this.enemy = new Enemy(enemyType, 20, true);
    }

    public Enemy getEnemy() {
        return enemy;
    }
}
