package cardSaga.cells;

import java.util.ArrayList;
import java.util.List;

import cardSaga.Card;
import cardSaga.MasterList;

public class ShopCell extends Cell {

    private List<Card> shop = new ArrayList<>();
    MasterList masterList = MasterList.getInstance();


    public ShopCell() {
        super();
        this.val = 's';
        this.shop = masterList.getShop();
    }

    public List<Card> getShop() {
        return this.shop;
    }
}
