import java.util.*;
import java.io.*;

/**
 * ============================================================
 * APPLICATION - bookmystay
 * ============================================================
 *
 * Use Case 12: Data Persistence & System Recovery
 *
 * @version 12.0
 */
public class bookmystay {

    public static void main(String[] args) {

        System.out.println("System Recovery\n");

        String filePath = "inventory.txt";

        RoomInventory inventory = new RoomInventory();
        FilePersistenceService persistence = new FilePersistenceService();

        // Try loading previous state
        boolean loaded = persistence.loadInventory(inventory, filePath);

        if (!loaded) {
            System.out.println("No valid inventory data found. Starting fresh.\n");

            // Initialize default inventory
            inventory.addRoomType("Single", 5);
            inventory.addRoomType("Double", 3);
            inventory.addRoomType("Suite", 2);
        }

        // Display current inventory
        System.out.println("Current Inventory:");
        inventory.displayInventory();

        // Save current state
        persistence.saveInventory(inventory, filePath);
        System.out.println("\nInventory saved successfully.");
    }
}

/* ============================================================
 * CLASS - RoomInventory
 * ============================================================ */
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    public void setRoomCount(String type, int count) {
        inventory.put(type, count);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void displayInventory() {
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

/* ============================================================
 * CLASS - FilePersistenceService
 * ============================================================
 *
 * Handles saving & loading inventory from file
 *
 * @version 12.0
 */
class FilePersistenceService {

    // Save inventory to file
    public void saveInventory(RoomInventory inventory, String filePath) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (Map.Entry<String, Integer> entry : inventory.getInventory().entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving inventory.");
        }
    }

    // Load inventory from file
    public boolean loadInventory(RoomInventory inventory, String filePath) {

        File file = new File(filePath);

        if (!file.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split("=");

                if (parts.length != 2) {
                    return false; // corrupted format
                }

                String roomType = parts[0];
                int count = Integer.parseInt(parts[1]);

                inventory.setRoomCount(roomType, count);
            }

            return true;

        } catch (Exception e) {
            return false; // corrupted file or parse error
        }
    }
}