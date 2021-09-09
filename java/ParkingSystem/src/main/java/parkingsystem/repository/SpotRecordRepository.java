package parkingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import parkingsystem.models.Spot;
import parkingsystem.models.SpotRecord;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpotRecordRepository extends JpaRepository<SpotRecord, Long> {
    SpotRecord findSpotRecordBySpotAndTimeOutIsNull(Spot spot);
}
