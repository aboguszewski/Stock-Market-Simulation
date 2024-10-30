package orders;

import investors.Investor;
import investors.Randomizer;
import stockmarket.Stock;

import java.util.ArrayList;

public abstract class Order {
    private final Investor investor;
    private final Stock stock;
    private int quantity;
    private final int priceLimit;
    private final OrderType type;
    private final int turnWhenPlaced;

    public static Order createRandomPriorityOrder(Investor investor, Stock stock, int quantity, int priceLimit, OrderType type, int turn) {
        OrderPriority orderPriority = Randomizer.randomOrderPriority();
        Order order = null;
        switch (orderPriority) {
            case INSTANT:
                order = new InstantOrder(investor, stock, quantity, priceLimit, type, turn);
                break;
            case TERMLESS:
                order = new TermlessOrder(investor, stock, quantity, priceLimit, type, turn);
                break;
            case FC:
                order = new FCOrder(investor, stock, quantity, priceLimit, type, turn);
                break;
            case TEMPORARY:
                order = new TemporaryOrder(investor, stock, quantity, priceLimit, type, turn, Randomizer.randomTurn(turn));
                break;
        }
        return order;
    }

    protected Order(Investor investor, Stock stock, int quantity, int priceLimit, OrderType type, int turnWhenPlaced) {
        this.investor = investor;
        this.stock = stock;
        this.quantity = quantity;
        this.priceLimit = priceLimit;
        this.type = type;
        this.turnWhenPlaced = turnWhenPlaced;
    }

    public ArrayList<Order> fulfill(ArrayList<Order> sellOrders, int turn) {
        ArrayList<Order> usedToFulfill = new ArrayList<>();

        int buyQuantity = getQuantity();
        for (Order sellOrder : sellOrders) {
            if (sellOrder.getPriceLimit() > getPriceLimit()) {
                return usedToFulfill;
            }

            if (sellOrder.getQuantity() >= buyQuantity) {
                sellOrder.decreaseQuantity(buyQuantity);
                usedToFulfill.add(sellOrder);
                return usedToFulfill;
            } else {
                buyQuantity -= sellOrder.getQuantity();
                sellOrder.decreaseQuantity(sellOrder.getQuantity());
                usedToFulfill.add(sellOrder);
            }
        }

        return usedToFulfill;
    }

    protected abstract boolean shouldCancel(int turn);

    protected abstract Order copy();

    protected boolean canFulfill(int price, int quantity) {
        if (type == OrderType.BUY) {
            return investor.getCash() >= quantity * price;
        } else {
            return investor.getPortfolioQuantity(stock) >= quantity;
        }
    }

    public Investor getInvestor() {
        return investor;
    }

    protected OrderType getType() {
        return type;
    }

    protected int getPriceLimit() {
        return priceLimit;
    }

    protected int getTurn() {
        return turnWhenPlaced;
    }

    protected int getQuantity() {
        return quantity;
    }

    protected void decreaseQuantity(int quantity) {
        this.quantity -= quantity;
    }

    protected void bought(int quantity, int price) {
        investor.bought(stock, quantity, price);
    }

    protected void sold(int quantity, int price) {
        investor.sold(stock, quantity, price);
    }

    public Stock getStock() {
        return stock;
    }

    @Override
    public String toString() {
        return  "investor=" + investor +
                ", stock=" + stock +
                ", quantity=" + quantity +
                ", priceLimit=" + priceLimit +
                ", type=" + type +
                ", turnWhenPlaced=" + turnWhenPlaced;
    }
}
