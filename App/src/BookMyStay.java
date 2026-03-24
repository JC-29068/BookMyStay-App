import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {
        String fileName = "book_my_stay_state.ser";

        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 3);
        inventory.addRoomType("Double Room", 2);
        inventory.addRoomType("Suite Room", 1);

        BookingHistory bookingHistory = new BookingHistory();
        bookingHistory.addReservation(new ConfirmedReservation("R001", "Aarav Sharma", "Single Room", "SR-001", 2, 2400.0));
        bookingHistory.addReservation(new ConfirmedReservation("R002", "Diya Mehta", "Double Room", "DR-001", 3, 6600.0));
        bookingHistory.addReservation(new ConfirmedReservation("R003", "Kabir Nair", "Suite Room", "SU-001", 1, 4500.0));

        inventory.reduceAvailability("Single Room");
        inventory.reduceAvailability("Double Room");
        inventory.reduceAvailability("Suite Room");

        SystemState initialState = new SystemState(inventory, bookingHistory);
        PersistenceService persistenceService = new PersistenceService();

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: 12.0");
        System.out.println();
        System.out.println("Current In-Memory State");
        System.out.println("-----------------------");
        displaySystemState(initialState);

        System.out.println("Saving System State");
        System.out.println("-------------------");
        boolean saved = persistenceService.saveState(initialState, fileName);
        if (saved) {
            System.out.println("System state saved successfully to " + fileName);
        } else {
            System.out.println("System state could not be saved.");
        }

        System.out.println();
        System.out.println("Restoring System State");
        System.out.println("----------------------");
        SystemState restoredState = persistenceService.loadState(fileName);

        if (restoredState != null) {
            System.out.println("System state restored successfully.");
            System.out.println();
            System.out.println("Recovered State");
            System.out.println("---------------");
            displaySystemState(restoredState);
        } else {
            System.out.println("No valid persisted state found. Starting with a safe empty state.");
            System.out.println();
            System.out.println("Recovered State");
            System.out.println("---------------");
            displaySystemState(new SystemState(new RoomInventory(), new BookingHistory()));
        }
    }

    private static void displaySystemState(SystemState state) {
        System.out.println("Inventory");
        System.out.println("---------");
        state.getInventory().displayInventory();

        System.out.println("Booking History");
        System.out.println("---------------");
        state.getBookingHistory().displayReservations();
    }
}

class ConfirmedReservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String reservationId;
    private final String guestName;
    private final String roomType;
    private final String assignedRoomId;
    private final int numberOfNights;
    private final double totalAmount;

    public ConfirmedReservation(String reservationId, String guestName, String roomType, String assignedRoomId, int numberOfNights, double totalAmount) {
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

class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<ConfirmedReservation> reservations;

    public BookingHistory() {
        this.reservations = new ArrayList<>();
    }

    public void addReservation(ConfirmedReservation reservation) {
        reservations.add(reservation);
    }

    public List<ConfirmedReservation> getReservations() {
        return new ArrayList<>(reservations);
    }

    public void displayReservations() {
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
            System.out.println();
        }
    }
}

class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Integer> roomAvailability;

    public RoomInventory() {
        this.roomAvailability = new HashMap<>();
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

    public void displayInventory() {
        if (roomAvailability.isEmpty()) {
            System.out.println("No inventory data found.");
            System.out.println();
            return;
        }

        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println("Room Type    : " + entry.getKey());
            System.out.println("Availability : " + entry.getValue());
            System.out.println();
        }
    }
}

class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    private final RoomInventory inventory;
    private final BookingHistory bookingHistory;

    public SystemState(RoomInventory inventory, BookingHistory bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }

    public RoomInventory getInventory() {
        return inventory;
    }

    public BookingHistory getBookingHistory() {
        return bookingHistory;
    }
}

class PersistenceService {

    public boolean saveState(SystemState state, String fileName) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(state);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public SystemState loadState(String fileName) {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Object data = inputStream.readObject();
            if (data instanceof SystemState) {
                return (SystemState) data;
            }
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
        return null;
    }
}
