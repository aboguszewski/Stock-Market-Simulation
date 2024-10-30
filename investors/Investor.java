package investors;

import stockmarket.Stock;
import stockmarket.StockMarket;

import java.util.ArrayList;
import java.util.Map;

public abstract class Investor {
    private int cash;
    private Map<Stock, Integer> portfolio;

    protected Investor(int cash, Map<Stock, Integer> portfolio) {
        this.cash = cash;
        this.portfolio = portfolio;
    }

    public abstract void makeOrder(StockMarket stockMarket);

    protected ArrayList<Stock> getPortfolioStocks() {
        ArrayList<Stock> ownedStocks = new ArrayList<>();
        for (Stock stock : portfolio.keySet()) {
            if (portfolio.get(stock) > 0) {
                ownedStocks.add(stock);
            }
        }
        return ownedStocks;
    }

    protected boolean ownsStocks() {
        return !getPortfolioStocks().isEmpty();
    }

    public int getPortfolioQuantity(Stock stock) {
        return portfolio.get(stock);
    }

    public int getCash() {
        return cash;
    }

    public void bought(Stock stock, int quantity, int price) {
        cash -= quantity * price;
        int newQuantity = portfolio.get(stock) + quantity;
        portfolio.put(stock, newQuantity);
    }

    public void sold(Stock stock, int quantity, int price) {
        cash += quantity * price;
        int newQuantity = portfolio.get(stock) - quantity;
        portfolio.put(stock, newQuantity);
    }

    public int networth(StockMarket stockMarket) {
        int networth = cash;
        for (Stock stock : portfolio.keySet()) {
            networth += portfolio.get(stock) * stockMarket.getStockPrice(stock);
        }
        return networth;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("cash=").append(cash).append(" ");
        for (Stock stock : portfolio.keySet()) {
            stringBuilder.append(stock).append("=").append(portfolio.get(stock)).append(" ");
        }
        return stringBuilder.toString();
    }
}
