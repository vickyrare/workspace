package parkingsystem.models;

public class Spot {
    int spotId;
    boolean occupied;
    Vehicle vehicle;

    public Spot(int spotId, boolean occupied) {
        this.spotId = spotId;
        this.occupied = occupied;
        this.vehicle = null;
    }

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
