package cz.jpcz.houseplants.test;

import cz.jpcz.houseplants.model.Plant;
import cz.jpcz.houseplants.util.DebugManager;
import cz.jpcz.houseplants.util.ConsoleColor;

import java.time.Duration;
import java.time.LocalDateTime;

public class DataTest {
    public static void run() {
        DebugManager.setDebug(true);
        DebugManager.print(ConsoleColor.GREEN + "Running DataTest");

        DebugManager.print(ConsoleColor.YELLOW + "Creating 3 plants");
        Plant plant1 = new Plant("Kopretina", Duration.ofDays(3),
                "Notes for Kopretina",
                LocalDateTime.now(),
                LocalDateTime.now().minusDays(2));
        Plant plant2 = new Plant("Kopretina", Duration.ofDays(3));
        Plant plant3 = new Plant("Kopretina");


        DebugManager.print(ConsoleColor.YELLOW + "Testing 0 or negative watering interval");
        Plant plant4 = new Plant("Kopretina", Duration.ofDays(-1));



        DebugManager.print(ConsoleColor.GREEN + "DataTest finished");
    }
}
