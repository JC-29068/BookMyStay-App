import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class UseCase10BookingCancellation {

    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        BookingHistory bookingHistory = new BookingHistory();

        ConfirmedReservation reservation1 = new ConfirmedReservation("R001", "Aarav Sharma", "Single Room", "SR-001", 2, 2400.0);
        ConfirmedReservation reservation2 = new ConfirmedReservation("R002", "Diya Mehta", "Double Room", "DR-001", 3, 6600.0);
        ConfirmedReservation reservation3 = new ConfirmedReservation("R003", "Kabir Nair", "Suite Room", "SU-001", 1, 4500.0);

        bookingHistory.addReservation(reservation1);
        bookingHistory.addReservation(reservation2);
        bookingHistory.addReservation(reservation3);

        inventory.reduceAvailability("Single Room");
        inventory.reduceAvailability("Double Room");
        inventory.reduceAvailability("Suite Room");

        CancellationService cancellationService = new CancellationService(inventory, bookingHistory);

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: 10.0");
        System.out.println();
        System.out.println("Initial Booking History");
        System.out.println("-----------------------");
        bookingHistory.displayAllReservations();

        System.out.println("Initial Inventory");
        System.out.println("-----------------");
        inventory.displayInventory();

        System.out.println("Processing Cancellations");
        System.out.println("------------------------");
        cancellationService.cancelReservation("R002");
        System.out.println();
        cancellationService.cancelReservation("R999");
        System.out.println();
        cancellationService.cancelReservation("R002");
        System.out.println();

        System.out.println("Updated Booking History");
        System.out.println("-----------------------");
        bookingHistory.displayAllReservations();

        System.out.println("Updated Inventory");
        System.out.println("-----------------");
        inventory.displayInventory();

        System.out.println("Rollback Stack");
        System.out.println("--------------");
        cancellationService.displayRollbackStack();
    }
}

class ConfirmedReservation {
    private final String reservationId;
    private final String guestName;
    private final String roomType;
    private final String assignedRoomId;
    private final int numberOfNights;
    private final double totalAmount;
    private boolean cancelled;

    public ConfirmedReservation(String reservationId, String guestName, String roomType, String assignedRoomId, int numberOfNights, double totalAmount) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.assignedRoomId = assignedRoomId;
        this.numberOfNights = numberOfNights;
        this.totalAmount = totalAmount;
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

    public String getAssignedRoomId() {
        return assignedRoomId;
    }

    public int getNumberOfNights() {
        return numberOfNights;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }
}

class RoomInventory {
    private final java.util.Map<String, Integer> roomAvailability;

    public RoomInventory() {
        this.roomAvailability = new java.util.HashMap<>();
    }

    public void addRoomType(String roomType, int availableCount) {
        if (availableCount >= 0) {
            roomAvailability.put(roomType, availableCount);
        }
    }

    public int getAvailability(String roomType) {
        Integer availableCount = roomAvailability.get(roomType);
        return availableCount != null ? availableCount : 0;
    }

    public boolean reduceAvailability(String roomType) {
        int currentAvailability = getAvailability(roomType);
        if (currentAvailability <= 0) {
            return false;
        }
        roomAvailability.put(roomType, currentAvailability - 1);
        return true;
    }

    public void increaseAvailability(String roomType) {
        int currentAvailability = getAvailability(roomType);
        roomAvailability.put(roomType, currentAvailability + 1);
    }

    public void displayInventory() {
        for (java.util.Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println("Room Type    : " + entry.getKey());
            System.out.println("Availability : " + entry.getValue());
            System.out.println();
        }
    }
}

class BookingHistory {
    private final List<ConfirmedReservation> reservations;

    public BookingHistory() {
        this.reservations = new ArrayList<>();
    }

    public void addReservation(ConfirmedReservation reservation) {
        reservations.add(reservation);
    }

    public ConfirmedReservation findReservationById(String reservationId) {
        for (ConfirmedReservation reservation : reservations) {
            if (reservation.getReservationId().equals(reservationId)) {
                return reservation;
            }
        }
        return null;
    }

    public void displayAllReservations() {
        if (reservations.isEmpty()) {
            System.out.println("No reservations found.");
            System.out.println();
            return;
        }

        for (ConfirmedReservation reservation : reservations) {
            System.out.println("Reservation ID   : " + reservation.getReservationId());
            System.out.println("Guest Name       : " + reservation.getGuestName());
            System.out.println("Room Type        : " + reservation.getRoomType());
            System.out.println("Assigned Room ID : " + reservation.getAssignedRoomId());
            System.out.println("Nights           : " + reservation.getNumberOfNights());
            System.out.println("Total Amount     : Rs. " + reservation.getTotalAmount());
            System.out.println("Status           : " + (reservation.isCancelled() ? "Cancelled" : "Confirmed"));
            System.out.println();
        }
    }
}

class CancellationService {
    private final RoomInventory inventory;
    private final BookingHistory bookingHistory;
    private final Stack<String> rollbackRoomIds;

    public CancellationService(RoomInventory inventory, BookingHistory bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
        this.rollbackRoomIds = new Stack<>();
    }

    public void cancelReservation(String reservationId) {
        ConfirmedReservation reservation = bookingHistory.findReservationById(reservationId);

        if (reservation == null) {
            System.out.println("Cancellation Failed");
            System.out.println("Reservation ID : " + reservationId);
            System.out.println("Reason         : Reservation does not exist");
            return;
        }

        if (reservation.isCancelled()) {
            System.out.println("Cancellation Failed");
            System.out.println("Reservation ID : " + reservationId);
            System.out.println("Reason         : Reservation is already cancelled");
            return;
        }

        rollbackRoomIds.push(reservation.getAssignedRoomId());
        inventory.increaseAvailability(reservation.getRoomType());
        reservation.cancel();

        System.out.println("Cancellation Successful");
        System.out.println("Reservation ID : " + reservation.getReservationId());
        System.out.println("Guest Name     : " + reservation.getGuestName());
        System.out.println("Released Room  : " + reservation.getAssignedRoomId());
        System.out.println("Room Type      : " + reservation.getRoomType());
        System.out.println("Status         : Cancelled");
    }

    public void displayRollbackStack() {
        if (rollbackRoomIds.isEmpty()) {
            System.out.println("No released room IDs available.");
            return;
        }

        for (int i = rollbackRoomIds.size() - 1; i >= 0; i--) {
            System.out.println(rollbackRoomIds.get(i));
        }
    }
}
