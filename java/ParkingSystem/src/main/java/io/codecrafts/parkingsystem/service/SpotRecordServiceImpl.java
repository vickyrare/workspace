package io.codecrafts.parkingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.codecrafts.parkingsystem.models.Spot;
import io.codecrafts.parkingsystem.models.SpotRecord;
import io.codecrafts.parkingsystem.repository.SpotRecordRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class SpotRecordServiceImpl implements SpotRecordService {

    @Autowired
    private SpotRecordRepository spotRecordRepository;

    public List<SpotRecord> getAll(){
        List<SpotRecord> spotRecords = new ArrayList<>();
        spotRecordRepository.findAll().forEach(spotRecords::add);
        return spotRecords;
    }

    @Override
    public SpotRecord saveSpotRecord(SpotRecord spotRecord) {
        return spotRecordRepository.save(spotRecord);
    }

    @Override
    public SpotRecord findBySpot(Spot spot) {
        return spotRecordRepository.findSpotRecordBySpotAndTimeOutIsNull(spot);
    }
}