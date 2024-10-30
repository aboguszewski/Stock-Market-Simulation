package stockmarket;

import orders.Order;
import orders.StockOrders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class StockMarket {
    private final Map<Stock, StockOrders> exchangeSystem;
    private final Map<Stock, Integer> stockQuantities;
    private int currentTurn;

    public StockMarket(Map<Stock, Integer> initialStockPrices, Map<Stock, Integer> stockQuantities) {
        Set<Stock> stocks = initialStockPrices.keySet();
        Map<Stock, StockOrders> exchangeSystem = new HashMap<>();
        for (Stock stock : stocks) {
            exchangeSystem.put(stock, new StockOrders(initialStockPrices.get(stock)));
        }

        this.exchangeSystem = exchangeSystem;
        this.stockQuantities = stockQuantities;
        currentTurn = 0;
    }

    public ArrayList<Stock> getStocks() {
        Set<Stock> stockSet = exchangeSystem.keySet();
        return new ArrayList<>(stockSet);
    }

    public void place(Order order) {
        exchangeSystem.get(order.getStock()).place(order);
    }

    @Override
    public String toString() {
        return exchangeSystem.toString();
    }

    public int getStockQuantity(Stock stock) {
        return stockQuantities.get(stock);
    }

    public int getStockPrice(Stock stock) {
        return exchangeSystem.get(stock).getPrice();
    }

    public int getTurn() {
        return currentTurn;
    }

    public void newTurn(int turn) {
        for (Stock stock : exchangeSystem.keySet()) {
            exchangeSystem.get(stock).completeOrders(turn);
        }

        currentTurn = turn;
    }
}
