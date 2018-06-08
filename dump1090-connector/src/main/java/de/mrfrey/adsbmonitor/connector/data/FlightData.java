package de.mrfrey.adsbmonitor.connector.data;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class FlightData {

    private String flightId;
    private long altitude;
    private boolean onGround = true;
    private String icao;
    private double longitude;
    private long heading;
    private double latitude;
    private long speed;
    private long timestamp;

    public static FlightData fromJSON(Map json) {
        FlightData data = new FlightData();

        // Data without flightId is unusable.
        Object flightId = json.get("flight");
        if (flightId != null && StringUtils.isNotBlank(flightId.toString())) {
            data.flightId = StringUtils.trim(flightId.toString());
        }

        Object altitude = json.get("altitude");
        if (altitude != null) {
            if (altitude.equals("ground")) {
                data.onGround = true;
                data.altitude = 0;
            } else {
                data.altitude = ((Number) altitude).longValue();
                data.onGround = false;
            }
        }
        data.icao = (String) json.get("hex");
        if (json.get("lon") != null)
            data.longitude = (double) json.get("lon");
        if (json.get("track") != null)
            data.heading = (long) json.get("track");
        if (json.get("lat") != null)
            data.latitude = (double) json.get("lat");
        if (json.get("speed") != null)
            data.speed = (long) json.get("speed");
        return data;
    }

    public boolean isComplete() {
        return StringUtils.isNotBlank(flightId)
                && longitude != 0.0
                && latitude != 0.0;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("{")
                .append("\"flightId\":\"").append(flightId).append("\",");
        if (onGround)
            result.append("\"altitude\":\"onGround\",");
        else
            result.append("\"altitude\":").append(altitude).append(",");
        result
                .append("\"icao\":\"").append(icao).append("\",")
                .append("\"longitude\":").append(longitude).append(",")
                .append("\"heading\":").append(heading).append(",")
                .append("\"latitude\":").append(latitude).append(",")
                .append("\"speed\":").append(speed).append(",")
                .append("\"timestamp\":").append(timestamp)
                .append("}");
        return result.toString();
    }


    public Map toMap() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("flightId", flightId);
        map.put("icao", icao);
        map.put("latitude", latitude);
        map.put("longitude", longitude);
        map.put("altitude", altitude);
        map.put("onGround", onGround);
        map.put("heading", heading);
        map.put("speed", speed);
        map.put("timestamp", timestamp);
        return map;
    }

    public Map toMapWithoutTimestamp() {
        Map result = this.toMap();
        result.remove("timestamp");
        return result;
    }

    public boolean equals(FlightData other) {
        if (other == null) {
            return false;
        }
        return this.toMapWithoutTimestamp().equals(other.toMapWithoutTimestamp());
    }

    public String getFlightId() {
        return flightId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getIcao() {
        return icao;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}
