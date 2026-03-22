package ParkingLott;
import java.util.ArrayList;
import java.util.Scanner;

public class ParkingLot {

    public static void main(String[] args) {
        ArrayList<ParkingTicket> assignedSpotList = new ArrayList<>();
        ParkingSpot parkingSpot = new ParkingSpot();
        RandomInfo randomInfo = new RandomInfo();
        Scanner scan = new Scanner(System.in);

        System.out.println("=== Parking Lot System ===");
        System.out.println("Available commands:");
        System.out.println("- park: Park a new car");
        System.out.println("- exit: Exit with your car");
        System.out.println("- info: Show all parked cars");
        System.out.println("- quit: Exit the system\n");

        while (true) {
            System.out.print("Enter command: ");
            String userInput = scan.nextLine().trim().toLowerCase();

            switch (userInput) {
                case "park":
                    handleParking(assignedSpotList, parkingSpot, randomInfo);
                    break;
                    
                case "exit":
                    handleExit(assignedSpotList, parkingSpot, randomInfo, scan);
                    break;
                    
                case "info":
                    displayAllParkedCars(assignedSpotList, parkingSpot);
                    break;
                    
                case "quit":
                    System.out.println("Exiting parking system. Goodbye!");
                    scan.close();
                    System.exit(0);
                    break;
                    
                default:
                    System.out.println("Invalid command. Please try again.");
                    System.out.println("Valid commands: park, exit, info, quit");
            }
        }
    }

    private static void handleParking(ArrayList<ParkingTicket> assignedSpotList, 
                                    ParkingSpot parkingSpot, RandomInfo randomInfo) {
        ParkingTicket parkingTicket = new ParkingTicket();
        Car car = new Car();

        // Get random car information
        String carColor = randomInfo.CarColor();
        String numberPlate = randomInfo.Numberplate();
        String carType = randomInfo.CarType();

        // Assign car details
        parkingTicket.setAssignedCar(car);
        parkingTicket.getAssignedCar().setNumberPlate(numberPlate);
        parkingTicket.getAssignedCar().setCarColor(carColor);
        parkingTicket.getAssignedCar().setCarType(carType);

        // Get parking spot
        int spotNum = parkingSpot.SpotNum();
        if (spotNum == 0) {
        	
            System.out.println("Sorry, no parking spots available.");
            return;
        }
        parkingTicket.setSpotNumber(spotNum);

        // Set ticket information
        parkingTicket.setCardType(randomInfo.CardType());
        parkingTicket.setTime(randomInfo.Time());
        parkingTicket.setDate(randomInfo.Date());
        parkingTicket.setCardNumber(randomInfo.CardNumber());

        // Display ticket
        System.out.println("\n=== Parking Ticket ===");
        System.out.println("Car Number: " + numberPlate);
        System.out.println("Car Color: " + carColor);
        System.out.println("Car Type: " + carType);
        System.out.println("Parking Time: " + parkingTicket.getTime());
        System.out.println("Date: " + parkingTicket.getDate());
        System.out.println("Spot Number: " + spotNum + "\n");

        assignedSpotList.add(parkingTicket);
        System.out.println("Car parked successfully. Total parked cars: " + assignedSpotList.size());
    }

    private static void handleExit(ArrayList<ParkingTicket> assignedSpotList,
                                 ParkingSpot parkingSpot, RandomInfo randomInfo,
                                 Scanner scan) {
        if (assignedSpotList.isEmpty()) {
            System.out.println("No cars currently parked.");
            return;
        }

        System.out.print("Enter your car number: ");
        String number = scan.nextLine().trim();

        ScanTicket scanTicket = new ScanTicket();
        TotalTime totalTime = new TotalTime();
        Payment payment = new Payment();

        for (ParkingTicket ticket : assignedSpotList) {
            if (scanTicket.cheaknumber(number, ticket.getAssignedCar().getNumberPlate()) == 1) {
                // Get exit time
                String exitDate = randomInfo.ExitDate();
                String exitTime = randomInfo.ExitTime();
                
                // Calculate parking duration and fee
                int[] timeSpent = totalTime.CalculateTime(
                    ticket.getDate(), exitDate, 
                    ticket.getTime(), exitTime
                );
                float amount = payment.TotalAmount(timeSpent[0], timeSpent[1]);

                // Display receipt
                System.out.println("\n=== Parking Receipt ===");
                System.out.println("Car Number: " + ticket.getAssignedCar().getNumberPlate());
                System.out.println("Car Color: " + ticket.getAssignedCar().getCarColor());
                System.out.println("Car Type: " + ticket.getAssignedCar().getCarType());
                System.out.println("Parking Time: " + ticket.getTime());
                System.out.println("Exit Time: " + exitTime);
                System.out.println("Parking Date: " + ticket.getDate());
                System.out.println("Exit Date: " + exitDate);
                System.out.println("Spot Number: " + ticket.getSpotNumber());
                System.out.println("Total Time: " + timeSpent[0] + " hours " + timeSpent[1] + " minutes");
                System.out.println("Total Amount: " + amount + " rupees\n");

                // Free the spot and remove ticket
                parkingSpot.FreeSpot(ticket.getSpotNumber());
                assignedSpotList.remove(ticket);
                System.out.println("Thank you for using our parking lot!");
                return;
            }
        }
        System.out.println("Car not found. Please check your car number.");
    }

    private static void displayAllParkedCars(ArrayList<ParkingTicket> assignedSpotList,
                                           ParkingSpot parkingSpot) {
        if (assignedSpotList.isEmpty()) {
            System.out.println("No cars currently parked.");
            return;
        }

        System.out.println("\n=== Currently Parked Cars ===");
        for (ParkingTicket ticket : assignedSpotList) {
            System.out.println("Car Number: " + ticket.getAssignedCar().getNumberPlate());
            System.out.println("Car Color: " + ticket.getAssignedCar().getCarColor());
            System.out.println("Car Type: " + ticket.getAssignedCar().getCarType());
            System.out.println("Parking Time: " + ticket.getTime());
            System.out.println("Date: " + ticket.getDate());
            System.out.println("Spot Number: " + ticket.getSpotNumber());
            System.out.println();
        }
        
        parkingSpot.sipe(); // Show spot status
        System.out.println("Total parked cars: " + assignedSpotList.size() + "\n");
    }
}
