import java.util.*;

/**
 * ============================================================
 * APPLICATION - bookmystay
 * ============================================================
 *
 * Use Case 5: Booking Request Queue (FIFO)
 *
 * @version 5.0
 */
public class bookmystay {

    public static void main(String[] args) {

        // Display header
        System.out.println("Booking Request Queue\n");

        // Initialize queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Create booking requests
        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Double");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        // Add requests to queue
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Process in FIFO order
        while (bookingQueue.hasPendingRequests()) {
            Reservation current = bookingQueue.getNextRequest();

            System.out.println(
                "Processing booking for Guest: " +
                current.getGuestName() +
                ", Room Type: " +
                current.getRoomType()
            );
        }
    }
}

/* ============================================================
 * CLASS - Reservation
 * ============================================================
 *
 * Represents a booking request
 *
 * @version 5.0
 */
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
 * CLASS - BookingRequestQueue
 * ============================================================
 *
 * FIFO Queue for booking requests
 *
 * @version 5.0
 */
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add request (enqueue)
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
    }

    // Get next request (dequeue)
    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    // Check if queue is not empty
    public boolean hasPendingRequests() {
        return !requestQueue.isEmpty();
    }
}