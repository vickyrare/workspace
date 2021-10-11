package io.codecrafts.parkingsystem.service;

import io.codecrafts.parkingsystem.models.Spot;
import io.codecrafts.parkingsystem.models.SpotRecord;

import java.util.List;

public interface SpotRecordService {
    List<SpotRecord> getAll();

    SpotRecord saveSpotRecord(SpotRecord spotRecord);

    public SpotRecord findBySpot(Spot spot);
}