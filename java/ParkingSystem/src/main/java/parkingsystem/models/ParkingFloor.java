package parkingsystem.models;

import parkingsystem.utils.Util;

import java.util.ArrayList;

public class ParkingFloor {
    int floorNum;
    int numSpots;
    ArrayList<Spot> floorSpots;

    public ParkingFloor(int floorNum, int numSpots) {
        this.floorNum = floorNum;
        this.numSpots = numSpots;
        ArrayList<Integer> spotIds = Util.createSpotIds(floorNum * 100 + 1, numSpots);
        floorSpots = new ArrayList<>();
        for(int i = 0; i < numSpots; i++) {
            floorSpots.add(new Spot(spotIds.get(i), 0,false));
        }
    }

    public int getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(int floorNum) {
        this.floorNum = floorNum;
    }

    public int getNumSpots() {
        return numSpots;
    }

    public void setNumSpots(int numSpots) {
        this.numSpots = numSpots;
    }

    public ArrayList<Spot> getFloorSpots() {
        return floorSpots;
    }

    public void setFloorSpots(ArrayList<Spot> floorSpots) {
        this.floorSpots = floorSpots;
    }
}
