package investors;

import orders.OrderPriority;
import orders.OrderType;
import stockmarket.Stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Randomizer {
    private static final Random generator = new Random();
    private static final int MAX_PRICE_DEVIATION = 10;

    public static OrderType randomOrderType() {
        return generator.nextBoolean() ? OrderType.BUY : OrderType.SELL;
    }

    public static Stock randomStock(ArrayList<Stock> stocks) {
        return stocks.get(generator.nextInt(stocks.size()));
    }

    public static OrderPriority randomOrderPriority() {
        ArrayList<OrderPriority> orderPriorities = new ArrayList<>(
                List.of(OrderPriority.INSTANT, OrderPriority.TERMLESS,
                        OrderPriority.FC, OrderPriority.TEMPORARY));

        return orderPriorities.get(generator.nextInt(orderPriorities.size()));
    }

    public static int randomPrice(int price) {
        return generator.nextInt(Math.max(price - MAX_PRICE_DEVIATION, 1), price + MAX_PRICE_DEVIATION + 1);
    }

    public static int randomQuantity(int maxQuantity) {
        return generator.nextInt(1, maxQuantity + 1);
    }

    public static int randomTurn(int currentTurn) {
        return generator.nextInt(currentTurn + 1, currentTurn + 20);
    }
}
