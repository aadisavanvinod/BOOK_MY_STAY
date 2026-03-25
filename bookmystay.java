import java.util.HashMap;
import java.util.Map;

/**
 * ============================================================
 * APPLICATION - bookmystay
 * ============================================================
 *
 * Use Case 4: Room Search & Availability Check
 *
 * @version 4.0
 */
public class bookmystay {

    public static void main(String[] args) {

        System.out.println("====== Welcome to BookMyStay ======\n");

        // Room objects (Domain Model)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Centralized Inventory
        RoomInventory inventory = new RoomInventory();

        // Search Service (Read-only)
        RoomSearchService searchService = new RoomSearchService();

        // Perform search (NO mutation)
        searchService.searchAvailableRooms(inventory, single, doubleRoom, suite);

        System.out.println("\nApplication terminated.");
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
 * Centralized inventory (Single Source of Truth)
 *
 * @version 3.1
 */
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    // Read-only access method
    public Map<String, Integer> getRoomAvailability() {
        return inventory;
    }
}

/* ============================================================
 * CLASS - RoomSearchService
 * ============================================================
 *
 * Use Case 4: Read-only search service
 *
 * @version 4.0
 */
class RoomSearchService {

    /**
     * Displays available rooms with details
     */
    public void searchAvailableRooms(
            RoomInventory inventory,
            Room singleRoom,
            Room doubleRoom,
            Room suiteRoom) {

        Map<String, Integer> availability = inventory.getRoomAvailability();

        System.out.println("=== Available Rooms ===\n");

        // Single Room
        if (availability.get("Single") > 0) {
            System.out.println("Single Room:");
            singleRoom.displayRoomDetails();
            System.out.println("Available: " + availability.get("Single") + "\n");
        }

        // Double Room
        if (availability.get("Double") > 0) {
            System.out.println("Double Room:");
            doubleRoom.displayRoomDetails();
            System.out.println("Available: " + availability.get("Double") + "\n");
        }

        // Suite Room
        if (availability.get("Suite") > 0) {
            System.out.println("Suite Room:");
            suiteRoom.displayRoomDetails();
            System.out.println("Available: " + availability.get("Suite") + "\n");
        }
    }
}