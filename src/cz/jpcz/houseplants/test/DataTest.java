package cz.jpcz.houseplants.test;

import cz.jpcz.houseplants.exceptions.PlantException;
import cz.jpcz.houseplants.model.Plant;
import cz.jpcz.houseplants.model.PlantCollection;
import cz.jpcz.houseplants.util.DebugManager;
import cz.jpcz.houseplants.util.ConsoleColor;

import java.time.Duration;
import java.time.LocalDate;

public class DataTest {
    public static void run() {
        DebugManager.setDebug(true);
        DebugManager.print(ConsoleColor.GREEN + "Running DataTest");

        DebugManager.print(ConsoleColor.YELLOW + "Creating 3 plants");
        try {
            Plant plant1 = new Plant("Kopretina1", Duration.ofDays(5),
                    "Notes for Kopretina",
                    LocalDate.now(), LocalDate.now());
            Plant plant2 = new Plant("Kopretina2", Duration.ofDays(3));
            Plant plant3 = new Plant("Kopretina3");
        } catch (PlantException e) {
            DebugManager.printError("Exception thrown: " + e.getMessage());
        }

        DebugManager.print(ConsoleColor.YELLOW + "Testing 0 or negative watering interval. Exception should be thrown:");
        try {
            Plant plant4 = new Plant("Kopretina4", Duration.ofDays(-1));
        } catch (PlantException e) {
            DebugManager.printError("Exception thrown: " + e.getMessage());

        }

        DebugManager.print(ConsoleColor.YELLOW + "Testing loading data from file(A): test-plants.txt");
        PlantCollection plantCollection = new PlantCollection("test-plants.txt");
        DebugManager.print(ConsoleColor.YELLOW + "Printing loaded plants of file (A):");
        plantCollection.getPlants().forEach(p -> DebugManager.print(ConsoleColor.BLUE + p.toString()));

        DebugManager.print(ConsoleColor.YELLOW + "Testing saving data to file(B): test-plants2.txt");
        plantCollection.savePlantsToFile("test-plants2.txt");

        DebugManager.print(ConsoleColor.YELLOW + "Testing loading data in wrong format from file(B): kvetiny-spatne-datum.txt");
        PlantCollection plantCollection2 = new PlantCollection("kvetiny-spatne-datum.txt");

        DebugManager.printError(ConsoleColor.YELLOW + "Testing loading data in wrong format from file(B): kvetiny-spatne-frekvence.txt");
        PlantCollection plantCollection3 = new PlantCollection("kvetiny-spatne-frekvence.txt");

        //TODO: Test Collection methods and other


        DebugManager.print(ConsoleColor.GREEN + "DataTest finished");
    }
}