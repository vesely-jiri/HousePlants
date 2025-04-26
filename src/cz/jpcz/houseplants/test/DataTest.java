package cz.jpcz.houseplants.test;

import cz.jpcz.houseplants.exceptions.PlantException;
import cz.jpcz.houseplants.model.Plant;
import cz.jpcz.houseplants.model.PlantCollection;
import cz.jpcz.houseplants.util.DebugManager;
import cz.jpcz.houseplants.util.ConsoleColor;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

    /**
     * <pre>
     * BLUE   - program output
     * RED    - error output
     * YELLOW - new test section
     * PURPLE - assignment tasks
     * GREEN  - correct output
     * <pre>
     */
public final class DataTest {

    private DataTest() {}

    /**
     * If true, generated test files will be deleted at the end of the testg
     */
    private static final boolean fileCleanup = true;

    public static void run() {
        DebugManager.setDebug(true);
        DebugManager.print(ConsoleColor.PURPLE + "Running DataTest");

        testAssignment();

        testPlantCreation();
        testInvalidWateringInterval();
        testSavingAndLoading();
        testSortPlantsByName();
        testSortPlantsByLastWateringDate();
        testErrorLoadingWrongFileFormat();
        testGetUnWateredPlants();

        if (fileCleanup) {
            cleanupGeneratedFiles();
        }

        DebugManager.print(ConsoleColor.PURPLE + "DataTest finished");
    }

    private static void testPlantCreation() {
        DebugManager.printHeader("Testing creation of 3 plants with different parameters");
        try {
            Plant plant1 = new Plant("Kopretina1", Duration.ofDays(5),
                    "Notes for Kopretina", LocalDate.now(), LocalDate.now());
            Plant plant2 = new Plant("Kopretina2", Duration.ofDays(3));
            Plant plant3 = new Plant("Kopretina3");
            DebugManager.print(ConsoleColor.GREEN + "Plants created successfully.");
        } catch (PlantException e) {
            DebugManager.printError("ERROR! Exception thrown during plant creation: " + e.getMessage());
        }
    }

    private static void testInvalidWateringInterval() {
        DebugManager.printHeader("Testing invalid watering interval (should throw exception)");
        try {
            new Plant("Kopretina4", Duration.ofDays(-1));
            DebugManager.printError("ERROR! Plant with negative watering interval was created!");
        } catch (PlantException e) {
            DebugManager.print(ConsoleColor.GREEN + "Correctly caught exception: " + e.getMessage());
        }
    }

    private static void testSavingAndLoading() {
        DebugManager.printHeader("Testing saving and loading plants");

        PlantCollection plantCollection = new PlantCollection("resources/kvetiny.txt");
        try {
            Plant plant1 = new Plant("TestPlant", Duration.ofDays(2));
            plantCollection.addPlant(plant1);

            plantCollection.saveCollectionToFile("resources/test-plants2.txt");
            DebugManager.print(ConsoleColor.BLUE + "Plants saved successfully to test-plants2.txt");

            PlantCollection loadedCollection = new PlantCollection("resources/test-plants2.txt");

            loadedCollection.getPlants().forEach(p ->
                    DebugManager.print(ConsoleColor.BLUE + p.toString()));

            DebugManager.print(ConsoleColor.GREEN + "Plants loaded successfully from test-plants2.txt");

        } catch (PlantException e) {
            DebugManager.printError("ERROR! Exception during saving/loading: " + e.getMessage());
        }
    }

    private static void testErrorLoadingWrongFileFormat() {
        DebugManager.printHeader("Testing loading plants from wrong format files");

        PlantCollection collection1 = new PlantCollection("resources/kvetiny-spatne-datum.txt");
        PlantCollection collection2 = new PlantCollection("resources/kvetiny-spatne-frekvence.txt");

        DebugManager.print(ConsoleColor.GREEN + "Finished testing wrong format files (2 errors should have been printed above).");
    }

    private static void testGetUnWateredPlants() {
        DebugManager.printHeader("Testing getUnWateredPlants method");

        PlantCollection plantCollection = new PlantCollection("resources/kvetiny.txt");

        plantCollection.getUnWateredPlants().forEach(plant ->
                DebugManager.print(ConsoleColor.GREEN + plant.getWateringInfo()));
    }

    public static void testSortPlantsByName() {
        DebugManager.printHeader("Testing sortPlantsByName method");

        PlantCollection plantCollection = new PlantCollection("resources/kvetiny.txt");

        DebugManager.print(ConsoleColor.BLUE + "Plants before sorting:");
        plantCollection.getPlants().forEach(plant ->
                DebugManager.print(ConsoleColor.GREEN + plant.getName()));

        List<Plant> plants = plantCollection.getPlants();
        Collections.sort(plants);

        DebugManager.print(ConsoleColor.BLUE + "Plants after sorting:");
        plants.forEach(plant ->
                DebugManager.print(ConsoleColor.GREEN + plant.getName()));
    }

    /**
     * Tests all assignment tasks
     */
    public static void testAssignment() {
        DebugManager.printHeader(ConsoleColor.YELLOW + "Testing assignment");

        DebugManager.print(ConsoleColor.PURPLE + "Loading plants from file kvetiny.txt");
        PlantCollection plantCollection = new PlantCollection("resources/kvetiny.txt");

        DebugManager.print(ConsoleColor.PURPLE + "Printing watering information about all plants");
        plantCollection.getPlants().forEach(plant ->
                DebugManager.print(ConsoleColor.GREEN + plant.getWateringInfo()));

        DebugManager.print(ConsoleColor.PURPLE + "Adding new plant to collection");
        try {
            Plant plant = new Plant("Růže pro Růžu", Duration.ofDays(2),"", LocalDate.now(), LocalDate.now());
            plantCollection.addPlant(plant);
        } catch (PlantException e) {
            DebugManager.printError("ERROR! Exception thrown during plant creation: " + e.getMessage());
        }

        DebugManager.print(ConsoleColor.PURPLE + "Adding " + 10 + " plants to collection");
        for (int i = 0; i < 10; i++) {
            try {
                Plant plant = new Plant("Tulipán na prodej " + (i + 1), Duration.ofDays(14),"", LocalDate.now(), LocalDate.now());
                plantCollection.addPlant(plant);
            } catch (PlantException e) {
                DebugManager.printError("ERROR! Exception thrown during plant creation: " + e.getMessage());
            }
        }

        DebugManager.print(ConsoleColor.PURPLE + "Removing 3rd plant from collection. Collection size: " + plantCollection.getPlants().size());
        try {
            plantCollection.removePlant(2);
        } catch (IndexOutOfBoundsException e) {
            DebugManager.printError("ERROR! Exception thrown during plant removal: " + e.getMessage());
        }
        DebugManager.print(ConsoleColor.GREEN + "Removed 3rd plant from collection. Collection size: " + plantCollection.getPlants().size());

        DebugManager.print(ConsoleColor.PURPLE + "Saving plants to new file new-plants.txt");
        plantCollection.saveCollectionToFile("resources/new-plants.txt");

        DebugManager.print(ConsoleColor.PURPLE + "Loading plants from new file new-plants.txt");
        PlantCollection newCollection = new PlantCollection("resources/new-plants.txt");
        newCollection.getPlants().forEach(plant ->
                DebugManager.print(ConsoleColor.GREEN + plant.toString()));

        DebugManager.print(ConsoleColor.PURPLE + "Sorting plants by name");
        Collections.sort(newCollection.getPlants());
        newCollection.getPlants().forEach(plant ->
                DebugManager.print(ConsoleColor.GREEN + plant.toString()));

        DebugManager.print(ConsoleColor.PURPLE + "Sorting plants by last watering date");
        newCollection.sortPlantsByLastWateringDate();
        newCollection.getPlants().forEach(plant ->
                DebugManager.print(ConsoleColor.GREEN + plant.toString()));
    }


    public static void testSortPlantsByLastWateringDate() {
        DebugManager.printHeader("Testing sortPlantsByLastWateringDate method");

        PlantCollection plantCollection = new PlantCollection("resources/kvetiny.txt");

        plantCollection.getPlants().forEach(plant ->
                DebugManager.print("[BEFORE SORT] " + ConsoleColor.GREEN +
                        plant.getLastWateringDate() + " - " + plant));

        plantCollection.sortPlantsByLastWateringDate();

        plantCollection.getPlants().forEach(plant ->
                DebugManager.print("[AFTER SORT] " + ConsoleColor.GREEN +
                        plant.getLastWateringDate() + " - " + plant));
    }

    private static void cleanupGeneratedFiles() {
        DebugManager.printHeader("Cleaning up generated test files");

        deleteFileIfExists("resources/new-plants.txt");
        deleteFileIfExists("resources/test-plants2.txt");
    }

    private static void deleteFileIfExists(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            if (file.delete()) {
                DebugManager.print(ConsoleColor.BLUE + "File " + fileName + " deleted.");
            } else {
                DebugManager.printError("Failed to delete file " + fileName);
            }
        } else {
            DebugManager.print(ConsoleColor.RED + "File " + fileName + " does not exist.");
        }
    }
}
