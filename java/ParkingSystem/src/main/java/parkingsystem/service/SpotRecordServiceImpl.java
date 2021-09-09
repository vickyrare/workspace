package parkingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingsystem.models.Spot;
import parkingsystem.models.SpotRecord;
import parkingsystem.repository.SpotRecordRepository;

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