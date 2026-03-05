/**
 * Hotel Booking Application
 * This class represents the entry point of the Hotel Booking System.
 * It starts the application and displays a welcome message.
 *
 * @author Shourya
 * @version 1.0
 */

/**
 * BookMyStayApp
 * Demonstrates Room Types and Static Availability
 *
 * @author Shourya
 * @version 2.1
 */

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
        super("Suite Room", 3, 600, 9000);
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("Welcome to BookMyStayApp v2.1");
        System.out.println("------------------------------");

        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        single.displayRoomDetails();
        System.out.println("Available Rooms: " + singleAvailable);
        System.out.println();

        doubleRoom.displayRoomDetails();
        System.out.println("Available Rooms: " + doubleAvailable);
        System.out.println();

        suite.displayRoomDetails();
        System.out.println("Available Rooms: " + suiteAvailable);
    }
}