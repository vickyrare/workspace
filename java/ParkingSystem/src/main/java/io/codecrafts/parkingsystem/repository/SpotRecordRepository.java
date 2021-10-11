package io.codecrafts.parkingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.codecrafts.parkingsystem.models.Spot;
import io.codecrafts.parkingsystem.models.SpotRecord;

@Repository
public interface SpotRecordRepository extends JpaRepository<SpotRecord, Long> {
    SpotRecord findSpotRecordBySpotAndTimeOutIsNull(Spot spot);
}
