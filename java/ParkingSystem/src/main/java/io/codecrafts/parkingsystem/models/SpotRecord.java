package io.codecrafts.parkingsystem.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Entity
public class SpotRecord {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    Spot spot;

    private String vehicleRego;

    LocalDateTime timeIn, timeOut;

    public SpotRecord() {

    }

    public SpotRecord(Spot spot, String vehicleRego, LocalDateTime timeIn) {
        this.spot = spot;
        this.vehicleRego = vehicleRego;
        this.timeIn = timeIn;
        this.timeOut = null;
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public String getVehicleRego() {
        return vehicleRego;
    }

    public void setVehicleRego(String vehicleRego) {
        this.vehicleRego = vehicleRego;
    }

    public LocalDateTime getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(LocalDateTime timeIn) {
        this.timeIn = timeIn;
    }

    public LocalDateTime getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(LocalDateTime timeOut) {
        this.timeOut = timeOut;
    }

    public void report() {
        if (timeOut == null) {
            System.out.println("NA");
        } else {
            Duration dur = Duration.between(timeIn, timeOut);
            long millis = dur.toMillis();

            long hrs = TimeUnit.MILLISECONDS.toHours(millis);
            long mins = TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis));
            long secs = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            System.out.println(String.format("%s[%s]: TimeIn: %s, TimeOut: %s, Duration %02d:%02d:%02d", spot.getSpotId(), getVehicleRego(), timeIn.format(formatter), timeOut.format(formatter), hrs, mins, secs));
        }
    }
}
