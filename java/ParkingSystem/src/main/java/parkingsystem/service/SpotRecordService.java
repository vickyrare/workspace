package parkingsystem.service;

import parkingsystem.models.Spot;
import parkingsystem.models.SpotRecord;

import java.util.List;

public interface SpotRecordService {
    List<SpotRecord> getAll();

    SpotRecord saveSpotRecord(SpotRecord spotRecord);

    public SpotRecord findBySpot(Spot spot);
}