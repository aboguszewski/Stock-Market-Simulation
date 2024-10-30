package orders;

import investors.Investor;
import stockmarket.Stock;

import java.util.ArrayList;

// Fulfil or Cancel Order - Zlecenia Wykonaj lub Anuluj (WA) [zaimplementowane poprawnie tylko dla zleceń kupna]
public class FCOrder extends Order {

    public FCOrder(Investor investor, Stock stock, int quantity, int priceLimit, OrderType type, int turnWhenPlaced) {
        super(investor, stock, quantity, priceLimit, type, turnWhenPlaced);
    }

    @Override
    public ArrayList<Order> fulfill(ArrayList<Order> sellOrders, int turn) {
        ArrayList<Order> usedToFulfill = super.fulfill(sellOrders, turn);

        int buyQuantity = getQuantity();
        for (int i = 0; i < usedToFulfill.size(); i++) {
            buyQuantity -= sellOrders.get(i).getQuantity();
        }

        if (buyQuantity <= 0) {
            return usedToFulfill;
        } else {
            decreaseQuantity(getQuantity());
            return new ArrayList<>();
        }
    }

    @Override
    public Order copy() {
        return new FCOrder(getInvestor(), getStock(), getQuantity(), getPriceLimit(), getType(), getTurn());
    }

    @Override
    public boolean shouldCancel(int turn) {
        return turn > getTurn();
    }
}