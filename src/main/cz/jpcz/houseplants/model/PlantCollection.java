package cz.jpcz.houseplants.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

/**
 * Class representing collection of Plants
 */
public class PlantCollection {

    List<Plant> plants = new ArrayList<>();

    public PlantCollection(List<Plant> plants) {
        this.plants.addAll(plants);
    }
    public PlantCollection() {};

    public void addPlant(Plant plant) {
        this.plants.add(plant);
    }
    public void removePlant(Plant plant) {
        plants.remove(plant);
    }
    public void removePlant(int index) {
        plants.remove(index);
    }

    public Plant getPlant(int index) {
        if (index < 0 || index >= plants.size()) {
            throw new IndexOutOfBoundsException("Invalid plant index: " + index);
        }
        return plants.get(index);
    }
    public List<Plant> getPlants() {
        return new ArrayList<>(plants);
    }

    public List<Plant> getPlantsByCondition(Predicate<Plant> condition) {
        return plants.stream()
                .filter(condition)
                .toList();
    }

    public void sortPlantsByLastWateringDate() {
        plants.sort(Comparator.comparing(Plant::getLastWateringDate));
    }
}