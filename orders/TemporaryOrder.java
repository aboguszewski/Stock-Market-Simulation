package orders;

import investors.Investor;
import stockmarket.Stock;

import java.util.ArrayList;

public class TemporaryOrder extends Order {
    private final int terminationTurn;

    public TemporaryOrder(Investor investor, Stock stock, int quantity, int priceLimit, OrderType type, int turnWhenPlaced, int terminationTurn) {
        super(investor, stock, quantity, priceLimit, type, turnWhenPlaced);
        this.terminationTurn = terminationTurn;
    }

    @Override
    public ArrayList<Order> fulfill(ArrayList<Order> sellOrders, int turn) {
        if (turn > terminationTurn) {
            decreaseQuantity(getQuantity());
            return new ArrayList<>();
        } else {
            return super.fulfill(sellOrders, turn);
        }
    }

    @Override
    public String toString() {
        return super.toString() + ", terminationTurn=" + terminationTurn;
    }

    @Override
    public Order copy() {
        return new TemporaryOrder(getInvestor(), getStock(), getQuantity(), getPriceLimit(), getType(), getTurn(), terminationTurn);
    }

    @Override
    public boolean shouldCancel(int turn) {
        return turn > terminationTurn;
    }
}
