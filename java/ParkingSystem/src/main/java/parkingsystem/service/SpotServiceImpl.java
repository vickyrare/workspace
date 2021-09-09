package parkingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingsystem.models.Spot;
import parkingsystem.repository.SpotRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpotServiceImpl implements SpotService {

    @Autowired
    private SpotRepository spotRepository;

    public List<Spot> getAll(){
        List<Spot> spots = new ArrayList<Spot>();
        spotRepository.findAll().forEach(spots::add);
        return spots;
    }

    @Override
    public Spot saveSpot(Spot spot) {
        return spotRepository.save(spot);
    }

    @Override
    public Spot findSpot(int spotId) {
        Optional<Spot> spot = spotRepository.findSpotBySpotId(spotId);
        return spot.get();
    }

    @Override
    public void deleteSpot(Long id) {
        spotRepository.deleteById(id);
    }

    @Override
    public List<Spot> findSpotByFloorId(int floorId) {
        List<Spot> spots = spotRepository.findSpotByFloorId(floorId);
        return spots;
    }
}