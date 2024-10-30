package orders;

import investors.Investor;
import stockmarket.Stock;

public class TermlessOrder extends Order{

    public TermlessOrder(Investor investor, Stock stock, int quantity, int priceLimit, OrderType type, int turnWhenPlaced) {
        super(investor, stock, quantity, priceLimit, type, turnWhenPlaced);
    }

    @Override
    public Order copy() {
        return new TermlessOrder(getInvestor(), getStock(), getQuantity(), getPriceLimit(), getType(), getTurn());
    }

    @Override
    public boolean shouldCancel(int turn) {
        return false;
    }

}
