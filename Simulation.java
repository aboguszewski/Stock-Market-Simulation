package stockmarket;

import investors.Investor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Simulation {
    private final ArrayList<Investor> investors;
    private final StockMarket stockMarket;
    private final int turns;

    public static Simulation create(SimulationParameters parameters) {
        ArrayList<Investor> investors = parameters.createInvestors();
        StockMarket stockMarket = parameters.createStockMarket();
        int turns = parameters.getTurns();

        return new Simulation(investors, stockMarket, turns);
    }

    private Simulation(ArrayList<Investor> investors, StockMarket stockMarket, int turns) {
        this.investors = investors;
        this.stockMarket = stockMarket;
        this.turns = turns;
    }

    public void run() {
        for (int i = 0; i < turns; i++) {
            ArrayList<Investor> investorsList = new ArrayList<>(investors);

            Collections.shuffle(investorsList);

            for (Investor investor : investorsList) {
                investor.makeOrder(stockMarket);
            }
            stockMarket.newTurn(i);
        }

        for (Investor investor : investors) {
            System.out.println("networth= " + investor.networth(stockMarket) + " " + investor);
        }

    }

    public int totalInvestorsCash() {
        int totalCash = 0;
        for (Investor investor : investors) {
            totalCash += investor.getCash();
        }
        return totalCash;
    }

    @Override
    public String toString() {
        return investors + " " + stockMarket + " " + turns;
    }

    public Map<Stock, Integer> totalStockQuantities() {
        Map<Stock, Integer> totalStockQuantities = new HashMap<>();

        ArrayList<Stock> stocks = stockMarket.getStocks();
        for (Investor investor : investors) {
            for (Stock stock : stocks) {
                totalStockQuantities.put(stock, totalStockQuantities.getOrDefault(stock, 0) + investor.getPortfolioQuantity(stock));
            }
        }

        return totalStockQuantities;
    }
}
