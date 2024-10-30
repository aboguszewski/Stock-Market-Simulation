package investors;

import orders.*;
import stockmarket.Stock;
import stockmarket.StockMarket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class InvestorSMA extends Investor {
    private Map<Stock, ArrayList<Integer>> last5Prices;
    private Map<Stock, ArrayList<Integer>> last10Prices;
    private Map<Stock, Integer> prevSMA5;
    private Map<Stock, Integer> prevSMA10;

    public InvestorSMA(int cash, Map<Stock, Integer> portfolio) {
        super(cash, portfolio);
        last5Prices = new HashMap<>();
        last10Prices = new HashMap<>();
        prevSMA5 = new HashMap<>();
        prevSMA10 = new HashMap<>();
    }

    @Override
    public void makeOrder(StockMarket stockMarket) {
        int turn = stockMarket.getTurn();

        if (turn == 0) {
            for (Stock stock : stockMarket.getStocks()) {
                last5Prices.put(stock, new ArrayList<>());
                last10Prices.put(stock, new ArrayList<>());
                prevSMA5.put(stock, 0);
                prevSMA10.put(stock, 0);
            }
        }

        for (Stock stock : stockMarket.getStocks()) {
            if (last5Prices.get(stock).size() < 5) {
                last5Prices.get(stock).add(stockMarket.getStockPrice(stock));
            } else {
                prevSMA5.put(stock, average(last5Prices.get(stock)));
                last5Prices.get(stock).removeFirst();
                last5Prices.get(stock).add(stockMarket.getStockPrice(stock));
            }

            if (last10Prices.get(stock).size() < 10) {
                last10Prices.get(stock).add(stockMarket.getStockPrice(stock));
            } else {
                prevSMA10.put(stock, average(last10Prices.get(stock)));
                last10Prices.get(stock).removeFirst();
                last10Prices.get(stock).add(stockMarket.getStockPrice(stock));
            }
        }

        if (turn >= 10) {
            ArrayList<Stock> stocks = stockMarket.getStocks();
            Order order = null;

            Collections.shuffle(stocks);
            for (Stock stock : stocks) {
                int sma5 = average(last5Prices.get(stock));
                int sma10 = average(last10Prices.get(stock));

                if (sma5 > sma10 && prevSMA5.get(stock) < prevSMA10.get(stock)) {
                    order = makeBuyOrder(stock, stockMarket, turn);
                } else if (sma5 < sma10 && prevSMA5.get(stock) > prevSMA10.get(stock)) {
                    order = makeSellOrder(stock, stockMarket, turn);
                }

                prevSMA5.put(stock, sma5);
                prevSMA10.put(stock, sma10);

                if (order != null) {
                    stockMarket.place(order);
                }
            }

        }
    }

    private Order makeBuyOrder(Stock stock, StockMarket stockMarket, int turn) {
        int priceLimit = stockMarket.getStockPrice(stock);
        int buyingCapacity = getCash() / priceLimit;
        if (buyingCapacity == 0) {
            return null;
        }
        int maxQuantity = Math.min(stockMarket.getStockQuantity(stock), buyingCapacity);
        int quantity = Randomizer.randomQuantity(maxQuantity);

        return Order.createRandomPriorityOrder(this, stock, quantity, priceLimit, OrderType.BUY, turn);
    }

    private Order makeSellOrder(Stock stock, StockMarket stockMarket, int turn) {
        ArrayList<Stock> portfolioStocks = getPortfolioStocks();
        if (!portfolioStocks.contains(stock)) {
            return null;
        }

        int priceLimit = stockMarket.getStockPrice(stock);
        int quantity = Randomizer.randomQuantity(getPortfolioQuantity(stock));

        return Order.createRandomPriorityOrder(this, stock, quantity, priceLimit, OrderType.SELL, turn);
    }

    private int average(ArrayList<Integer> prices) {
        int sum = 0;
        for (int price : prices) {
            sum += price;
        }
        return sum / prices.size();
    }

    @Override
    public String toString() {
        return "S " + super.toString();
    }
}
