package io.codecrafts.parkingsystem.repository;

import io.codecrafts.parkingsystem.models.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {
    Optional<Spot> findSpotBySpotId(int spotId);
    List<Spot> findSpotByFloorId(int floorId);
}
