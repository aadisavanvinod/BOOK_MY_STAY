import java.util.*;

/**
 * ============================================================
 * APPLICATION - bookmystay
 * ============================================================
 *
 * Use Case 7: Add-On Service Selection
 *
 * @version 7.0
 */
public class bookmystay {

    public static void main(String[] args) {

        System.out.println("Add-On Service Selection\n");

        // Assume reservation IDs from allocation system
        String reservationId1 = "Single-1";
        String reservationId2 = "Suite-1";

        // Initialize service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Create services
        Service wifi = new Service("WiFi", 200.0);
        Service breakfast = new Service("Breakfast", 500.0);
        Service spa = new Service("Spa", 1500.0);

        // Attach services
        serviceManager.addService(reservationId1, wifi);
        serviceManager.addService(reservationId1, breakfast);

        serviceManager.addService(reservationId2, spa);

        // Calculate cost
        System.out.println("Total Add-On Cost for " + reservationId1 + ": "
                + serviceManager.calculateTotalServiceCost(reservationId1));

        System.out.println("Total Add-On Cost for " + reservationId2 + ": "
                + serviceManager.calculateTotalServiceCost(reservationId2));
    }
}

/* ============================================================
 * CLASS - Service
 * ============================================================ */
class Service {

    private String serviceName;
    private double cost;

    public Service(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

/* ============================================================
 * CLASS - AddOnServiceManager
 * ============================================================
 *
 * @version 7.0
 */
class AddOnServiceManager {

    // Map: Reservation ID → List of Services
    private Map<String, List<Service>> servicesByReservation;

    public AddOnServiceManager() {
        servicesByReservation = new HashMap<>();
    }

    // Attach service to reservation
    public void addService(String reservationId, Service service) {

        servicesByReservation
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added " + service.getServiceName()
                + " to reservation " + reservationId);
    }

    // Calculate total cost
    public double calculateTotalServiceCost(String reservationId) {

        List<Service> services = servicesByReservation.get(reservationId);

        if (services == null) return 0.0;

        double total = 0;

        for (Service s : services) {
            total += s.getCost();
        }

        return total;
    }
}