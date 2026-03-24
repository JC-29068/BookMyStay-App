import java.util.LinkedList;
import java.util.Queue;

public class UseCase5BookingRequestQueue {

    public static void main(String[] args) {
        Queue<Reservation> bookingRequestQueue = new LinkedList<>();

        bookingRequestQueue.offer(new Reservation("R001", "Aarav Sharma", "Single Room", 2));
        bookingRequestQueue.offer(new Reservation("R002", "Diya Mehta", "Double Room", 3));
        bookingRequestQueue.offer(new Reservation("R003", "Kabir Nair", "Suite Room", 1));
        bookingRequestQueue.offer(new Reservation("R004", "Ananya Iyer", "Double Room", 2));

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: 5.0");
        System.out.println();
        System.out.println("Booking Request Queue");
        System.out.println("---------------------");

        displayBookingRequests(bookingRequestQueue);
    }

    private static void displayBookingRequests(Queue<Reservation> bookingRequestQueue) {
        if (bookingRequestQueue.isEmpty()) {
            System.out.println("No booking requests available.");
            return;
        }

        int position = 1;
        for (Reservation reservation : bookingRequestQueue) {
            System.out.println("Queue Position : " + position);
            System.out.println("Request ID     : " + reservation.getRequestId());
            System.out.println("Guest Name     : " + reservation.getGuestName());
            System.out.println("Room Type      : " + reservation.getRoomType());
            System.out.println("Nights         : " + reservation.getNumberOfNights());
            System.out.println();
            position++;
        }
    }
}

class Reservation {
    private final String requestId;
    private final String guestName;
    private final String roomType;
    private final int numberOfNights;

    public Reservation(String requestId, String guestName, String roomType, int numberOfNights) {
        this.requestId = requestId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.numberOfNights = numberOfNights;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }
}
