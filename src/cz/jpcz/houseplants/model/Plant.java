package cz.jpcz.houseplants.model;

import cz.jpcz.houseplants.exceptions.PlantException;
import cz.jpcz.houseplants.util.ConsoleColor;
import cz.jpcz.houseplants.util.DebugManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Plant {

    private String name;
    private Duration wateringInterval;
    private String notes;
    private LocalDateTime plantedDate;
    private LocalDateTime lastWateringDate;

    public Plant(String name, Duration wateringInterval, String notes, LocalDateTime plantedDate, LocalDateTime lastWateringDate) {
        DebugManager.print(ConsoleColor.BLUE + "Creating plant with parameters: " + name + ", " +
                wateringInterval.toDays() + ", " + notes + ", " + plantedDate + ", " + lastWateringDate);
        this.name = name;
        try {
            if (wateringInterval.isNegative() || wateringInterval.isZero()) {
                throw new PlantException("Watering interval cannot be 0 or negative.");
            }
        } catch (PlantException e) {
            if (DebugManager.isDebug()) {
                DebugManager.printError(e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
        this.wateringInterval = wateringInterval;
        this.notes = notes;
        this.plantedDate = plantedDate;
        this.lastWateringDate = lastWateringDate;
    }

    public Plant(String name, Duration wateringInterval) {
        this(name, wateringInterval, "", LocalDateTime.now(), LocalDateTime.now());
    }

    public Plant(String name) {
        this(name, Duration.ofDays(7));
    }

    public void doWateringNow() {
        this.lastWateringDate = LocalDateTime.now();
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
    public void setWateringInterval(Duration wateringInterval) {
        this.wateringInterval = wateringInterval;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public LocalDateTime getPlantedDate() {
        return plantedDate;
    }
    public void setPlantedDate(LocalDateTime plantedDate) {
        this.plantedDate = plantedDate;
    }
    public LocalDateTime getLastWateringDate() {
        return lastWateringDate;
    }
    public void setLastWateringDate(LocalDateTime lastWateringDate) {
        this.lastWateringDate = lastWateringDate;
    }

    public String getWateringInfo() {
        return "Plant " + name + " was last watered " + wateringInterval + ". Recommended interval is " + wateringInterval + ".";
    }

    @Override
    public String toString() {
        return "Plant{" +
                "name='" + name + '\'' +
                ", wateringInterval=" + wateringInterval +
                ", notes='" + notes + '\'' +
                ", plantedDate=" + plantedDate +
                ", lastWateringDate=" + lastWateringDate +
                '}';
    }
}
