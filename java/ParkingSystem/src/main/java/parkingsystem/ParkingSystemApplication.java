package parkingsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import parkingsystem.manager.ParkingManager;
import parkingsystem.models.SpotResult;

import java.util.Scanner;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class ParkingSystemApplication implements CommandLineRunner {

    private ParkingManager parkingManager;

    public ParkingSystemApplication(ParkingManager parkingManager) {
        this.parkingManager = parkingManager;
    }

    public static void main(String[] args) {
        SpringApplication.run(ParkingSystemApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        parkingManager.init(2, 20);

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
                SpotResult spotResult = parkingManager.occupySpot(Integer.parseInt(spotId), vehicleRego);
                System.out.println(spotResult.message);
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

