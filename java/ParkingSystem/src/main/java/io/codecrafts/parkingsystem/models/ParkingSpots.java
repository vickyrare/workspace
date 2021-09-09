package io.codecrafts.parkingsystem.models;

import io.codecrafts.parkingsystem.service.SpotService;
import io.codecrafts.parkingsystem.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ParkingSpots {

    @Autowired
    private SpotService spotService;

    int numFloors;
    int numSpotsOnEachFloor;

    ArrayList<ParkingFloor> parkingFloors = new ArrayList<>();

    public ParkingSpots() {
    }

    public void init(int numFloors, int numSpotsOnEachFloor) {
        this.numSpotsOnEachFloor = numSpotsOnEachFloor;
        this.numFloors = numFloors;
        for(int floorNum = 1; floorNum <= numFloors; floorNum++) {
            ArrayList<Integer> spotIds = Util.createSpotIds(floorNum * 100, numSpotsOnEachFloor);
            for(int j = 0; j < spotIds.size(); j++) {
                Spot spot = new Spot(spotIds.get(j), floorNum, false);
                spotService.saveSpot(spot);
            }
        }
    }

    public int getNumSpotsOnEachFloor() {
        return numSpotsOnEachFloor;
    }

    public void setNumSpotsOnEachFloor(int numSpotsOnEachFloor) {
        this.numSpotsOnEachFloor = numSpotsOnEachFloor;
    }

    public int getNumFloors() {
        return numFloors;
    }

    public void setNumFloors(int numFloors) {
        this.numFloors = numFloors;
    }

    public boolean isSpotAvailable(Integer spotId) {
        int floorNum = Integer.parseInt(spotId.toString().charAt(0) + "");
        int spotNum = Integer.parseInt(spotId.toString().substring(1)) - 1;

        ParkingFloor currentFloor = parkingFloors.get(floorNum - 1);

        if (!currentFloor.getFloorSpots().get(spotNum).isOccupied()) {
            return true;
        }

        return false;
    }

    public Spot getSpot(Integer spotId) {
        Spot spot = spotService.findSpot(spotId);
        return spot;
    }

    public SpotResult occupySpot(Integer spotId, String vehicleRego) {
        Spot spot = getSpot(spotId);

        if (spot == null) {
            return SpotResult.SPOT_NOT_FOUND;
        } else if (!spot.isOccupied()) {
            spot.setOccupied(true);
            spotService.saveSpot(spot);
            return SpotResult.SUCCESS;
        } else if (spot.isOccupied()){
            return SpotResult.SPOT_ALREADY_IN_USE;
        }
        return SpotResult.SPOT_NOT_FOUND;
    }

    public SpotResult freeSpot(Integer spotId) {
        Spot spot = getSpot(spotId);

        if (spot == null) {
            return SpotResult.SPOT_NOT_FOUND;
        } else if (spot.isOccupied()) {
            spot.setOccupied(false);
            spotService.saveSpot(spot);
            return SpotResult.SUCCESS;
        } else if (!spot.isOccupied()){
            return SpotResult.SPOT_ALREADY_FREE;
        }
        return SpotResult.SPOT_NOT_FOUND;
    }

    public void showReport() {
        List<Spot> spots = spotService.getAll();
        int currentFloor, lastFloor = 1;
        for (Spot spot: spots) {
            currentFloor = spot.getFloorId();
            if (currentFloor != lastFloor) {
                System.out.println();
            }
            System.out.print(spot.getSpotId() + "[" + (spot.isOccupied() ? 'O' : 'A') + "] ");
            lastFloor = currentFloor;
        }
        System.out.println();
    }
}
