import java.util.HashMap;
import java.util.Map;

/**
 * BookMyStayApp
 * Use Case 3: Centralized Room Inventory Management
 *
 * @author Shourya
 * @version 3.1
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("        Book My Stay App");
        System.out.println(" Use Case 3: Room Inventory");
        System.out.println("======================================");

        // Create room objects
        Room single = new SingleRoom();
        Room doubleroom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Display room details
        System.out.println("\n--- ROOM DETAILS ---");
        single.displayRoomDetails();
        System.out.println();

        doubleroom.displayRoomDetails();
        System.out.println();

        suite.displayRoomDetails();

        // Create inventory
        RoomInventory inventory = new RoomInventory();

        // Display inventory
        inventory.displayInventory();

        // Check availability
        System.out.println("\nSingle Room Available: " +
                inventory.getAvailability("Single Room"));

        // Update inventory
        inventory.updateAvailability("Single Room", 8);

        System.out.println("\nAfter Update:");
        inventory.displayInventory();
    }
}

// ABSTRACT CLASS
abstract class Room {
    String roomType;
    int beds;
    int size;
    double price;

    public Room(String roomType, int beds, int size, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.size = size;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: ₹" + price);
    }

    public String getRoomType() {
        return roomType;
    }
}

// SINGLE ROOM
class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200, 3000);
    }
}

// DOUBLE ROOM
class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350, 5000);
    }
}

// SUITE ROOM
class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 500, 8000);
    }
}

// ROOM INVENTORY (MAIN UC3 PART)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        // Initialize availability
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 7);
        inventory.put("Suite Room", 3);
    }

    // Get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    // Display inventory
    public void displayInventory() {
        System.out.println("\n===== ROOM INVENTORY =====");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " rooms");
        }
    }
}