import java.util.HashMap;
import java.util.Map;

public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        BookingService bookingService = new BookingService(inventory);

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: 9.0");
        System.out.println();
        System.out.println("Booking Validation and Error Handling");
        System.out.println("-------------------------------------");
        System.out.println();

        processBooking(bookingService, new Reservation("R001", "Aarav Sharma", "Single Room", 2));
        processBooking(bookingService, new Reservation("R002", "Diya Mehta", "Deluxe Room", 3));
        processBooking(bookingService, new Reservation("R003", "Kabir Nair", "Suite Room", 0));
        processBooking(bookingService, new Reservation("R004", "Ananya Iyer", "Double Room", 1));
        processBooking(bookingService, new Reservation("R005", "Vivaan Rao", "Double Room", 2));

        System.out.println("Final Inventory");
        System.out.println("---------------");
        inventory.displayInventory();
    }

    private static void processBooking(BookingService bookingService, Reservation reservation) {
        try {
            BookingConfirmation confirmation = bookingService.confirmReservation(reservation);
            System.out.println("Reservation ID   : " + confirmation.getReservationId());
            System.out.println("Guest Name       : " + confirmation.getGuestName());
            System.out.println("Room Type        : " + confirmation.getRoomType());
            System.out.println("Assigned Room ID : " + confirmation.getAssignedRoomId());
            System.out.println("Nights           : " + confirmation.getNumberOfNights());
            System.out.println("Status           : Reservation Confirmed");
        } catch (InvalidBookingException e) {
            System.out.println("Reservation ID   : " + reservation.getReservationId());
            System.out.println("Guest Name       : " + reservation.getGuestName());
            System.out.println("Room Type        : " + reservation.getRoomType());
            System.out.println("Status           : Booking Failed");
            System.out.println("Reason           : " + e.getMessage());
        }

        System.out.println();
    }
}

class Reservation {
    private final String reservationId;
    private final String guestName;
    private final String roomType;
    private final int numberOfNights;

    public Reservation(String reservationId, String guestName, String roomType, int numberOfNights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.numberOfNights = numberOfNights;
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

    public int getNumberOfNights() {
        return numberOfNights;
    }
}

class BookingConfirmation {
    private final String reservationId;
    private final String guestName;
    private final String roomType;
    private final String assignedRoomId;
    private final int numberOfNights;

    public BookingConfirmation(String reservationId, String guestName, String roomType, String assignedRoomId, int numberOfNights) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.assignedRoomId = assignedRoomId;
        this.numberOfNights = numberOfNights;
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
}

class RoomInventory {
    private final Map<String, Integer> roomAvailability;

    public RoomInventory() {
        this.roomAvailability = new HashMap<>();
    }

    public void addRoomType(String roomType, int availableCount) {
        if (availableCount >= 0) {
            roomAvailability.put(roomType, availableCount);
        }
    }

    public boolean containsRoomType(String roomType) {
        return roomAvailability.containsKey(roomType);
    }

    public int getAvailability(String roomType) {
        Integer availableCount = roomAvailability.get(roomType);
        return availableCount != null ? availableCount : 0;
    }

    public void reduceAvailability(String roomType) throws InvalidBookingException {
        if (!containsRoomType(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType);
        }

        int currentAvailability = getAvailability(roomType);

        if (currentAvailability <= 0) {
            throw new InvalidBookingException("No rooms available for room type: " + roomType);
        }

        int updatedAvailability = currentAvailability - 1;

        if (updatedAvailability < 0) {
            throw new InvalidBookingException("Inventory cannot become negative for room type: " + roomType);
        }

        roomAvailability.put(roomType, updatedAvailability);
    }

    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println("Room Type    : " + entry.getKey());
            System.out.println("Availability : " + entry.getValue());
            System.out.println();
        }
    }
}

class BookingService {
    private final RoomInventory inventory;
    private final Map<String, Integer> roomCounters;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.roomCounters = new HashMap<>();
    }

    public BookingConfirmation confirmReservation(Reservation reservation) throws InvalidBookingException {
        validateReservation(reservation);
        inventory.reduceAvailability(reservation.getRoomType());
        String assignedRoomId = generateRoomId(reservation.getRoomType());

        return new BookingConfirmation(
                reservation.getReservationId(),
                reservation.getGuestName(),
                reservation.getRoomType(),
                assignedRoomId,
                reservation.getNumberOfNights()
        );
    }

    private void validateReservation(Reservation reservation) throws InvalidBookingException {
        if (reservation.getGuestName() == null || reservation.getGuestName().trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty");
        }

        if (reservation.getRoomType() == null || reservation.getRoomType().trim().isEmpty()) {
            throw new InvalidBookingException("Room type cannot be empty");
        }

        if (!inventory.containsRoomType(reservation.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + reservation.getRoomType());
        }

        if (reservation.getNumberOfNights() <= 0) {
            throw new InvalidBookingException("Number of nights must be greater than zero");
        }
    }

    private String generateRoomId(String roomType) {
        String prefix = getRoomPrefix(roomType);
        int nextNumber = roomCounters.getOrDefault(roomType, 0) + 1;
        roomCounters.put(roomType, nextNumber);
        return prefix + String.format("%03d", nextNumber);
    }

    private String getRoomPrefix(String roomType) {
        if ("Single Room".equals(roomType)) {
            return "SR-";
        }
        if ("Double Room".equals(roomType)) {
            return "DR-";
        }
        if ("Suite Room".equals(roomType)) {
            return "SU-";
        }
        return "RM-";
    }
}

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}
