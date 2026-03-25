import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================
 * APPLICATION - bookmystay
 * ============================================================
 *
 * Use Case 3: Centralized Room Inventory Management
 *
 * @version 3.0
 */

public class bookmystay {

    public static void main(String[] args) {

        System.out.println("====== Welcome to BookMyStay ======\n");

        // Create Room objects
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Initialize Inventory (Single Source of Truth)
        RoomInventory inventory = new RoomInventory();

        // Display room details + availability
        displayRoom("Single Room", single, inventory);
        displayRoom("Double Room", doubleRoom, inventory);
        displayRoom("Suite Room", suite, inventory);

        // Example update (booking simulation)
        System.out.println("\n--- Booking 1 Single Room ---");
        inventory.updateAvailability("SingleRoom", -1);

        // Display updated inventory
        displayRoom("Single Room", single, inventory);

        System.out.println("\nApplication terminated.");
    }

    // Helper method (clean design)
    public static void displayRoom(String type, Room room, RoomInventory inventory) {
        System.out.println(type + ":");
        room.displayRoomDetails();
        System.out.println("Available: " + inventory.getAvailability(type.replace(" ", "")));
        System.out.println();
    }
}

/* ============================================================
 * ABSTRACT CLASS - Room
 * ============================================================ */
abstract class Room {

    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    public void displayRoomDetails() {
        System.out.println("Beds: " + numberOfBeds);
        System.out.println("Size: " + squareFeet + " sqft");
        System.out.println("Price per night: " + pricePerNight);
    }
}

/* ============================================================
 * ROOM TYPES
 * ============================================================ */
class SingleRoom extends Room {
    public SingleRoom() {
        super(1, 250, 1500.0);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super(2, 400, 2500.0);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super(3, 750, 5000.0);
    }
}

/* ============================================================
 * CLASS - RoomInventory
 * ============================================================
 *
 * Centralized inventory using HashMap
 *
 * @version 3.1
 */
class RoomInventory {

    private Map<String, Integer> inventory;

    // Constructor initializes availability
    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("SingleRoom", 5);
        inventory.put("DoubleRoom", 3);
        inventory.put("SuiteRoom", 2);
    }

    // Get availability (O(1))
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Controlled update
    public void updateAvailability(String roomType, int change) {
        int current = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, current + change);
    }

    // Display full inventory
    public void displayInventory() {
        System.out.println("=== Current Inventory ===");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}