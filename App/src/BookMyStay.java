import java.util.ArrayList;
import java.util.List;

public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {
        BookingHistory bookingHistory = new BookingHistory();

        bookingHistory.addConfirmedReservation(new Reservation("R001", "Aarav Sharma", "Single Room", "SR-001", 2, 2400.0));
        bookingHistory.addConfirmedReservation(new Reservation("R002", "Diya Mehta", "Double Room", "DR-001", 3, 6600.0));
        bookingHistory.addConfirmedReservation(new Reservation("R003", "Kabir Nair", "Suite Room", "SU-001", 1, 4500.0));
        bookingHistory.addConfirmedReservation(new Reservation("R004", "Ananya Iyer", "Single Room", "SR-002", 2, 2400.0));

        BookingReportService reportService = new BookingReportService(bookingHistory);

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: 8.0");
        System.out.println();
        System.out.println("Booking History");
        System.out.println("---------------");
        reportService.displayBookingHistory();
        System.out.println("Booking Summary Report");
        System.out.println("----------------------");
        reportService.displaySummaryReport();
    }
}

class Reservation {
    private final String reservationId;
    private final String guestName;
    private final String roomType;
    private final String assignedRoomId;
    private final int numberOfNights;
    private final double totalAmount;

    public Reservation(String reservationId, String guestName, String roomType, String assignedRoomId, int numberOfNights, double totalAmount) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.assignedRoomId = assignedRoomId;
        this.numberOfNights = numberOfNights;
        this.totalAmount = totalAmount;
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

    public String getAssignedRoomId() {
        return assignedRoomId;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}

class BookingHistory {
    private final List<Reservation> confirmedReservations;

    public BookingHistory() {
        this.confirmedReservations = new ArrayList<>();
    }

    public void addConfirmedReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    public List<Reservation> getConfirmedReservations() {
        return new ArrayList<>(confirmedReservations);
    }
}

class BookingReportService {
    private final BookingHistory bookingHistory;

    public BookingReportService(BookingHistory bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public void displayBookingHistory() {
        List<Reservation> reservations = bookingHistory.getConfirmedReservations();

        if (reservations.isEmpty()) {
            System.out.println("No confirmed bookings found.");
            System.out.println();
            return;
        }

        for (Reservation reservation : reservations) {
            System.out.println("Reservation ID   : " + reservation.getReservationId());
            System.out.println("Guest Name       : " + reservation.getGuestName());
            System.out.println("Room Type        : " + reservation.getRoomType());
            System.out.println("Assigned Room ID : " + reservation.getAssignedRoomId());
            System.out.println("Nights           : " + reservation.getNumberOfNights());
            System.out.println("Total Amount     : Rs. " + reservation.getTotalAmount());
            System.out.println();
        }
    }

    public void displaySummaryReport() {
        List<Reservation> reservations = bookingHistory.getConfirmedReservations();

        int totalBookings = reservations.size();
        int totalNights = 0;
        double totalRevenue = 0.0;

        for (Reservation reservation : reservations) {
            totalNights += reservation.getNumberOfNights();
            totalRevenue += reservation.getTotalAmount();
        }

        System.out.println("Total Confirmed Bookings : " + totalBookings);
        System.out.println("Total Nights Booked      : " + totalNights);
        System.out.println("Total Revenue            : Rs. " + totalRevenue);
    }
}
