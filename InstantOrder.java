package orders;

import investors.Investor;
import stockmarket.Stock;

import java.util.ArrayList;

public class InstantOrder extends Order {
    public InstantOrder(Investor investor, Stock stock, int quantity, int priceLimit, OrderType type, int turnWhenPlaced) {
        super(investor, stock, quantity, priceLimit, type, turnWhenPlaced);
    }

    @Override
    public ArrayList<Order> fulfill(ArrayList<Order> sellOrders, int turn) {
        ArrayList<Order> usedToFulfill = super.fulfill(sellOrders, turn);

        decreaseQuantity(getQuantity());
        return usedToFulfill;
    }

    @Override
    public Order copy() {
        return new InstantOrder(getInvestor(), getStock(), getQuantity(), getPriceLimit(), getType(), getTurn());
    }

    @Override
    public boolean shouldCancel(int turn) {
        return turn > getTurn();
    }
}
