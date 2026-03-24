import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class UseCase6RoomAllocationService {

    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        Queue<Reservation> bookingRequestQueue = new LinkedList<>();
        bookingRequestQueue.offer(new Reservation("R001", "Aarav Sharma", "Single Room", 2));
        bookingRequestQueue.offer(new Reservation("R002", "Diya Mehta", "Double Room", 3));
        bookingRequestQueue.offer(new Reservation("R003", "Kabir Nair", "Suite Room", 1));
        bookingRequestQueue.offer(new Reservation("R004", "Ananya Iyer", "Single Room", 2));
        bookingRequestQueue.offer(new Reservation("R005", "Vivaan Rao", "Single Room", 1));

        BookingService bookingService = new BookingService(inventory);

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: 6.0");
        System.out.println();
        System.out.println("Processing Booking Requests");
        System.out.println("---------------------------");

        while (!bookingRequestQueue.isEmpty()) {
            Reservation reservation = bookingRequestQueue.poll();
            bookingService.confirmReservation(reservation);
            System.out.println();
        }

        System.out.println("Final Inventory");
        System.out.println("---------------");
        inventory.displayInventory();
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
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println("Room Type    : " + entry.getKey());
            System.out.println("Availability : " + entry.getValue());
            System.out.println();
        }
    }
}

class BookingService {
    private final RoomInventory inventory;
    private final Set<String> allocatedRoomIds;
    private final Map<String, Set<String>> allocatedRoomsByType;
    private final Map<String, Integer> roomCounters;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRoomIds = new HashSet<>();
        this.allocatedRoomsByType = new HashMap<>();
        this.roomCounters = new HashMap<>();
    }

    public void confirmReservation(Reservation reservation) {
        String roomType = reservation.getRoomType();

        if (inventory.getAvailability(roomType) <= 0) {
            System.out.println("Reservation Request ID : " + reservation.getRequestId());
            System.out.println("Guest Name             : " + reservation.getGuestName());
            System.out.println("Requested Room Type    : " + roomType);
            System.out.println("Status                 : Booking Failed");
            System.out.println("Reason                 : No rooms available");
            return;
        }

        String roomId = generateUniqueRoomId(roomType);
        boolean inventoryUpdated = inventory.reduceAvailability(roomType);

        if (!inventoryUpdated) {
            System.out.println("Reservation Request ID : " + reservation.getRequestId());
            System.out.println("Guest Name             : " + reservation.getGuestName());
            System.out.println("Requested Room Type    : " + roomType);
            System.out.println("Status                 : Booking Failed");
            System.out.println("Reason                 : Inventory update failed");
            return;
        }

        allocatedRoomIds.add(roomId);
        allocatedRoomsByType.computeIfAbsent(roomType, key -> new HashSet<>()).add(roomId);

        System.out.println("Reservation Request ID : " + reservation.getRequestId());
        System.out.println("Guest Name             : " + reservation.getGuestName());
        System.out.println("Requested Room Type    : " + roomType);
        System.out.println("Assigned Room ID       : " + roomId);
        System.out.println("Nights                 : " + reservation.getNumberOfNights());
        System.out.println("Status                 : Reservation Confirmed");
    }

    private String generateUniqueRoomId(String roomType) {
        String prefix = getRoomPrefix(roomType);

        int nextNumber = roomCounters.getOrDefault(roomType, 0) + 1;
        String roomId = prefix + String.format("%03d", nextNumber);

        while (allocatedRoomIds.contains(roomId)) {
            nextNumber++;
            roomId = prefix + String.format("%03d", nextNumber);
        }

        roomCounters.put(roomType, nextNumber);
        return roomId;
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
