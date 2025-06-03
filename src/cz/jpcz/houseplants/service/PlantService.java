package cz.jpcz.houseplants.service;

import cz.jpcz.houseplants.exceptions.PlantException;
import cz.jpcz.houseplants.model.Plant;
import cz.jpcz.houseplants.model.PlantCollection;
import cz.jpcz.houseplants.util.ConsoleColor;
import cz.jpcz.houseplants.util.DebugManager;
import cz.jpcz.houseplants.util.parser.PlantParser;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing plants
 */
public class PlantService {

    private final PlantCollection plantCollection;

    public PlantService(PlantCollection plantCollection) {
        this.plantCollection = plantCollection;
    }
    public PlantService() {
        this(new PlantCollection());
    }

    public PlantCollection loadFromFile(String path) {
        return loadFromFile(new File(path));
    }
    public PlantCollection loadFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Plant plant = PlantParser.deserialize(line);
                plantCollection.addPlant(plant);
            }
            DebugManager.print(ConsoleColor.BLUE + "Plants loaded from file " + file);
            return plantCollection;
        } catch (IOException | PlantException e) {
            DebugManager.printError("Exception thrown: " + e.getMessage());
            return new PlantCollection();
        }
    }
    public void saveToFile(String path) {
        if (!new File(path).exists()) {
            DebugManager.print(ConsoleColor.BLUE + "File " + path + " does not exist. Creating new file.");
        }
        saveToFile(new File(path));
    }
    public void saveToFile(File file) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)))) {
            for (Plant plant : plantCollection.getPlants()) {
                writer.println(PlantParser.serialize(plant));
            }
            DebugManager.print(ConsoleColor.BLUE + "Plants(" + plantCollection.getPlants().size() + ") saved to file " + file);
        } catch (IOException e) {
            DebugManager.printError("Exception thrown: " + e.getMessage());
        }
    }

    public void addPlant(Plant plant) throws PlantException {
        if (plant == null) throw new PlantException("Plant cannot be null.");
        plantCollection.addPlant(plant);
    }
    public void removePlant(Plant plant) throws PlantException {
        if (plant == null) throw new PlantException("Plant cannot be null.");
        plantCollection.removePlant(plant);
    }
    public void removePlant(int index) throws PlantException {
        plantCollection.removePlant(index);
    }

    public Plant getPlant(int index) {
        return plantCollection.getPlant(index);
    }
    public List<Plant> getPlants() {
        return plantCollection.getPlants();
    }
    public PlantCollection getPlantCollection() {
        return plantCollection;
    }

    public List<Plant> getUnWateredPlants() {
        List<Plant> unWateredPlants = new ArrayList<>();
        for (Plant plant : plantCollection.getPlants()) {
            if (!plant.getLastWateringDate().plusDays(plant.getWateringInterval().toDays()).isBefore(LocalDate.now())) {
                unWateredPlants.add(plant);
            }
        }
        return unWateredPlants;
    }
}
