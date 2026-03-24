import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) throws InterruptedException {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 2);
        inventory.addRoomType("Double Room", 1);
        inventory.addRoomType("Suite Room", 1);

        BookingRequestQueue requestQueue = new BookingRequestQueue();
        requestQueue.addRequest(new Reservation("R001", "Aarav Sharma", "Single Room", 2));
        requestQueue.addRequest(new Reservation("R002", "Diya Mehta", "Single Room", 1));
        requestQueue.addRequest(new Reservation("R003", "Kabir Nair", "Double Room", 3));
        requestQueue.addRequest(new Reservation("R004", "Ananya Iyer", "Suite Room", 2));
        requestQueue.addRequest(new Reservation("R005", "Vivaan Rao", "Suite Room", 1));
        requestQueue.addRequest(new Reservation("R006", "Myra Das", "Double Room", 1));

        ConcurrentBookingProcessor processor = new ConcurrentBookingProcessor(inventory, requestQueue);

        Thread worker1 = new Thread(processor, "Booking-Worker-1");
        Thread worker2 = new Thread(processor, "Booking-Worker-2");
        Thread worker3 = new Thread(processor, "Booking-Worker-3");

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: 11.0");
        System.out.println();
        System.out.println("Concurrent Booking Simulation");
        System.out.println("-----------------------------");

        worker1.start();
        worker2.start();
        worker3.start();

        worker1.join();
        worker2.join();
        worker3.join();

        System.out.println();
        System.out.println("Final Inventory");
        System.out.println("---------------");
        inventory.displayInventory();

        System.out.println("Allocated Room IDs");
        System.out.println("------------------");
        processor.displayAllocatedRoomIds();
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

class BookingRequestQueue {
    private final Queue<Reservation> queue;

    public BookingRequestQueue() {
        this.queue = new LinkedList<>();
    }

    public synchronized void addRequest(Reservation reservation) {
        queue.offer(reservation);
    }

    public synchronized Reservation getNextRequest() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

class RoomInventory {
    private final Map<String, Integer> roomAvailability;

    public RoomInventory() {
        this.roomAvailability = new HashMap<>();
    }

    public synchronized void addRoomType(String roomType, int availableCount) {
        if (availableCount >= 0) {
            roomAvailability.put(roomType, availableCount);
        }
    }

    public synchronized int getAvailability(String roomType) {
        Integer availableCount = roomAvailability.get(roomType);
        return availableCount != null ? availableCount : 0;
    }

    public synchronized boolean allocateRoom(String roomType) {
        int currentAvailability = getAvailability(roomType);
        if (currentAvailability <= 0) {
            return false;
        }
        roomAvailability.put(roomType, currentAvailability - 1);
        return true;
    }

    public synchronized void displayInventory() {
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println("Room Type    : " + entry.getKey());
            System.out.println("Availability : " + entry.getValue());
            System.out.println();
        }
    }
}

class ConcurrentBookingProcessor implements Runnable {
    private final RoomInventory inventory;
    private final BookingRequestQueue requestQueue;
    private final Set<String> allocatedRoomIds;
    private final Map<String, Integer> roomCounters;

    public ConcurrentBookingProcessor(RoomInventory inventory, BookingRequestQueue requestQueue) {
        this.inventory = inventory;
        this.requestQueue = requestQueue;
        this.allocatedRoomIds = new HashSet<>();
        this.roomCounters = new HashMap<>();
    }

    @Override
    public void run() {
        while (true) {
            Reservation reservation = requestQueue.getNextRequest();

            if (reservation == null) {
                break;
            }

            processReservation(reservation);
        }
    }

    private void processReservation(Reservation reservation) {
        String threadName = Thread.currentThread().getName();

        synchronized (this) {
            String roomType = reservation.getRoomType();

            if (!inventory.allocateRoom(roomType)) {
                System.out.println(threadName + " -> Reservation ID: " + reservation.getReservationId()
                        + ", Guest: " + reservation.getGuestName()
                        + ", Room Type: " + roomType
                        + ", Status: Booking Failed");
                return;
            }

            String roomId = generateUniqueRoomId(roomType);

            System.out.println(threadName + " -> Reservation ID: " + reservation.getReservationId()
                    + ", Guest: " + reservation.getGuestName()
                    + ", Room Type: " + roomType
                    + ", Assigned Room ID: " + roomId
                    + ", Nights: " + reservation.getNumberOfNights()
                    + ", Status: Confirmed");
        }
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
        allocatedRoomIds.add(roomId);
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

    public synchronized void displayAllocatedRoomIds() {
        if (allocatedRoomIds.isEmpty()) {
            System.out.println("No room IDs allocated.");
            return;
        }

        for (String roomId : allocatedRoomIds) {
            System.out.println(roomId);
        }
    }
}
