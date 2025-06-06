package cz.jpcz.houseplants.model;

import cz.jpcz.houseplants.exceptions.PlantException;
import cz.jpcz.houseplants.util.ConsoleColor;
import cz.jpcz.houseplants.util.DebugManager;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Plant implements Comparable<Plant> {
    private String name;
    private Duration wateringInterval;
    private String notes;
    private LocalDate plantedDate;
    private LocalDate lastWateringDate;

    /**
     * Main constructor for creating new plant
     * @param name Name of plant
     * @param wateringInterval Watering interval
     * @param notes Notes
     * @param plantedDate Plant's planted date
     * @param lastWateringDate Plant's last watering date
     */
    public Plant(String name, Duration wateringInterval, String notes, LocalDate plantedDate, LocalDate lastWateringDate) throws PlantException {
        this.name = name;
        this.notes = notes;
        this.lastWateringDate = lastWateringDate;
        setWateringInterval(wateringInterval);
        setPlantedDate(plantedDate);
        DebugManager.print(ConsoleColor.BLUE + "Creating plant with parameters: " + name + ", " +
                wateringInterval.toDays() + ", " + notes + ", " + plantedDate + ", " + lastWateringDate);
    }
    public Plant(String name, Duration wateringInterval) throws PlantException {
        this(name, wateringInterval, "", LocalDate.now(), LocalDate.now());
    }
    public Plant(String name) throws PlantException {
        this(name, Duration.ofDays(7));
    }

    public void doWateringNow() {
        this.lastWateringDate = LocalDate.now();
    }
    public LocalDate getNextWateringDate() {
        return lastWateringDate.plusDays(wateringInterval.toDays());
    }
    public String getWateringInfo() {
        long daysSinceLastWatering = LocalDate.now().toEpochDay() - lastWateringDate.toEpochDay();
        String message = "The plant was last watered on " + lastWateringDate + "(" + daysSinceLastWatering + " days ago)"
                + ". Watering interval is " + wateringInterval.toDays() + " days.";
        if (daysSinceLastWatering >= wateringInterval.toDays()) {
            message += " Consider watering now!";
        }
        return message;
    }
    public boolean isWateringNeeded() {
        return getNextWateringDate().isBefore(LocalDate.now());
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Duration getWateringInterval() {
        return wateringInterval;
    }
    public void setWateringInterval(Duration wateringInterval) throws PlantException {
        if (wateringInterval.isNegative() || wateringInterval.isZero()) {
            throw new PlantException("Watering interval cannot be negative or zero.");
        }
        this.wateringInterval = wateringInterval;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public LocalDate getPlantedDate() {
        return plantedDate;
    }
    public void setPlantedDate(LocalDate plantedDate) throws PlantException {
        if (plantedDate.isAfter(lastWateringDate)) {
            throw new PlantException("Planted date cannot be after last watering date.");
        }
        this.plantedDate = plantedDate;
    }
    public LocalDate getLastWateringDate() {
        return lastWateringDate;
    }
    public void setLastWateringDate(LocalDate lastWateringDate) {
        this.lastWateringDate = lastWateringDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return "Plant{" +
                "name='" + name + '\'' +
                ", wateringInterval=" + wateringInterval.toDays() +
                ", notes='" + notes + '\'' +
                ", plantedDate=" + plantedDate.format(formatter) +
                ", lastWateringDate=" + lastWateringDate.format(formatter) +
                '}';
    }

    @Override
    public int compareTo(Plant plant) {
        return name.compareTo(plant.getName());
    }
}