import java.util.LinkedList;
import java.util.Queue;

/**
 * BookMyStayApp
 * Use Case 11: Concurrent Booking Simulation (Thread Safety)
 *
 * @version 11.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("           Book My Stay App");
        System.out.println(" Use Case 11: Concurrent Booking Simulation");
        System.out.println("==========================================");

        SharedBookingQueue bookingQueue = new SharedBookingQueue();
        RoomInventory inventory = new RoomInventory();
        ConcurrentBookingProcessor processor = new ConcurrentBookingProcessor(inventory);

        bookingQueue.addRequest(new Reservation("Shourya", "Single Room"));
        bookingQueue.addRequest(new Reservation("Aman", "Single Room"));
        bookingQueue.addRequest(new Reservation("Riya", "Double Room"));
        bookingQueue.addRequest(new Reservation("Karan", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Neha", "Suite Room"));
        bookingQueue.addRequest(new Reservation("Rahul", "Double Room"));

        Thread t1 = new Thread(new BookingWorker("Worker-1", bookingQueue, processor));
        Thread t2 = new Thread(new BookingWorker("Worker-2", bookingQueue, processor));
        Thread t3 = new Thread(new BookingWorker("Worker-3", bookingQueue, processor));

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted.");
        }

        System.out.println("\n===== FINAL INVENTORY =====");
        inventory.displayInventory();
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


// ---------------- SHARED BOOKING QUEUE ----------------
class SharedBookingQueue {
    private Queue<Reservation> queue;

    public SharedBookingQueue() {
        queue = new LinkedList<>();
    }

    public synchronized void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }
}


// ---------------- ROOM INVENTORY ----------------
class RoomInventory {
    private int singleRooms;
    private int doubleRooms;
    private int suiteRooms;

    public RoomInventory() {
        singleRooms = 1;
        doubleRooms = 2;
        suiteRooms = 1;
    }

    public synchronized boolean allocateRoom(String roomType, String guestName) {
        if (roomType.equals("Single Room")) {
            if (singleRooms > 0) {
                singleRooms--;
                System.out.println(guestName + " booked Single Room successfully.");
                return true;
            }
        } else if (roomType.equals("Double Room")) {
            if (doubleRooms > 0) {
                doubleRooms--;
                System.out.println(guestName + " booked Double Room successfully.");
                return true;
            }
        } else if (roomType.equals("Suite Room")) {
            if (suiteRooms > 0) {
                suiteRooms--;
                System.out.println(guestName + " booked Suite Room successfully.");
                return true;
            }
        }

        System.out.println(guestName + " booking failed for " + roomType + " (No availability)");
        return false;
    }

    public void displayInventory() {
        System.out.println("Single Room -> " + singleRooms + " left");
        System.out.println("Double Room -> " + doubleRooms + " left");
        System.out.println("Suite Room  -> " + suiteRooms + " left");
    }
}


// ---------------- CONCURRENT BOOKING PROCESSOR ----------------
class ConcurrentBookingProcessor {
    private RoomInventory inventory;

    public ConcurrentBookingProcessor(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void process(Reservation reservation) {
        if (reservation != null) {
            inventory.allocateRoom(reservation.getRoomType(), reservation.getGuestName());
        }
    }
}


// ---------------- BOOKING WORKER ----------------
class BookingWorker implements Runnable {
    private String workerName;
    private SharedBookingQueue queue;
    private ConcurrentBookingProcessor processor;

    public BookingWorker(String workerName, SharedBookingQueue queue, ConcurrentBookingProcessor processor) {
        this.workerName = workerName;
        this.queue = queue;
        this.processor = processor;
    }

    public void run() {
        while (true) {
            Reservation reservation = queue.getNextRequest();

            if (reservation == null) {
                break;
            }

            System.out.println(workerName + " processing request of " + reservation.getGuestName());
            processor.process(reservation);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println(workerName + " interrupted.");
            }
        }
    }
}