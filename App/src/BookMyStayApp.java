import java.util.HashMap;
import java.util.Map;

/**
 * BookMyStayApp
 * Use Case 9: Error Handling & Validation
 *
 * @version 9.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("        Book My Stay App");
        System.out.println(" Use Case 9: Error Handling");
        System.out.println("======================================");

        RoomInventory inventory = new RoomInventory();

        try {
            processBooking("Shourya", "Single Room", inventory);
        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }

        try {
            processBooking("Aman", "Luxury Room", inventory);
        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }

        try {
            processBooking("Riya", "Suite Room", inventory);
            processBooking("Karan", "Suite Room", inventory);
        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }

        try {
            inventory.forceInvalidUpdate("Double Room", -5);
        } catch (InvalidBookingException e) {
            System.out.println("Inventory Error: " + e.getMessage());
        }

        System.out.println("\n===== FINAL INVENTORY =====");
        inventory.displayInventory();
    }

    public static void processBooking(String guestName, String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        InvalidBookingValidator.validateRoomType(roomType, inventory);
        InvalidBookingValidator.validateAvailability(roomType, inventory);

        inventory.decrementAvailability(roomType);

        System.out.println("Booking Confirmed for " + guestName + " | Room Type: " + roomType);
    }
}


// ---------------- CUSTOM EXCEPTION ----------------
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}


// ---------------- VALIDATOR ----------------
class InvalidBookingValidator {

    public static void validateRoomType(String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        if (!inventory.isValidRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }
    }

    public static void validateAvailability(String roomType, RoomInventory inventory)
            throws InvalidBookingException {

        if (inventory.getAvailability(roomType) <= 0) {
            throw new InvalidBookingException("No rooms available for: " + roomType);
        }
    }

    public static void validateInventoryValue(int value)
            throws InvalidBookingException {

        if (value < 0) {
            throw new InvalidBookingException("Inventory cannot be negative.");
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

    public boolean isValidRoomType(String roomType) {
        return inventory.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementAvailability(String roomType) throws InvalidBookingException {
        int count = getAvailability(roomType);

        if (count <= 0) {
            throw new InvalidBookingException("Cannot reduce inventory below zero for " + roomType);
        }

        inventory.put(roomType, count - 1);
    }

    public void forceInvalidUpdate(String roomType, int value) throws InvalidBookingException {
        InvalidBookingValidator.validateInventoryValue(value);
        inventory.put(roomType, value);
    }

    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue() + " rooms left");
        }
    }
}