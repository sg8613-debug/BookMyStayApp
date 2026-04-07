import java.util.*;

/**
 * BookMyStayApp
 * Use Case 10: Booking Cancellation & Inventory Rollback
 *
 * @version 10.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("        Book My Stay App");
        System.out.println(" Use Case 10: Booking Cancellation");
        System.out.println("======================================");

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService(inventory, history);

        Reservation r1 = new Reservation("RES101", "Shourya", "Single Room", "S1");
        Reservation r2 = new Reservation("RES102", "Aman", "Double Room", "D1");
        Reservation r3 = new Reservation("RES103", "Riya", "Suite Room", "SU1");

        history.addBooking(r1);
        history.addBooking(r2);
        history.addBooking(r3);

        inventory.decrementAvailability("Single Room");
        inventory.decrementAvailability("Double Room");
        inventory.decrementAvailability("Suite Room");

        System.out.println("\n===== CURRENT BOOKINGS =====");
        history.displayHistory();

        System.out.println("\n===== CURRENT INVENTORY =====");
        inventory.displayInventory();

        System.out.println("\n===== CANCELLATION PROCESS =====");
        cancellationService.cancelBooking("RES102");
        cancellationService.cancelBooking("RES999");
        cancellationService.cancelBooking("RES102");

        System.out.println("\n===== UPDATED BOOKINGS =====");
        history.displayHistory();

        System.out.println("\n===== UPDATED INVENTORY =====");
        inventory.displayInventory();

        System.out.println("\n===== ROLLBACK STACK =====");
        cancellationService.displayRollbackStack();
    }
}


// ---------------- RESERVATION ----------------
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean cancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.cancelled = false;
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

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        cancelled = true;
    }

    public void displayReservation() {
        System.out.println("Reservation ID : " + reservationId);
        System.out.println("Guest Name     : " + guestName);
        System.out.println("Room Type      : " + roomType);
        System.out.println("Assigned Room  : " + roomId);
        System.out.println("Status         : " + (cancelled ? "Cancelled" : "Confirmed"));
    }
}


// ---------------- BOOKING HISTORY ----------------
class BookingHistory {
    private List<Reservation> bookingList;

    public BookingHistory() {
        bookingList = new ArrayList<>();
    }

    public void addBooking(Reservation reservation) {
        bookingList.add(reservation);
    }

    public Reservation findBookingById(String reservationId) {
        for (Reservation reservation : bookingList) {
            if (reservation.getReservationId().equals(reservationId)) {
                return reservation;
            }
        }
        return null;
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
class RoomInventory {
    private HashMap<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    public void decrementAvailability(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        if (count > 0) {
            inventory.put(roomType, count - 1);
        }
    }

    public void incrementAvailability(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, count + 1);
    }

    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " rooms available");
        }
    }
}


// ---------------- CANCELLATION SERVICE ----------------
class CancellationService {
    private RoomInventory inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId) {
        Reservation reservation = history.findBookingById(reservationId);

        if (reservation == null) {
            System.out.println("Cancellation Failed: Reservation " + reservationId + " does not exist.");
            return;
        }

        if (reservation.isCancelled()) {
            System.out.println("Cancellation Failed: Reservation " + reservationId + " is already cancelled.");
            return;
        }

        rollbackStack.push(reservation.getRoomId());
        inventory.incrementAvailability(reservation.getRoomType());
        reservation.cancel();

        System.out.println("Cancellation Successful for " + reservationId +
                " | Released Room ID: " + reservation.getRoomId());
    }

    public void displayRollbackStack() {
        if (rollbackStack.isEmpty()) {
            System.out.println("No released room IDs.");
            return;
        }

        for (int i = rollbackStack.size() - 1; i >= 0; i--) {
            System.out.println(rollbackStack.get(i));
        }
    }
}