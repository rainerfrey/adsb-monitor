package de.mrfrey.adsb.alexa.flight;

public class Flight {
    private String flightId;
    private String registration;
    private String model;
    private String operator;
    private Double distanceFromHome;

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId( String flightId ) {
        this.flightId = flightId;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration( String registration ) {
        this.registration = registration;
    }

    public String getModel() {
        return model;
    }

    public void setModel( String model ) {
        this.model = model;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator( String operator ) {
        this.operator = operator;
    }

    public Double getDistanceFromHome() {
        return distanceFromHome;
    }

    public void setDistanceFromHome( Double distanceFromHome ) {
        this.distanceFromHome = distanceFromHome;
    }
}
