import java.util.HashMap;
import java.util.Map;

public class UseCase4RoomSearch {

    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        inventory.addRoomType("Single Room", 5);
        inventory.addRoomType("Double Room", 0);
        inventory.addRoomType("Suite Room", 2);

        Map<String, Room> roomCatalog = new HashMap<>();
        roomCatalog.put("Single Room", new SingleRoom(1, 180.0, 1200.0, "Free WiFi, AC"));
        roomCatalog.put("Double Room", new DoubleRoom(2, 280.0, 2200.0, "Free WiFi, AC, TV"));
        roomCatalog.put("Suite Room", new SuiteRoom(3, 450.0, 4500.0, "Free WiFi, AC, TV, Mini Bar"));

        RoomSearchService searchService = new RoomSearchService(inventory, roomCatalog);

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: 4.0");
        System.out.println();
        System.out.println("Available Rooms");
        System.out.println("---------------");

        searchService.displayAvailableRooms();
    }
}

abstract class Room {
    private final String roomType;
    private final int numberOfBeds;
    private final double sizeInSquareFeet;
    private final double pricePerNight;
    private final String amenities;

    public Room(String roomType, int numberOfBeds, double sizeInSquareFeet, double pricePerNight, String amenities) {
        this.roomType = roomType;
        this.numberOfBeds = numberOfBeds;
        this.sizeInSquareFeet = sizeInSquareFeet;
        this.pricePerNight = pricePerNight;
        this.amenities = amenities;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNumberOfBeds() {
        return numberOfBeds;
    }

    public double getSizeInSquareFeet() {
        return sizeInSquareFeet;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public String getAmenities() {
        return amenities;
    }
}

class SingleRoom extends Room {
    public SingleRoom(int numberOfBeds, double sizeInSquareFeet, double pricePerNight, String amenities) {
        super("Single Room", numberOfBeds, sizeInSquareFeet, pricePerNight, amenities);
    }
}

class DoubleRoom extends Room {
    public DoubleRoom(int numberOfBeds, double sizeInSquareFeet, double pricePerNight, String amenities) {
        super("Double Room", numberOfBeds, sizeInSquareFeet, pricePerNight, amenities);
    }
}

class SuiteRoom extends Room {
    public SuiteRoom(int numberOfBeds, double sizeInSquareFeet, double pricePerNight, String amenities) {
        super("Suite Room", numberOfBeds, sizeInSquareFeet, pricePerNight, amenities);
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

    public Map<String, Integer> getAllAvailability() {
        return new HashMap<>(roomAvailability);
    }
}

class RoomSearchService {
    private final RoomInventory inventory;
    private final Map<String, Room> roomCatalog;

    public RoomSearchService(RoomInventory inventory, Map<String, Room> roomCatalog) {
        this.inventory = inventory;
        this.roomCatalog = roomCatalog;
    }

    public void displayAvailableRooms() {
        Map<String, Integer> availabilityData = inventory.getAllAvailability();
        boolean found = false;

        for (Map.Entry<String, Integer> entry : availabilityData.entrySet()) {
            String roomType = entry.getKey();
            int availableCount = entry.getValue();

            if (availableCount > 0 && roomCatalog.containsKey(roomType)) {
                Room room = roomCatalog.get(roomType);
                System.out.println("Room Type     : " + room.getRoomType());
                System.out.println("Beds          : " + room.getNumberOfBeds());
                System.out.println("Size (sq. ft.): " + room.getSizeInSquareFeet());
                System.out.println("Price/Night   : Rs. " + room.getPricePerNight());
                System.out.println("Amenities     : " + room.getAmenities());
                System.out.println("Availability  : " + availableCount);
                System.out.println();
                found = true;
            }
        }

        if (!found) {
            System.out.println("No rooms are currently available.");
        }
    }
}
