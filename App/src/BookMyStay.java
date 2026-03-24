import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {
        Reservation reservation1 = new Reservation("R001", "Aarav Sharma", "Single Room", 2, "SR-001");
        Reservation reservation2 = new Reservation("R002", "Diya Mehta", "Suite Room", 3, "SU-001");

        AddOnServiceManager serviceManager = new AddOnServiceManager();

        serviceManager.addServiceToReservation(reservation1.getReservationId(), new AddOnService("Breakfast", 500.0));
        serviceManager.addServiceToReservation(reservation1.getReservationId(), new AddOnService("Airport Pickup", 1200.0));

        serviceManager.addServiceToReservation(reservation2.getReservationId(), new AddOnService("Spa Access", 2000.0));
        serviceManager.addServiceToReservation(reservation2.getReservationId(), new AddOnService("Dinner Buffet", 1500.0));
        serviceManager.addServiceToReservation(reservation2.getReservationId(), new AddOnService("Laundry", 600.0));

        System.out.println("Welcome to Book My Stay App");
        System.out.println("Version: 7.0");
        System.out.println();
        System.out.println("Add-On Service Selection");
        System.out.println("------------------------");
        System.out.println();

        displayReservationServices(reservation1, serviceManager);
        displayReservationServices(reservation2, serviceManager);
    }

    private static void displayReservationServices(Reservation reservation, AddOnServiceManager serviceManager) {
        System.out.println("Reservation ID   : " + reservation.getReservationId());
        System.out.println("Guest Name       : " + reservation.getGuestName());
        System.out.println("Room Type        : " + reservation.getRoomType());
        System.out.println("Assigned Room ID : " + reservation.getAssignedRoomId());
        System.out.println("Nights           : " + reservation.getNumberOfNights());
        System.out.println("Selected Services:");

        List<AddOnService> services = serviceManager.getServicesForReservation(reservation.getReservationId());

        if (services.isEmpty()) {
            System.out.println("None");
        } else {
            for (AddOnService service : services) {
                System.out.println("- " + service.getServiceName() + " : Rs. " + service.getServiceCost());
            }
        }

        System.out.println("Total Add-On Cost: Rs. " + serviceManager.calculateTotalServiceCost(reservation.getReservationId()));
        System.out.println();
    }
}

class Reservation {
    private final String reservationId;
    private final String guestName;
    private final String roomType;
    private final int numberOfNights;
    private final String assignedRoomId;

    public Reservation(String reservationId, String guestName, String roomType, int numberOfNights, String assignedRoomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.numberOfNights = numberOfNights;
        this.assignedRoomId = assignedRoomId;
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

    public String getAssignedRoomId() {
        return assignedRoomId;
    }
}

class AddOnService {
    private final String serviceName;
    private final double serviceCost;

    public AddOnService(String serviceName, double serviceCost) {
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getServiceCost() {
        return serviceCost;
    }
}

class AddOnServiceManager {
    private final Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        this.reservationServices = new HashMap<>();
    }

    public void addServiceToReservation(String reservationId, AddOnService service) {
        reservationServices.computeIfAbsent(reservationId, key -> new ArrayList<>()).add(service);
    }

    public List<AddOnService> getServicesForReservation(String reservationId) {
        return new ArrayList<>(reservationServices.getOrDefault(reservationId, new ArrayList<>()));
    }

    public double calculateTotalServiceCost(String reservationId) {
        List<AddOnService> services = reservationServices.getOrDefault(reservationId, new ArrayList<>());
        double total = 0.0;

        for (AddOnService service : services) {
            total += service.getServiceCost();
        }

        return total;
    }
}
