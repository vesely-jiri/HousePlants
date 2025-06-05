package cz.jpcz.houseplants.util.parser;

import cz.jpcz.houseplants.exceptions.PlantException;
import cz.jpcz.houseplants.model.Plant;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Class for parsing string to Plant and vice versa
 */
public class PlantParser {

    private static final String DELIMITER = "\t";

    /**
     * Constructs Plant from string
     * @param string string to parse
     * @return Constructed Plant
     * @throws PlantException if string can't be parsed
     */
    public static Plant deserialize(String string) throws PlantException {
        String[] args = string.split(DELIMITER);
        if (args.length != 5) {
            throw new PlantException("Illegal number of arguments for Plant constructor(" + args.length + "): " + string);
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            Duration wateringInterval = Duration.ofDays(Integer.parseInt(args[2].trim()));
            LocalDate plantedDate = LocalDate.parse(args[4].trim(), formatter);
            LocalDate lastWateringDate = LocalDate.parse(args[3].trim(), formatter);
            return new Plant(args[0].trim(), wateringInterval, args[1].trim(), plantedDate, lastWateringDate);
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

    /**
     * Serializes Plant to string
     * @param plant Plant
     * @return Returns serialized string of Plant
     */
    public static String serialize(Plant plant) {
        return plant.getName() + DELIMITER + plant.getNotes() + DELIMITER + plant.getWateringInterval().toDays()
                + DELIMITER + plant.getLastWateringDate() + DELIMITER + plant.getPlantedDate();
    }
}
