package io.codecrafts.parkingsystem.models;

public enum SpotResult {

    SPOT_NOT_FOUND("Spot not found"),
    SPOT_ALREADY_IN_USE("Spot already in use"),
    SPOT_ALREADY_FREE("Spot already free"),
    SUCCESS("Success");

    public final String message;

    private SpotResult(String message) {
        this.message = message;
    }
}