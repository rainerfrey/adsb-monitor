package de.mrfrey.adsb.aws.data;

import java.time.Instant;

public class Flight {
    private String flightId;
    private Instant timestamp;
    private String registration;
    private String model;
    private String operator;
    private Double distanceFromHome;

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Double getDistanceFromHome() {
        return distanceFromHome;
    }

    public void setDistanceFromHome( Double distanceFromHome ) {
        this.distanceFromHome = distanceFromHome;
    }
}
