package de.mrfrey.adsbmonitor.connector.data;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AwsData {
        private String flightId;
        private Long timestamp;
        private String registration;
        private String model;
        private String operator;
        private Double distanceFromHome;

    public AwsData(String flightId, Long timestamp) {
        this.flightId = flightId;
        this.timestamp = timestamp;
    }

    public String getFlightId() {
        return flightId;
    }

    public Long getTimestamp() {
        return timestamp;
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

    @Override
    public String toString() {
        return "AwsData{" +
                "flightId='" + flightId + '\'' +
                ", timestamp=" + timestamp +
                ", registration='" + registration + '\'' +
                ", model='" + model + '\'' +
                ", operator='" + operator + '\'' +
                '}';
    }
}
