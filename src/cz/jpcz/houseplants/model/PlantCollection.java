package cz.jpcz.houseplants.model;

import cz.jpcz.houseplants.exceptions.PlantException;
import cz.jpcz.houseplants.util.DebugManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class PlantCollection {
    List<Plant> plants = new ArrayList<>();

    public PlantCollection() {}

    //Sukulent v koupelně	Nezalévá se	365	2011-03-01	2011-03-01
    public PlantCollection(String fileName) {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(fileName)))) {
            while (scanner.hasNextLine()) {
                //TODO: check if the line is valid
                plants.add(Plant.serialize(scanner.nextLine()));
            }
        } catch (FileNotFoundException e) {
            DebugManager.printError("File " + fileName + " was not found");
        } catch (PlantException e) {
            DebugManager.printError("Exception thrown: " + e.getMessage());
        }
    }

    public void addPlant(Plant plant) {
        plants.add(plant);
    }

    public void savePlantsToFile() {

    }

    public Plant getPlant(int index) {
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
            if (plant.getLastWateringDate().plus(plant.getWateringInterval()).isBefore(LocalDateTime.now())) {
                unWateredPlants.add(plant);
            }
        }
        return unWateredPlants;
    }

    public void sortPlantsByWateringInterval() {
        plants.sort(Comparator.comparing(Plant::getWateringInterval));
    }
}
