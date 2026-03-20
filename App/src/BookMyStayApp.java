import java.util.*;

/**
 * BookMyStayApp
 * Use Case 6: Reservation Confirmation & Room Allocation
 *
 * @version 6.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("        Book My Stay App");
        System.out.println(" Use Case 6: Room Allocation Service");
        System.out.println("======================================");

        // Inventory setup
        RoomInventory inventory = new RoomInventory();

        // Booking queue setup
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        bookingQueue.addRequest(new Reservation("Shourya", "Single Room"));
        bookingQueue.addRequest(new Reservation("Aman", "Double Room"));
        bookingQueue.addRequest(new Reservation("Riya", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Karan", "Single Room"));
        bookingQueue.addRequest(new Reservation("Neha", "Suite Room")); // should fail if no stock left

        // Allocation service
        BookingService bookingService = new BookingService(inventory);

        System.out.println("\n===== PROCESSING BOOKINGS =====");
        while (!bookingQueue.isEmpty()) {
            Reservation request = bookingQueue.getNextRequest();
            bookingService.confirmReservation(request);
        }

        System.out.println("\n===== FINAL INVENTORY =====");
        inventory.displayInventory();

        System.out.println("\n===== ALLOCATED ROOM IDS =====");
        bookingService.displayAllocatedRooms();
    }
}


// ---------------- RESERVATION ----------------
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


// ---------------- BOOKING REQUEST QUEUE ----------------
class BookingRequestQueue {
    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println(reservation.getGuestName() + " booking request added to queue.");
    }

    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    public boolean isEmpty() {
        return requestQueue.isEmpty();
    }
}


// ---------------- ROOM INVENTORY ----------------
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementAvailability(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        if (count > 0) {
            inventory.put(roomType, count - 1);
        }
    }

    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " rooms left");
        }
    }
}


// ---------------- BOOKING SERVICE ----------------
class BookingService {
    private RoomInventory inventory;

    // Stores all allocated room IDs globally to prevent reuse
    private Set<String> allocatedRoomIds;

    // Maps room type to set of allocated room IDs
    private HashMap<String, Set<String>> roomTypeAllocations;

    // Counters for generating unique IDs
    private HashMap<String, Integer> roomCounters;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRoomIds = new HashSet<>();
        this.roomTypeAllocations = new HashMap<>();
        this.roomCounters = new HashMap<>();
    }

    public void confirmReservation(Reservation reservation) {
        String roomType = reservation.getRoomType();
        String guestName = reservation.getGuestName();

        int available = inventory.getAvailability(roomType);

        if (available <= 0) {
            System.out.println("Reservation FAILED for " + guestName + " (" + roomType + ") - No rooms available.");
            return;
        }

        String roomId = generateUniqueRoomId(roomType);

        // Record room ID
        allocatedRoomIds.add(roomId);

        roomTypeAllocations.putIfAbsent(roomType, new HashSet<>());
        roomTypeAllocations.get(roomType).add(roomId);

        // Immediately decrement inventory
        inventory.decrementAvailability(roomType);

        System.out.println("Reservation CONFIRMED for " + guestName
                + " | Room Type: " + roomType
                + " | Assigned Room ID: " + roomId);
    }

    private String generateUniqueRoomId(String roomType) {
        String prefix;

        if (roomType.equals("Single Room")) {
            prefix = "S";
        } else if (roomType.equals("Double Room")) {
            prefix = "D";
        } else {
            prefix = "SU";
        }

        int nextNumber = roomCounters.getOrDefault(roomType, 0) + 1;
        String roomId = prefix + nextNumber;

        while (allocatedRoomIds.contains(roomId)) {
            nextNumber++;
            roomId = prefix + nextNumber;
        }

        roomCounters.put(roomType, nextNumber);
        return roomId;
    }

    public void displayAllocatedRooms() {
        for (Map.Entry<String, Set<String>> entry : roomTypeAllocations.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}