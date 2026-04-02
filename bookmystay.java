import java.util.*;

/**
 * ============================================================
 * APPLICATION - bookmystay
 * ============================================================
 *
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * @version 10.0
 */
public class bookmystay {

    public static void main(String[] args) {

        System.out.println("Booking Cancellation\n");

        // Initialize Inventory
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single", 5);

        // Cancellation Service
        CancellationService service = new CancellationService();

        // Simulate confirmed booking
        String reservationId = "Single-1";
        service.registerBooking(reservationId, "Single");

        // Cancel booking
        service.cancelBooking(reservationId, inventory);

        // Show rollback history
        service.showRollbackHistory();

        // Show updated inventory
        System.out.println("\nUpdated Single Room Availability: "
                + inventory.getAvailableRooms("Single"));
    }
}

/* ============================================================
 * CLASS - RoomInventory
 * ============================================================
 *
 * Manages room availability state
 *
 * @version 10.0
 */
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
    }

    public void addRoomType(String type, int count) {
        inventory.put(type, count);
    }

    // Rollback operation (increment)
    public void increaseInventory(String type) {
        inventory.put(type, inventory.getOrDefault(type, 0) + 1);
    }

    public int getAvailableRooms(String type) {
        return inventory.getOrDefault(type, 0);
    }
}

/* ============================================================
 * CLASS - CancellationService
 * ============================================================
 *
 * Handles booking cancellation and rollback logic
 *
 * @version 10.0
 */
class CancellationService {

    /** Stack to track released room IDs (LIFO rollback) */
    private Stack<String> releasedRoomIds;

    /** Maps reservationId → roomType */
    private Map<String, String> reservationRoomTypeMap;

    public CancellationService() {
        releasedRoomIds = new Stack<>();
        reservationRoomTypeMap = new HashMap<>();
    }

    /**
     * Registers a confirmed booking
     */
    public void registerBooking(String reservationId, String roomType) {
        reservationRoomTypeMap.put(reservationId, roomType);
    }

    /**
     * Cancels booking and restores inventory
     */
    public void cancelBooking(String reservationId, RoomInventory inventory) {

        // Validation (fail-fast)
        if (reservationId == null || reservationId.trim().isEmpty()) {
            System.out.println("Invalid reservation ID.");
            return;
        }

        if (!reservationRoomTypeMap.containsKey(reservationId)) {
            System.out.println("Cancellation failed: Reservation not found.");
            return;
        }

        String roomType = reservationRoomTypeMap.get(reservationId);

        // Rollback tracking (LIFO)
        releasedRoomIds.push(reservationId);

        // Restore inventory
        inventory.increaseInventory(roomType);

        // Remove booking record
        reservationRoomTypeMap.remove(reservationId);

        System.out.println("Booking cancelled for ID: "
                + reservationId + " | Inventory restored for: " + roomType);
    }

    /**
     * Displays rollback history
     */
    public void showRollbackHistory() {

        System.out.println("\nRollback History (Most Recent First):");

        if (releasedRoomIds.isEmpty()) {
            System.out.println("No cancellations recorded.");
            return;
        }

        // LIFO order
        for (int i = releasedRoomIds.size() - 1; i >= 0; i--) {
            System.out.println("Released Reservation ID: " + releasedRoomIds.get(i));
        }
    }
}