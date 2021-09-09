package parkingsystem.service;

import parkingsystem.models.Spot;

import java.util.List;
import java.util.Optional;

public interface SpotService {
    List<Spot> getAll();

    public Spot saveSpot(Spot spot);

    public Spot findSpot(int SpotId);

    public void deleteSpot(Long id);

    public List<Spot> findSpotByFloorId(int floorId);
}