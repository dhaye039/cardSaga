package cardSaga;

import java.util.*;

public class Entity {

    String entityType;

    MasterList masterList = MasterList.getInstance();
    Random rand = new Random();

    public Entity(String entityType) {
        this.entityType = entityType;
    }
}