import java.util.*;

/**
 * ============================================================
 * APPLICATION - bookmystay
 * ============================================================
 *
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * @version 6.0
 */
public class bookmystay {

    public static void main(String[] args) {

        System.out.println("Room Allocation Processing\n");

        // Queue (Use Case 5)
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.addRequest(new Reservation("Abhi", "Single"));
        bookingQueue.addRequest(new Reservation("Subha", "Single"));
        bookingQueue.addRequest(new Reservation("Vanmathi", "Suite"));

        // Inventory (Use Case 3)
        RoomInventory inventory = new RoomInventory();

        // Allocation Service (Use Case 6)
        RoomAllocationService allocator = new RoomAllocationService();

        // Process queue in FIFO
        while (bookingQueue.hasPendingRequests()) {
            Reservation request = bookingQueue.getNextRequest();
            allocator.allocateRoom(request, inventory);
        }
    }
}

/* ============================================================
 * CLASS - Reservation
 * ============================================================ */
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/* ============================================================
 * CLASS - BookingRequestQueue (FIFO)
 * ============================================================ */
class BookingRequestQueue {

    private Queue<Reservation> requestQueue = new LinkedList<>();

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}

/* ============================================================
 * CLASS - RoomInventory
 * ============================================================ */
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 2);
        inventory.put("Double", 2);
        inventory.put("Suite", 1);
    }

    public int getAvailability(String type) {
        return inventory.getOrDefault(type, 0);
    }

    public void updateAvailability(String type, int change) {
        inventory.put(type, getAvailability(type) + change);
    }
}

/* ============================================================
 * CLASS - RoomAllocationService
 * ============================================================
 *
 * @version 6.0
 */
class RoomAllocationService {

    // Prevent duplicate room IDs globally
    private Set<String> allocatedRoomIds;

    // Track rooms per type
    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String roomType = reservation.getRoomType();

        // Check availability
        if (inventory.getAvailability(roomType) <= 0) {
            System.out.println("No rooms available for " + reservation.getGuestName());
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(roomType);

        // Track allocation
        allocatedRoomIds.add(roomId);

        assignedRoomsByType
                .computeIfAbsent(roomType, k -> new HashSet<>())
                .add(roomId);

        // Update inventory (IMPORTANT)
        inventory.updateAvailability(roomType, -1);

        // Confirm booking
        System.out.println("Booking confirmed for Guest: "
                + reservation.getGuestName()
                + ", Room ID: "
                + roomId);
    }

    // Generate unique ID like Single-1, Single-2
    private String generateRoomId(String roomType) {

        int count = assignedRoomsByType
                .getOrDefault(roomType, new HashSet<>())
                .size() + 1;

        String roomId = roomType + "-" + count;

        // Safety check (extra layer)
        while (allocatedRoomIds.contains(roomId)) {
            count++;
            roomId = roomType + "-" + count;
        }

        return roomId;
    }
}