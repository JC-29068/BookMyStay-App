import java.util.HashMap;
import java.util.Map;

public class UseCase3InventorySetup {

    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();

        inventory.addRoomType("Single Room", 10);
        inventory.addRoomType("Double Room", 6);
        inventory.addRoomType("Suite Room", 2);

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: 3.0");
        System.out.println();
        System.out.println("Initial Centralized Room Inventory");
        System.out.println("----------------------------------");
        inventory.displayInventory();

        System.out.println("Updating inventory...");
        inventory.updateAvailability("Double Room", 5);
        inventory.updateAvailability("Suite Room", 1);
        System.out.println();

        System.out.println("Updated Centralized Room Inventory");
        System.out.println("----------------------------------");
        inventory.displayInventory();
    }
}

class RoomInventory {
    private final Map<String, Integer> roomAvailability;

    public RoomInventory() {
        this.roomAvailability = new HashMap<>();
    }

    public void addRoomType(String roomType, int availableCount) {
        if (availableCount < 0) {
            System.out.println("Availability cannot be negative for " + roomType + ".");
            return;
        }
        roomAvailability.put(roomType, availableCount);
    }

    public int getAvailability(String roomType) {
        Integer availableCount = roomAvailability.get(roomType);
        return availableCount != null ? availableCount : 0;
    }

    public void updateAvailability(String roomType, int newAvailableCount) {
        if (!roomAvailability.containsKey(roomType)) {
            System.out.println("Room type not found: " + roomType);
            return;
        }

        if (newAvailableCount < 0) {
            System.out.println("Availability cannot be negative for " + roomType + ".");
            return;
        }

        roomAvailability.put(roomType, newAvailableCount);
    }

    public void displayInventory() {
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println("Room Type    : " + entry.getKey());
            System.out.println("Availability : " + entry.getValue());
            System.out.println();
        }
    }
}
