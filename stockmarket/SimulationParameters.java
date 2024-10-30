package stockmarket;

import investors.Investor;
import investors.InvestorRandom;
import investors.InvestorSMA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SimulationParameters {
    private final int investorsRandom;
    private final int investorsSMA;
    private final Map<Stock, Integer> stockPrices;
    private final int initialCash;
    private final Map<Stock, Integer> initialPortfolio;
    private final int turns;

    public static SimulationParameters load(String filename, int turns) {
        int randomInvestors = 0;
        int smaInvestors = 0;
        Map<Stock, Integer> stockPrices = new HashMap<>();
        int initialCash = 0;
        Map<Stock, Integer> initialPortfolio = new HashMap<>();

        try (Scanner scanner = new Scanner(new File(filename), StandardCharsets.UTF_8)) {
            int dataLinesRead = 0;

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                if (!line.startsWith("#")) {
                    dataLinesRead++;
                    Scanner lineScanner = new Scanner(line);

                    switch (dataLinesRead) {
                        case 1:
                            while (lineScanner.hasNext()) {
                                String strategySymbol = lineScanner.next();
                                if (strategySymbol.equals("R")) {
                                    randomInvestors++;
                                } else if (strategySymbol.equals("S")) {
                                    smaInvestors++;
                                } else {
                                    System.err.println("Invalid strategy symbol: " + strategySymbol);
                                    System.exit(1);
                                }
                            }
                            break;
                        case 2:
                            while (lineScanner.hasNext()) {
                                String stockData = lineScanner.next();
                                String[] stockDataParts = stockData.split(":");
                                String stockSymbol = stockDataParts[0];
                                String stockPrice = stockDataParts[1];
                                stockPrices.put(new Stock(stockSymbol), Integer.parseInt(stockPrice));;
                            }
                            break;
                        case 3:
                            initialCash = lineScanner.nextInt();
                            while (lineScanner.hasNext()) {
                                String stockData = lineScanner.next();
                                String[] stockDataParts = stockData.split(":");
                                String stockSymbol = stockDataParts[0];
                                if (!stockPrices.containsKey(new Stock(stockSymbol))) {
                                    System.err.println("Stock " + stockSymbol + " is not traded");
                                    System.exit(1);
                                }
                                String stockAmount = stockDataParts[1];
                                initialPortfolio.put(new Stock(stockSymbol), Integer.parseInt(stockAmount));
                            }
                            break;
                    }
                    lineScanner.close();
                }
            }
        } catch (IOException exception) {
            System.err.println("Not able to read file: " + filename);
            System.exit(1);
        }


        return new SimulationParameters(randomInvestors, smaInvestors,
                                        stockPrices, initialCash,
                                        initialPortfolio, turns);
    }

    public SimulationParameters(int randomInvestors, int smaInvestors,
                                 Map<Stock, Integer> stockPrices,
                                 int initialCash, Map<Stock, Integer> initialPortfolio,
                                 int turns) {
        this.investorsRandom = randomInvestors;
        this.investorsSMA = smaInvestors;
        this.stockPrices = stockPrices;
        this.initialCash = initialCash;
        this.initialPortfolio = initialPortfolio;
        this.turns = turns;
    }

    @Override
    public String toString() {
        return investorsRandom + " " + investorsSMA + " " + stockPrices + " " + initialCash + " " + initialPortfolio;
    }

    public ArrayList<Investor> createInvestors() {
        ArrayList<Investor> investors = new ArrayList<>();
        for (int i = 0; i < investorsRandom; i++) {
            Map<Stock, Integer> initialPortfolioCopy = new HashMap<>(initialPortfolio);
            investors.add(new InvestorRandom(initialCash, initialPortfolioCopy));
        }
        for (int i = 0; i < investorsSMA; i++) {
            Map<Stock, Integer> initialPortfolioCopy = new HashMap<>(initialPortfolio);
            investors.add(new InvestorSMA(initialCash, initialPortfolioCopy));
        }
        return investors;
    }

    public StockMarket createStockMarket() {
        Map<Stock, Integer> stockQuantities = new HashMap<>();
        for (Stock stock : stockPrices.keySet()) {
            int stockQuantity = (investorsRandom + investorsSMA) * initialPortfolio.get(stock);
            stockQuantities.put(stock, stockQuantity);
        }

        return new StockMarket(stockPrices, stockQuantities);
    }

    public int getTurns() {
        return turns;
    }

}
