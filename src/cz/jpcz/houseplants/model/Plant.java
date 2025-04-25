package cz.jpcz.houseplants.model;

import cz.jpcz.houseplants.exceptions.PlantException;
import cz.jpcz.houseplants.util.ConsoleColor;
import cz.jpcz.houseplants.util.DebugManager;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Plant implements Comparable<Plant> {

    private static final char DELIMITER = '\t';

    private String name;
    private Duration wateringInterval;
    private String notes;
    private LocalDate plantedDate;
    private LocalDate lastWateringDate;

    public Plant(String name, Duration wateringInterval, String notes, LocalDate plantedDate, LocalDate lastWateringDate) throws PlantException {
        if (wateringInterval.isNegative() || wateringInterval.isZero()) {
            throw new PlantException("Watering interval cannot be negative or zero.");
        }
        DebugManager.print(ConsoleColor.BLUE + "Creating plant with parameters: " + name + ", " +
                wateringInterval.toDays() + ", " + notes + ", " + plantedDate + ", " + lastWateringDate);
        this.name = name;
        this.wateringInterval = wateringInterval;
        this.notes = notes;
        this.plantedDate = plantedDate;
        this.lastWateringDate = lastWateringDate;
    }

    public Plant(String name, Duration wateringInterval) throws PlantException {
        //Because of rule: this() must be first statement in constructor, object creation can't be delegated to main constructor
        if (wateringInterval.isNegative() || wateringInterval.isZero()) {
            throw new PlantException("Watering interval cannot be negative or zero.");
        }
        DebugManager.print(ConsoleColor.BLUE + "Creating plant with parameters: " + name + ", " +
                wateringInterval.toDays() + ", " + notes + ", " + LocalDate.now() + ", " + LocalDate.now());
        this.name = name;
        this.wateringInterval = wateringInterval;
        this.notes = "";
        this.plantedDate = LocalDate.now();
        this.lastWateringDate = LocalDate.now();
    }

    public Plant(String name) throws PlantException {
        this(name, Duration.ofDays(7));
    }

    public void doWateringNow() {
        this.lastWateringDate = LocalDate.now();
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
    public LocalDate getPlantedDate() {
        return plantedDate;
    }
    public void setPlantedDate(LocalDate plantedDate) {
        this.plantedDate = plantedDate;
    }
    public LocalDate getLastWateringDate() {
        return lastWateringDate;
    }
    public void setLastWateringDate(LocalDate lastWateringDate) {
        this.lastWateringDate = lastWateringDate;
    }

    public String getWateringInfo() {
        return "Plant " + name + " was last watered " + wateringInterval + ". Recommended interval is " + wateringInterval + ".";
    }

    public static Plant serialize(String string) throws PlantException {
        String[] args = string.split(String.valueOf(DELIMITER));
        if (args.length != 5) {
            DebugManager.printError("Illegal number of arguments for Plant constructor(" + args.length + "): " + string);
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Duration wateringInterval = Duration.ofDays(Integer.parseInt(args[2]));
            LocalDate plantedDate = LocalDate.parse(args[3], formatter);
            LocalDate lastWateringDate = LocalDate.parse(args[4], formatter);
            return new Plant(args[0], wateringInterval, args[1], plantedDate, lastWateringDate);
        } catch (ArrayIndexOutOfBoundsException | DateTimeParseException | NumberFormatException e) {
            if (DebugManager.isDebug()) {
                DebugManager.printError("Exception thrown: " + e.getMessage());
            } else {
                e.printStackTrace();
            }
        }
        return null;
    }

public String deserialize() {
    return name + DELIMITER + notes + wateringInterval.toDays() + DELIMITER + plantedDate + DELIMITER + lastWateringDate;
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

    @Override
    public int compareTo(Plant plant) {
        return this.name.compareTo(plant.name);
    }
}