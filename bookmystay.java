import java.util.*;

/**
 * ============================================================
 * APPLICATION - bookmystay
 * ============================================================
 *
 * Use Case 11: Concurrent Booking Simulation
 *
 * @version 11.0
 */
public class bookmystay {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation\n");

        // Shared resources
        BookingRequestQueue bookingQueue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();

        // Add booking requests
        bookingQueue.addRequest(new Reservation("Aadi", "Single"));
        bookingQueue.addRequest(new Reservation("Rahul", "Single"));
        bookingQueue.addRequest(new Reservation("Priya", "Suite"));

        // Create threads
        Thread t1 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService)
        );

        Thread t2 = new Thread(
                new ConcurrentBookingProcessor(bookingQueue, inventory, allocationService)
        );

        // Start threads
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.println("Thread execution interrupted.");
        }

        System.out.println("\nProcessing complete.");
    }
}

/* ============================================================
 * CLASS - ConcurrentBookingProcessor
 * ============================================================ */
class ConcurrentBookingProcessor implements Runnable {

    private BookingRequestQueue bookingQueue;
    private RoomInventory inventory;
    private RoomAllocationService allocationService;

    public ConcurrentBookingProcessor(
            BookingRequestQueue bookingQueue,
            RoomInventory inventory,
            RoomAllocationService allocationService
    ) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
        this.allocationService = allocationService;
    }

    @Override
    public void run() {

        while (true) {

            Reservation reservation;

            // CRITICAL SECTION 1: Queue access
            synchronized (bookingQueue) {
                if (!bookingQueue.hasPendingRequests()) {
                    break;
                }
                reservation = bookingQueue.getNextRequest();
            }

            // CRITICAL SECTION 2: Allocation + inventory
            synchronized (inventory) {
                allocationService.allocateRoom(reservation, inventory);
            }
        }
    }
}

/* ============================================================
 * SUPPORTING CLASSES
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

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }

    public boolean hasPendingRequests() {
        return !queue.isEmpty();
    }
}

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

class RoomAllocationService {

    private Set<String> allocatedRoomIds = new HashSet<>();
    private Map<String, Set<String>> assignedRoomsByType = new HashMap<>();

    public void allocateRoom(Reservation reservation, RoomInventory inventory) {

        String type = reservation.getRoomType();

        if (inventory.getAvailability(type) <= 0) {
            System.out.println("No rooms available for " + reservation.getGuestName());
            return;
        }

        String roomId = generateRoomId(type);

        allocatedRoomIds.add(roomId);

        assignedRoomsByType
                .computeIfAbsent(type, k -> new HashSet<>())
                .add(roomId);

        inventory.updateAvailability(type, -1);

        System.out.println(Thread.currentThread().getName()
                + " -> Booking confirmed for "
                + reservation.getGuestName()
                + " | Room ID: " + roomId);
    }

    private String generateRoomId(String type) {
        int count = assignedRoomsByType
                .getOrDefault(type, new HashSet<>())
                .size() + 1;

        String id = type + "-" + count;

        while (allocatedRoomIds.contains(id)) {
            count++;
            id = type + "-" + count;
        }

        return id;
    }
}