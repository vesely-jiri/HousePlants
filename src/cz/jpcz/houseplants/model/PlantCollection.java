package cz.jpcz.houseplants.model;

import cz.jpcz.houseplants.exceptions.PlantException;
import cz.jpcz.houseplants.util.ConsoleColor;
import cz.jpcz.houseplants.util.DebugManager;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class PlantCollection {
    public List<Plant> plants = new ArrayList<>();

    public PlantCollection() {}

    public PlantCollection(List<Plant> plants) {
        this.plants.addAll(plants);
    }

    /**
     * Collection contructor for creating and loading plant collections from file
     * @param fileName File name or path to the file containing plant collection
     */
    public PlantCollection(String fileName) {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            while (scanner.hasNextLine()) {
                plants.add(Plant.deserialize(scanner.nextLine()));
            }
        } catch (FileNotFoundException | PlantException e) {
            if (DebugManager.isDebug()) {
                DebugManager.printError("Exception thrown: " + e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
    }

    public void addPlant(Plant plant) {
        this.plants.add(plant);
    }

    public void savePlantsToFile(String path) {
        if (!new File(path).exists()) {
            DebugManager.print(ConsoleColor.BLUE + "File " + path + " does not exist. Creating new file.");
        }
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)))) {
            for (Plant plant : this.plants) {
                writer.println(plant.serialize());
            }
            DebugManager.print(ConsoleColor.BLUE + "Plants(" + plants.size() + ") saved to file " + path);
        } catch (IOException e) {
            DebugManager.printError("Exception thrown: " + e.getMessage());
        }
    }

    public Plant getPlant(int index) {
        if (index < 0 || index >= plants.size()) {
            throw new IndexOutOfBoundsException("Invalid plant index: " + index);
        }
        return plants.get(index);
    }

    public void removePlant(Plant plant) {
        plants.remove(plant);
    }

    public List<Plant> getPlants() {
        return new ArrayList<>(plants);
    }

    public List<Plant> getUnWateredPlants() {
        List<Plant> unWateredPlants = new ArrayList<>();
        for (Plant plant : plants) {
            if (plant.getLastWateringDate().plusDays(plant.getWateringInterval().toDays()).isBefore(LocalDate.now())) {
                unWateredPlants.add(plant);
            }
        }
        return unWateredPlants;
    }
    //More flexible version of getUnWateredPlants method
    public List<Plant> getPlantsByCondition(Predicate<Plant> condition) {
        List<Plant> result = new ArrayList<>();
        for (Plant plant : this.plants) {
            if (condition.test(plant)) {
                result.add(plant);
            }
        }
        return result;
    }

    public void sortPlantsByLastWateringDate() {
        plants.sort(Comparator.comparing(Plant::getLastWateringDate));
    }
}
