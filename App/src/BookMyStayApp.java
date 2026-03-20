import java.util.LinkedList;
import java.util.Queue;

/**
 * BookMyStayApp
 * Use Case 5: Booking Request (First-Come-First-Served)
 *
 * @version 5.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("        Book My Stay App");
        System.out.println(" Use Case 5: Booking Request Queue");
        System.out.println("======================================");

        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Guest booking requests added in arrival order
        bookingQueue.addRequest(new Reservation("Shourya", "Single Room"));
        bookingQueue.addRequest(new Reservation("Aman", "Double Room"));
        bookingQueue.addRequest(new Reservation("Riya", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Karan", "Single Room"));

        // Display queue
        bookingQueue.displayQueue();

        System.out.println("\nRequests are stored in FIFO order.");
        System.out.println("No inventory update or allocation is done at this stage.");
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

    public void displayReservation() {
        System.out.println("Guest Name     : " + guestName);
        System.out.println("Requested Room : " + roomType);
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

    public void displayQueue() {
        System.out.println("\n===== BOOKING REQUEST QUEUE =====");

        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        int position = 1;
        for (Reservation reservation : requestQueue) {
            System.out.println("\nRequest Position: " + position);
            reservation.displayReservation();
            position++;
        }
    }
}