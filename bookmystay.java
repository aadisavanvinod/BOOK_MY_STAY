import java.util.*;

/**
 * ============================================================
 * APPLICATION - bookmystay
 * ============================================================
 *
 * Use Case 8: Booking History & Reporting
 *
 * @version 8.0
 */
public class bookmystay {

    public static void main(String[] args) {

        System.out.println("Booking History & Reporting\n");

        // Simulate confirmed reservations (from allocation system)
        Reservation r1 = new Reservation("Abhi", "Single");
        Reservation r2 = new Reservation("Subha", "Double");
        Reservation r3 = new Reservation("Vanmathi", "Suite");

        // Booking History (stores confirmed bookings)
        BookingHistory history = new BookingHistory();

        history.addReservation(r1);
        history.addReservation(r2);
        history.addReservation(r3);

        // Reporting Service
        BookingReportService reportService = new BookingReportService();

        // Generate report (read-only)
        reportService.generateReport(history);
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
 * CLASS - BookingHistory
 * ============================================================
 *
 * Stores confirmed reservations in order
 *
 * @version 8.0
 */
class BookingHistory {

    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        confirmedReservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

/* ============================================================
 * CLASS - BookingReportService
 * ============================================================
 *
 * Generates reports from booking history
 *
 * @version 8.0
 */
class BookingReportService {

    public void generateReport(BookingHistory history) {

        System.out.println("=== Booking Report ===\n");

        List<Reservation> reservations = history.getConfirmedReservations();

        int total = reservations.size();

        // Count by room type
        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {

            System.out.println("Guest: " + r.getGuestName()
                    + ", Room Type: " + r.getRoomType());

            roomTypeCount.put(
                    r.getRoomType(),
                    roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("\nTotal Bookings: " + total);

        System.out.println("\nBookings by Room Type:");
        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + ": " + roomTypeCount.get(type));
        }
    }
}