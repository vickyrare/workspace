package parkingsystem.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingsystem.models.ParkingSpots;
import parkingsystem.models.Spot;
import parkingsystem.models.SpotResult;
import parkingsystem.service.SpotService;

@Service
public class ParkingManager {

    @Autowired
    ParkingSpots parkingSpots;

    @Autowired
    SpotService spotService;

    @Autowired
    SpotRecordManager spotRecordManager;

    public ParkingManager() {
    }

    public void init(int numFloors, int numSpotsOnEachFloor) {
        parkingSpots.init(numFloors, numSpotsOnEachFloor);
    }

    public SpotResult occupySpot(int spotId, String vehicleRego) {
        SpotResult spotResult = parkingSpots.occupySpot(spotId, vehicleRego);
        if (spotResult == SpotResult.SUCCESS) {
            Spot spot = spotService.findSpot(spotId);
            spotRecordManager.recordVehicleIn(spot, vehicleRego);
        }
        return spotResult;
    }

    public SpotResult freeSpot(int spotId) {
        SpotResult spotResult = parkingSpots.freeSpot(spotId);
        if (spotResult == SpotResult.SUCCESS) {
            Spot spot = spotService.findSpot(spotId);
            spotRecordManager.recordVehicleOut(spot);
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
