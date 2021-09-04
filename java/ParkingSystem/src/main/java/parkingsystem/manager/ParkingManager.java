package parkingsystem.manager;

import parkingsystem.models.ParkingSpots;
import parkingsystem.models.SpotRecord;
import parkingsystem.models.SpotResult;
import parkingsystem.models.Vehicle;

import java.util.HashMap;

public class ParkingManager {
    ParkingSpots parkingSpots;
    HashMap<String, SpotRecord> spotRecords;
    SpotRecordManager spotRecordManager;

    public ParkingManager(int numFloors, int numSpotsOnEachFloor) {
        parkingSpots = new ParkingSpots(2, 20);
        spotRecordManager = new SpotRecordManager();
    }

    public SpotResult occupySpot(int spotId, Vehicle vehicle) {
        SpotResult spotResult = parkingSpots.occupySpot(spotId, vehicle);
        if (spotResult == SpotResult.SUCCESS) {
            spotRecordManager.recordVehicleIn(parkingSpots.getSpot(spotId));
        }
        return spotResult;
    }

    public SpotResult freeSpot(int spotId) {
        SpotResult spotResult = parkingSpots.freeSpot(spotId);
        if (spotResult == SpotResult.SUCCESS) {
            spotRecordManager.recordVehicleOut(parkingSpots.getSpot(spotId));
        }
        return spotResult;
    }

    public void showReport() {
        parkingSpots.showReport();
    }

    public void reportActivity() {
        spotRecordManager.reportActivity();
    }
}
