package parkingsystem.app;

import parkingsystem.manager.ParkingManager;
import parkingsystem.models.SpotResult;
import parkingsystem.models.Vehicle;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        ParkingManager parkingManager = new ParkingManager(2, 20);

        Scanner in = new Scanner(System.in);

        while (in.hasNext()) {
            String input = in.nextLine();
            String inputTokens[] = input.split(" ");
            if (inputTokens.length == 1 && inputTokens[0].equalsIgnoreCase("q")) {
                System.exit(0);
            } else if (inputTokens.length == 1 && inputTokens[0].equalsIgnoreCase("h")) {
                System.out.println("q: quit");
                System.out.println("occupy vehicle_rego spot_id: occupy spot with vehicle");
                System.out.println("free spot_id: free spot");
                System.out.println("report_status: view status of the parking system");
                System.out.println("report_activities: view all activities");
            } else if (inputTokens.length == 3 && inputTokens[0].equalsIgnoreCase("occupy")) {
                String vehicleRego = inputTokens[1];
                String spotId = inputTokens[2];
                Vehicle vehicle = new Vehicle(vehicleRego);
                if (parkingManager.occupySpot(Integer.parseInt(spotId), vehicle) == SpotResult.SUCCESS) {
                    parkingManager.occupySpot(Integer.parseInt(spotId), vehicle);
                    System.out.println(SpotResult.SUCCESS.message);
                } else {
                    System.out.println("Spot already in use");
                }
            } else if (inputTokens.length == 2 && inputTokens[0].equalsIgnoreCase("free")) {
                String spotId = inputTokens[1];
                if (parkingManager.freeSpot(Integer.parseInt(spotId)) == SpotResult.SUCCESS) {
                    System.out.println(SpotResult.SUCCESS.message);
                } else {
                    System.out.println(SpotResult.SPOT_ALREADY_FREE.message);
                }
            } else if (inputTokens.length == 1 && inputTokens[0].equalsIgnoreCase("report_status")) {
                parkingManager.showReport();
            } else if (inputTokens.length == 1 && inputTokens[0].equalsIgnoreCase("report_activities")) {
                parkingManager.reportActivity();
            }
        }

    }
}
