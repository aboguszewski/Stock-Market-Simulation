package orders;

import java.util.ArrayList;

public class StockOrders {
    private final static int MAX_ORDER_QUEUE_SIZE = 300;
    private final ArrayList<Order> buyOrders;
    private final ArrayList<Order> sellOrders;
    private int lastPrice;

    public StockOrders(int initialPrice) {
        buyOrders = new ArrayList<>();
        sellOrders = new ArrayList<>();
        lastPrice = initialPrice;
    }

    public void completeOrders(int turn) {
        for (int i = 0; i < sellOrders.size(); i++) {
            if (sellOrders.get(i).shouldCancel(turn)) {
                sellOrders.remove(sellOrders.get(i));
            }
        }

        buyOrders.sort(OrderComparator.getInstance());
        sellOrders.sort(OrderComparator.getInstance());

        if (buyOrders.size() > MAX_ORDER_QUEUE_SIZE) {
            buyOrders.subList(MAX_ORDER_QUEUE_SIZE, buyOrders.size()).clear();
        }

        if (sellOrders.size() > MAX_ORDER_QUEUE_SIZE) {
            sellOrders.subList(MAX_ORDER_QUEUE_SIZE, sellOrders.size()).clear();
        }

        for (int i = 0; i < buyOrders.size(); i++) {
            Order buyOrder = buyOrders.get(i);

            ArrayList<Order> sellOrdersCopy = new ArrayList<>();
            for (Order sellOrder : sellOrders) {
                sellOrdersCopy.add(sellOrder.copy());
            }

            ArrayList<Order> usedToFulfill = buyOrder.fulfill(sellOrdersCopy, turn);
            if (buyOrder.getQuantity() == 0) {
                buyOrders.remove(buyOrder);
            } else {
                for (int j = 0; j < usedToFulfill.size(); j++) {
                    Order sellOrder = sellOrders.getFirst();
                    Order sellOrderCopy = usedToFulfill.getFirst();

                    int quantity = sellOrder.getQuantity() - sellOrderCopy.getQuantity();

                    int price;
                    if (buyOrder.getTurn() < sellOrder.getTurn()) {
                        price = buyOrder.getPriceLimit();
                    } else if (buyOrder.getTurn() > sellOrder.getTurn()) {
                        price = sellOrder.getPriceLimit();
                    } else {
                        price = Math.min(buyOrder.getPriceLimit(), sellOrder.getPriceLimit());
                    }
                    lastPrice = price;

                    if (!buyOrder.canFulfill(price, quantity) || !sellOrder.canFulfill(price, quantity)) {
                        continue;
                    }

                    buyOrder.bought(quantity, price);
                    sellOrder.sold(quantity, price);

                    buyOrder.decreaseQuantity(quantity);
                    sellOrder.decreaseQuantity(quantity);

                    if (sellOrder.getQuantity() == 0) {
                        sellOrders.remove(sellOrder);
                    }
                    if (buyOrder.getQuantity() == 0) {
                        buyOrders.remove(buyOrder);
                        break;
                    }
                }
            }
        }
    }

    public int getPrice() {
        return lastPrice;
    }

    public void place(Order order) {
        if (order.getType() == OrderType.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
    }

    @Override
    public String toString() {
        return buyOrders.size() + " " + sellOrders.size() + " " + lastPrice;
    }
}
