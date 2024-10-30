package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stockmarket.Simulation;
import stockmarket.SimulationParameters;
import stockmarket.Stock;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class simulationTests {
    private static Simulation simulation;

    @BeforeEach
    public void setUp() {
        int investorsRandom = 2;
        int investorsSMA = 1;
        int initialCash = 50000;
        int turns = 100;

        Stock A = new Stock("A");
        Stock B = new Stock("B");
        Stock C = new Stock("C");

        Map<Stock, Integer> stockPrices = Map.of(
                A, 72,
                B, 150,
                C, 1350
        );

        Map<Stock, Integer> initialPortfolio = Map.of(
                A, 3,
                B, 8,
                C, 2
        );

        SimulationParameters params = new SimulationParameters(investorsRandom, investorsSMA, stockPrices, initialCash, initialPortfolio, turns);
        simulation = Simulation.create(params);
    }

    @Test
    public void testTotalInvestorsCash() {
        int totalCash = 150000;
        assertEquals(simulation.totalInvestorsCash(), totalCash);
        System.out.println("TotalInvestorsCash test passed");
    }

    @Test
    public void testConstantCash() {
        int initialCash = simulation.totalInvestorsCash();
        simulation.run();
        assertEquals(simulation.totalInvestorsCash(), initialCash);
        System.out.println("ConstantCash test passed");
    }

    @Test
    public void testTotalStockQuantities() {
        Map<Stock, Integer> totalStockQuantity = simulation.totalStockQuantities();
        assertEquals(totalStockQuantity.get(new Stock("A")), 3*3);
        assertEquals(totalStockQuantity.get(new Stock("B")), 8*3);
        assertEquals(totalStockQuantity.get(new Stock("C")), 2*3);
        System.out.println("TotalStockQuantities test passed");
    }

    @Test
    public void testConstantStockQuantities() {
        Map<Stock, Integer> totalStockQuantity = simulation.totalStockQuantities();
        simulation.run();

        Map<Stock, Integer> totalStockQuantityAfter = simulation.totalStockQuantities();
        for (Stock stock : totalStockQuantity.keySet()) {
            assertEquals(totalStockQuantity.get(stock), totalStockQuantityAfter.get(stock));
        }
        System.out.println("ConstantStockQuantities test passed");
    }
}
