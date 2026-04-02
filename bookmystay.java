import java.util.*;

/**
 * ============================================================
 * APPLICATION - bookmystay
 * ============================================================
 *
 * Use Case 9: Error Handling & Validation
 *
 * @version 9.0
 */
public class bookmystay {

    public static void main(String[] args) {

        System.out.println("Booking Validation\n");

        Scanner scanner = new Scanner(System.in);

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        try {
            // Input from user
            System.out.print("Enter Guest Name: ");
            String guestName = scanner.nextLine();

            System.out.print("Enter Room Type (Single/Double/Suite): ");
            String roomType = scanner.nextLine();

            // Validation (FAIL FAST)
            validator.validate(guestName, roomType, inventory);

            // If valid → add to queue
            Reservation reservation = new Reservation(guestName, roomType);
            bookingQueue.addRequest(reservation);

            System.out.println("Booking request accepted.");

        } catch (InvalidBookingException e) {
            // Graceful failure
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}

/* ============================================================
 * CLASS - InvalidBookingException
 * ============================================================ */
class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

/* ============================================================
 * CLASS - ReservationValidator
 * ============================================================ */
class ReservationValidator {

    public void validate(
            String guestName,
            String roomType,
            RoomInventory inventory
    ) throws InvalidBookingException {

        // Validate guest name
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Validate room type
        if (!roomType.equals("Single") &&
            !roomType.equals("Double") &&
            !roomType.equals("Suite")) {

            throw new InvalidBookingException("Invalid room type selected.");
        }

        // Validate availability
        if (inventory.getAvailability(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for selected type.");
        }
    }
}

/* ============================================================
 * SUPPORTING CLASSES (Minimal required)
 * ============================================================ */

class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
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
}