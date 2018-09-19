package de.mrfrey.adsb.aws.flights.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import de.mrfrey.adsb.aws.flights.util.DateStringConverter;

import java.time.Instant;

@DynamoDBTable( tableName = "flights" )
public class Flight {
    @DynamoDBHashKey( attributeName = "flight_id" )
    private String flightId;
    @DynamoDBTypeConverted( converter = DateStringConverter.class )
    private String timestamp;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp( String timestamp ) {
        this.timestamp = timestamp;
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
