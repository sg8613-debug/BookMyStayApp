import java.util.ArrayList;
import java.util.List;

/**
 * BookMyStayApp
 * Use Case 8: Booking History & Reporting
 *
 * @version 8.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("        Book My Stay App");
        System.out.println(" Use Case 8: Booking History Report");
        System.out.println("======================================");

        BookingHistory bookingHistory = new BookingHistory();

        Reservation r1 = new Reservation("RES101", "Shourya", "Single Room", "S1");
        Reservation r2 = new Reservation("RES102", "Aman", "Double Room", "D1");
        Reservation r3 = new Reservation("RES103", "Riya", "Suite Room", "SU1");
        Reservation r4 = new Reservation("RES104", "Karan", "Single Room", "S2");

        bookingHistory.addBooking(r1);
        bookingHistory.addBooking(r2);
        bookingHistory.addBooking(r3);
        bookingHistory.addBooking(r4);

        System.out.println("\n===== BOOKING HISTORY =====");
        bookingHistory.displayHistory();

        BookingReportService reportService = new BookingReportService(bookingHistory);

        System.out.println("\n===== BOOKING REPORT =====");
        reportService.generateSummaryReport();
    }
}


// ---------------- RESERVATION ----------------
class Reservation {
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
class BookingHistory {
    private List<Reservation> bookingList;

    public BookingHistory() {
        bookingList = new ArrayList<>();
    }

    public void addBooking(Reservation reservation) {
        bookingList.add(reservation);
        System.out.println(reservation.getReservationId() + " added to booking history.");
    }

    public List<Reservation> getAllBookings() {
        return bookingList;
    }

    public void displayHistory() {
        if (bookingList.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        int count = 1;
        for (Reservation reservation : bookingList) {
            System.out.println("\nBooking " + count);
            reservation.displayReservation();
            count++;
        }
    }
}


// ---------------- REPORT SERVICE ----------------
class BookingReportService {
    private BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public void generateSummaryReport() {
        List<Reservation> bookings = bookingHistory.getAllBookings();

        if (bookings.isEmpty()) {
            System.out.println("No data available for report.");
            return;
        }

        int totalBookings = bookings.size();
        int singleCount = 0;
        int doubleCount = 0;
        int suiteCount = 0;

        for (Reservation reservation : bookings) {
            if (reservation.getRoomType().equals("Single Room")) {
                singleCount++;
            } else if (reservation.getRoomType().equals("Double Room")) {
                doubleCount++;
            } else if (reservation.getRoomType().equals("Suite Room")) {
                suiteCount++;
            }
        }

        System.out.println("Total Confirmed Bookings : " + totalBookings);
        System.out.println("Single Room Bookings     : " + singleCount);
        System.out.println("Double Room Bookings     : " + doubleCount);
        System.out.println("Suite Room Bookings      : " + suiteCount);
    }
}