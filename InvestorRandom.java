package investors;

import orders.*;
import stockmarket.Stock;
import stockmarket.StockMarket;

import java.util.Map;

public class InvestorRandom extends Investor {

    public InvestorRandom(int cash, Map<Stock, Integer> portfolio) {
        super(cash, portfolio);
    }

    @Override
    public void makeOrder(StockMarket stockMarket) {
        OrderType orderType;
        if (ownsStocks()) {
            orderType = Randomizer.randomOrderType();
        } else {
            orderType = OrderType.BUY;
        }

        Stock stock = null;
        int quantity = 0;
        int priceLimit = 0;

        switch (orderType) {
            case BUY:
                stock = Randomizer.randomStock(stockMarket.getStocks());
                priceLimit = Randomizer.randomPrice(stockMarket.getStockPrice(stock));
                int buyingCapacity = getCash() / priceLimit;
                int maxQuantity = Math.min(stockMarket.getStockQuantity(stock), buyingCapacity);
                if (maxQuantity <= 0) {
                    return;
                }
                quantity = Randomizer.randomQuantity(maxQuantity);
                break;

            case SELL:
                stock = Randomizer.randomStock(getPortfolioStocks());
                priceLimit = Randomizer.randomPrice(stockMarket.getStockPrice(stock));
                quantity = Randomizer.randomQuantity(getPortfolioQuantity(stock));
                break;
        }

        Order order = Order.createRandomPriorityOrder(this, stock, quantity, priceLimit, orderType, stockMarket.getTurn());
        stockMarket.place(order);
    }

    @Override
    public String toString() {
        return "R " + super.toString();
    }

}
