package io.codecrafts.parkingsystem.manager;

import io.codecrafts.parkingsystem.models.Spot;
import io.codecrafts.parkingsystem.service.SpotRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.codecrafts.parkingsystem.models.SpotRecord;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
public class SpotRecordManager {

    @Autowired
    SpotRecordService spotRecordService;

    HashMap<String, SpotRecord> spotRecords;

    public SpotRecordManager() {
        this.spotRecords = new HashMap<>();
    }

    public void recordVehicleIn(Spot spot, String vehicleRego) {
        SpotRecord spotRecord = new SpotRecord(spot, vehicleRego, LocalDateTime.now());
        spotRecordService.saveSpotRecord(spotRecord);
    }

    public void recordVehicleOut(Spot spot) {
        SpotRecord spotRecord = spotRecordService.findBySpot(spot);
        spotRecord.setTimeOut(LocalDateTime.now());
        spotRecordService.saveSpotRecord(spotRecord);
    }

    public void reportActivity() {
        List<SpotRecord> spotRecords = spotRecordService.getAll();
        for (SpotRecord spotRecord: spotRecords) {
            spotRecord.report();
        }
    }
}
