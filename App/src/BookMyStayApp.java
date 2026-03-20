import java.util.HashMap;
import java.util.Map;

/**
 * BookMyStayApp
 * Use Case 4: Room Search & Availability Check
 *
 * @version 4.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("        Book My Stay App");
        System.out.println(" Use Case 4: Room Search");
        System.out.println("======================================");

        // Room objects
        Room single = new SingleRoom();
        Room doubleroom = new DoubleRoom();
        Room suite = new SuiteRoom();

        Room[] rooms = {single, doubleroom, suite};

        // Inventory
        RoomInventory inventory = new RoomInventory();

        // Search Service
        SearchService searchService = new SearchService(inventory, rooms);

        // Show available rooms
        searchService.displayAvailableRooms();

        System.out.println("\n(Search is read-only, inventory unchanged)");
    }
}


// ---------------- ROOM CLASSES ----------------

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

    public String getRoomType() {
        return roomType;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Beds: " + beds);
        System.out.println("Size: " + size + " sq ft");
        System.out.println("Price: ₹" + price);
    }
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single Room", 1, 200, 3000);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double Room", 2, 350, 5000);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite Room", 3, 500, 8000);
    }
}


// ---------------- INVENTORY ----------------

class RoomInventory {

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        inventory.put("Single Room", 10);
        inventory.put("Double Room", 5);
        inventory.put("Suite Room", 0); // not available
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }
}


// ---------------- SEARCH SERVICE ----------------

class SearchService {

    private RoomInventory inventory;
    private Room[] rooms;

    public SearchService(RoomInventory inventory, Room[] rooms) {
        this.inventory = inventory;
        this.rooms = rooms;
    }

    public void displayAvailableRooms() {

        System.out.println("\n===== AVAILABLE ROOMS =====");

        boolean found = false;

        for (Room room : rooms) {

            int available = inventory.getAvailability(room.getRoomType());

            // Only show available rooms
            if (available > 0) {
                room.displayRoomDetails();
                System.out.println("Available: " + available);
                System.out.println("------------------------");
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms available.");
        }
    }
}