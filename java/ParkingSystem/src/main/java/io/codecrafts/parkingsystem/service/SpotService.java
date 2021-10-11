package io.codecrafts.parkingsystem.service;

import io.codecrafts.parkingsystem.models.Spot;

import java.util.List;

public interface SpotService {
    List<Spot> getAll();

    public Spot saveSpot(Spot spot);

    public Spot findSpot(int SpotId);

    public void deleteSpot(Long id);

    public List<Spot> findSpotByFloorId(int floorId);
}