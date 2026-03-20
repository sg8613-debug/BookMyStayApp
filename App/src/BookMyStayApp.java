import java.util.*;

/**
 * BookMyStayApp
 * Use Case 7: Add-On Service Selection
 *
 * @version 7.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("        Book My Stay App");
        System.out.println(" Use Case 7: Add-On Service Selection");
        System.out.println("======================================");

        // Existing confirmed reservation IDs
        String reservationId1 = "RES101";
        String reservationId2 = "RES102";

        // Add-on service manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // Guest selects services for reservation 1
        serviceManager.addService(reservationId1, new AddOnService("Breakfast", 500));
        serviceManager.addService(reservationId1, new AddOnService("Airport Pickup", 1200));
        serviceManager.addService(reservationId1, new AddOnService("Extra Bed", 800));

        // Guest selects services for reservation 2
        serviceManager.addService(reservationId2, new AddOnService("Spa Access", 1500));
        serviceManager.addService(reservationId2, new AddOnService("Dinner", 900));

        // Display selected services
        System.out.println("\n===== SELECTED SERVICES =====");
        serviceManager.displayServices(reservationId1);
        serviceManager.displayServices(reservationId2);

        // Display total additional cost
        System.out.println("\n===== ADDITIONAL COST =====");
        System.out.println(reservationId1 + " -> ₹" + serviceManager.calculateTotalServiceCost(reservationId1));
        System.out.println(reservationId2 + " -> ₹" + serviceManager.calculateTotalServiceCost(reservationId2));

        System.out.println("\nCore booking and inventory remain unchanged.");
    }
}


// ---------------- ADD-ON SERVICE ----------------
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    public void displayService() {
        System.out.println(serviceName + " - ₹" + cost);
    }
}


// ---------------- ADD-ON SERVICE MANAGER ----------------
class AddOnServiceManager {

    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {
        reservationServices.putIfAbsent(reservationId, new ArrayList<>());
        reservationServices.get(reservationId).add(service);

        System.out.println(service.getServiceName() + " added to reservation " + reservationId);
    }

    public void displayServices(String reservationId) {
        System.out.println("\nReservation ID: " + reservationId);

        List<AddOnService> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        for (AddOnService service : services) {
            service.displayService();
        }
    }

    public double calculateTotalServiceCost(String reservationId) {
        List<AddOnService> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            return 0;
        }

        double total = 0;
        for (AddOnService service : services) {
            total += service.getCost();
        }
        return total;
    }
}