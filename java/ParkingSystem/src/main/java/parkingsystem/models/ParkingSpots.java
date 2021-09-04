package parkingsystem.models;

import java.util.ArrayList;

public class ParkingSpots {
    int numFloors;
    int numSpotsOnEachFloor;

    ArrayList<ParkingFloor> parkingFloors = new ArrayList<>();

    public ParkingSpots(int numFloors, int numSpotsOnEachFloor) {
        this.numSpotsOnEachFloor = numSpotsOnEachFloor;
        this.numFloors = numFloors;
        for(int i = 1; i <= numFloors; i++) {
            ParkingFloor parkingFloor = new ParkingFloor(i, 20);
            parkingFloors.add(parkingFloor);
        }
        for(int i = 0; i < numFloors; i++) {
            for(int j = 0; j < numSpotsOnEachFloor; j++) {
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
        int floorNum = Integer.parseInt(spotId.toString().charAt(0) + "");
        int spotNum = Integer.parseInt(spotId.toString().substring(1)) - 1;

        ParkingFloor currentFloor = parkingFloors.get(floorNum - 1);

        return currentFloor.getFloorSpots().get(spotNum);
    }

    public SpotResult occupySpot(Integer spotId, Vehicle vehicle) {
        int floorNum = Integer.parseInt(spotId.toString().charAt(0) + "");
        int spotNum = Integer.parseInt(spotId.toString().substring(1)) - 1;

        if (floorNum > parkingFloors.size()) {
            return SpotResult.SPOT_NOT_FOUND;
        }

        ParkingFloor currentFloor = parkingFloors.get(floorNum - 1);

        if (spotNum >= currentFloor.getNumSpots()) {
            return SpotResult.SPOT_NOT_FOUND;
        }

        if (!currentFloor.getFloorSpots().get(spotNum).isOccupied()) {
            currentFloor.getFloorSpots().get(spotNum).setVehicle(vehicle);
            currentFloor.getFloorSpots().get(spotNum).setOccupied(true);
            return SpotResult.SUCCESS;
        } else {
            return SpotResult.SPOT_ALREADY_IN_USE;
        }
    }

    public SpotResult freeSpot(Integer spotId) {
        int floorNum = Integer.parseInt(spotId.toString().charAt(0) + "");
        int spotNum = Integer.parseInt(spotId.toString().substring(1)) - 1;

        if (floorNum > parkingFloors.size()) {
            return SpotResult.SPOT_NOT_FOUND;
        }

        ParkingFloor currentFloor = parkingFloors.get(floorNum - 1);

        if (spotNum >= currentFloor.getNumSpots()) {
            return SpotResult.SPOT_NOT_FOUND;
        }

        if (currentFloor.getFloorSpots().get(spotNum).isOccupied()) {
            //currentFloor.getFloorSpots().get(spotNum).setVehicle(null);
            currentFloor.getFloorSpots().get(spotNum).setOccupied(false);
            return SpotResult.SUCCESS;
        }
        return SpotResult.SPOT_ALREADY_FREE;
    }

    public void showReport() {
        for(int i = 1; i <= numFloors; i++) {
            ParkingFloor parkingFloor = new ParkingFloor(i, 20);
            parkingFloors.add(parkingFloor);
        }
        for(int i = 0; i < numFloors; i++) {
            for(int j = 0; j < numSpotsOnEachFloor; j++) {
                System.out.print(parkingFloors.get(i).floorSpots.get(j).getSpotId() + "[" + (parkingFloors.get(i).floorSpots.get(j).isOccupied() ? 'O' : 'A') + "] ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
