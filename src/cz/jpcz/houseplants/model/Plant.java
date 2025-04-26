package cz.jpcz.houseplants.model;

import cz.jpcz.houseplants.exceptions.PlantException;
import cz.jpcz.houseplants.util.ConsoleColor;
import cz.jpcz.houseplants.util.DebugManager;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Plant implements Comparable<Plant> {

    private static final String DELIMITER = "\t";

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
        if (wateringInterval.isNegative() || wateringInterval.isZero()) {
            throw new PlantException("Watering interval cannot be negative or zero.");
        }
        if (plantedDate.isAfter(lastWateringDate)) {
            throw new PlantException("Planted date cannot be after last watering date.");
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
        this.name = name;
        this.wateringInterval = wateringInterval;
        this.notes = "";
        this.plantedDate = LocalDate.now();
        this.lastWateringDate = LocalDate.now();
        DebugManager.print(ConsoleColor.BLUE + "Creating plant with parameters: " + name + ", " +
                wateringInterval.toDays() + ", " + notes + ", " + LocalDate.now() + ", " + LocalDate.now());
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
        long daysSinceLastWatering = LocalDate.now().toEpochDay() - (lastWateringDate.toEpochDay());
        if (lastWateringDate.plusDays(wateringInterval.toDays()).isBefore(LocalDate.now())) {
            return "The plant was last watered on " + lastWateringDate + "(" + daysSinceLastWatering + " days ago)" + ". Watering interval should be " +
                    wateringInterval.toDays() + " days. Consider watering now";
        }
        return "The plant was last watered on " + lastWateringDate + ".";
    }

    public static Plant deserialize(String string) throws PlantException {
        String[] args = string.split(DELIMITER);
        if (args.length != 5) {
            throw new PlantException("Illegal number of arguments for Plant constructor(" + args.length + "): " + string);
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Duration wateringInterval = Duration.ofDays(Integer.parseInt(args[2]));
            LocalDate plantedDate = LocalDate.parse(args[4], formatter);
            LocalDate lastWateringDate = LocalDate.parse(args[3], formatter);
            return new Plant(args[0], wateringInterval, args[1], plantedDate, lastWateringDate);
        } catch (NumberFormatException e) {
            throw new PlantException("Failed to parse watering interval: " + string);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new PlantException("Illegal number of arguments for Plant constructor(" + args.length + "): " + string);
        } catch (DateTimeParseException e) {
            throw new PlantException("Failed to parse date: " + string + e);
        } catch (PlantException e) {
            throw new PlantException("Failed to create Plant: " + string + e);
        }
    }

public String serialize() {
    return name + DELIMITER + notes + DELIMITER + wateringInterval.toDays() + DELIMITER + lastWateringDate + DELIMITER + plantedDate;
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
        return this.name.compareTo(plant.name);
    }
}