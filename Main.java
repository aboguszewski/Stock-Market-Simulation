package main;

import stockmarket.Simulation;
import stockmarket.SimulationParameters;

public class Main {

    // Zalecam uruchomić symulację dla kilku tysięcy tur, żeby nie trzeba było czekać zbyt długo
    // na zakończenie działania programu.
    // Dla dowolnej liczby tur program się zakończy, ale dla przykładowych danych z moodle-a trwa to bardzo długo.

    public static void main(String[] args) {
        SimulationParameters params = SimulationParameters.load(args[0], Integer.parseInt(args[1]));

        Simulation simulation = Simulation.create(params);
        simulation.run();
    }

}
