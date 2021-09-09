package parkingsystem.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Spot {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Column
    int spotId;

    @NotNull
    @Column
    int floorId;

    @Column
    boolean occupied;

    public Spot() {
    }

    public Spot(int spotId, int floorId, boolean occupied) {
        this.spotId = spotId;
        this.floorId = floorId;
        this.occupied = occupied;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public int getFloorId() {
        return floorId;
    }

    public void setFloorId(int floorId) {
        this.floorId = floorId;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
