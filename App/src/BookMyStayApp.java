import java.io.*;
import java.util.*;

/**
 * BookMyStayApp
 * Use Case 12: Data Persistence & System Recovery
 *
 * @version 12.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("           Book My Stay App");
        System.out.println(" Use Case 12: Data Persistence & Recovery");
        System.out.println("==========================================");

        String fileName = "bookmystay_data.ser";

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();

        history.addBooking(new Reservation("RES101", "Shourya", "Single Room", "S1"));
        history.addBooking(new Reservation("RES102", "Aman", "Double Room", "D1"));
        history.addBooking(new Reservation("RES103", "Riya", "Suite Room", "SU1"));

        inventory.setAvailability("Single Room", 1);
        inventory.setAvailability("Double Room", 2);
        inventory.setAvailability("Suite Room", 0);

        SystemState state = new SystemState(inventory, history);

        System.out.println("\n===== CURRENT STATE BEFORE SAVE =====");
        state.displayState();

        PersistenceService.saveState(state, fileName);

        System.out.println("\n===== SIMULATING SYSTEM RESTART =====");

        SystemState recoveredState = PersistenceService.loadState(fileName);

        if (recoveredState != null) {
            System.out.println("\n===== RECOVERED STATE AFTER RESTART =====");
            recoveredState.displayState();
        } else {
            System.out.println("Recovery failed. Starting with default safe state.");
        }
    }
}


// ---------------- RESERVATION ----------------
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public void displayReservation() {
        System.out.println("Reservation ID : " + reservationId);
        System.out.println("Guest Name     : " + guestName);
        System.out.println("Room Type      : " + roomType);
        System.out.println("Assigned Room  : " + roomId);
    }
}


// ---------------- BOOKING HISTORY ----------------
class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> bookingList;

    public BookingHistory() {
        bookingList = new ArrayList<>();
    }

    public void addBooking(Reservation reservation) {
        bookingList.add(reservation);
    }

    public List<Reservation> getAllBookings() {
        return bookingList;
    }

    public void displayHistory() {
        if (bookingList.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (Reservation reservation : bookingList) {
            System.out.println();
            reservation.displayReservation();
        }
    }
}


// ---------------- ROOM INVENTORY ----------------
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 0);
        inventory.put("Double Room", 0);
        inventory.put("Suite Room", 0);
    }

    public void setAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " rooms available");
        }
    }
}


// ---------------- SYSTEM STATE ----------------
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    private RoomInventory inventory;
    private BookingHistory history;

    public SystemState(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public RoomInventory getInventory() {
        return inventory;
    }

    public BookingHistory getHistory() {
        return history;
    }

    public void displayState() {
        System.out.println("\n--- Inventory ---");
        inventory.displayInventory();

        System.out.println("\n--- Booking History ---");
        history.displayHistory();
    }
}


// ---------------- PERSISTENCE SERVICE ----------------
class PersistenceService {

    public static void saveState(SystemState state, String fileName) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName))) {
            out.writeObject(state);
            System.out.println("\nSystem state saved successfully to file: " + fileName);
        } catch (IOException e) {
            System.out.println("\nError while saving system state: " + e.getMessage());
        }
    }

    public static SystemState loadState(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            System.out.println("Persistence file not found. Starting with safe default state.");
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            SystemState state = (SystemState) in.readObject();
            System.out.println("System state loaded successfully from file: " + fileName);
            return state;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while loading system state: " + e.getMessage());
            return null;
        }
    }
}