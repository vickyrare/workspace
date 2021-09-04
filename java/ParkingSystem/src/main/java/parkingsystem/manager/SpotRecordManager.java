package parkingsystem.manager;

import parkingsystem.models.Spot;
import parkingsystem.models.SpotRecord;

import java.time.LocalDateTime;
import java.util.HashMap;

public class SpotRecordManager {
    HashMap<String, SpotRecord> spotRecords;

    public SpotRecordManager() {
        this.spotRecords = new HashMap<>();
    }

    public void recordVehicleIn(Spot spot) {
        spotRecords.put(spot.getVehicle().getVehicleRego(), new SpotRecord(spot, LocalDateTime.now()));
    }

    public void recordVehicleOut(Spot spot) {
        spotRecords.get(spot.getVehicle().getVehicleRego()).setTimeOut(LocalDateTime.now());
    }

    public void reportActivity() {
        for (String key : spotRecords.keySet()) {
            SpotRecord spotRecord = spotRecords.get(key);
            spotRecord.report();
        }
    }
}
