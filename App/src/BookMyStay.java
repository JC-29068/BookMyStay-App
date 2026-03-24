
public class UseCase2RoomInitialization {

    public static void main(String[] args) {
        Room singleRoom = new SingleRoom(1, 180.0, 1200.0);
        Room doubleRoom = new DoubleRoom(2, 280.0, 2200.0);
        Room suiteRoom = new SuiteRoom(3, 450.0, 4500.0);

        int singleRoomAvailability = 10;
        int doubleRoomAvailability = 6;
        int suiteRoomAvailability = 2;

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: 2.0");
        System.out.println();
        System.out.println("Available Room Types and Static Availability");
        System.out.println("-------------------------------------------");

        displayRoomDetails(singleRoom, singleRoomAvailability);
        displayRoomDetails(doubleRoom, doubleRoomAvailability);
        displayRoomDetails(suiteRoom, suiteRoomAvailability);
    }

    private static void displayRoomDetails(Room room, int availability) {
        System.out.println("Room Type     : " + room.getRoomType());
        System.out.println("Beds          : " + room.getNumberOfBeds());
        System.out.println("Size (sq. ft.): " + room.getSizeInSquareFeet());
        System.out.println("Price/Night   : Rs. " + room.getPricePerNight());
        System.out.println("Availability  : " + availability);
        System.out.println();
    }
}

abstract class Room {
    private final int numberOfBeds;
    private final double sizeInSquareFeet;
    private final double pricePerNight;

    
    protected Room(int numberOfBeds, double sizeInSquareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.sizeInSquareFeet = sizeInSquareFeet;
        this.pricePerNight = pricePerNight;
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


    public abstract String getRoomType();
}
presents a single room.

class SingleRoom extends Room {

    public SingleRoom(int numberOfBeds, double sizeInSquareFeet, double pricePerNight) {
        super(numberOfBeds, sizeInSquareFeet, pricePerNight);
    }

    @Override
    public String getRoomType() {
        return "Single Room";
    }
}


class DoubleRoom extends Room {

    public DoubleRoom(int numberOfBeds, double sizeInSquareFeet, double pricePerNight) {
        super(numberOfBeds, sizeInSquareFeet, pricePerNight);
    }

    @Override
    public String getRoomType() {
        return "Double Room";
    }
}

class SuiteRoom extends Room {

    public SuiteRoom(int numberOfBeds, double sizeInSquareFeet, double pricePerNight) {
        super(numberOfBeds, sizeInSquareFeet, pricePerNight);
    }

    @Override
    public String getRoomType() {
        return "Suite Room";
    }
}
