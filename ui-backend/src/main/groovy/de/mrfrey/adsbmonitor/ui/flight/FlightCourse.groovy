package de.mrfrey.adsbmonitor.ui.flight

import de.mrfrey.adsbmonitor.ui.aircraft.Aircraft
import de.mrfrey.adsbmonitor.ui.position.Position

import static java.util.Comparator.comparing


class FlightCourse {
    String flightId
    String icao
    Aircraft aircraft
    long altitude
    long speed
    double heading
    long lastTimestamp
    SortedSet<Position> positions = new TreeSet<>(comparing({ it.timestamp }))

    void add(Position position) {
        if (this.empty) {
            this.flightId = position.flightId
            this.icao = position.icao
        } else if (position.flightId != this.flightId)
            return
        if (positions.empty || position.timestamp > positions.last().timestamp) {
            updateFromPosition(position)
        }
        positions.add(position)
    }

    public void updateFromPosition(Position position) {
        this.altitude = position.altitude
        this.speed = position.speed
        this.heading = position.heading
        this.lastTimestamp = position.timestamp
    }

    public FlightCourse addAll(FlightCourse other) {
        if (other.empty)
            return this
        if (empty)
            other.positions.each { add(it) }
        else {
            positions.addAll(other.positions)
            updateFromPosition(positions.last())
        }
        return this
    }

    public boolean isEmpty() {
        return flightId == null || positions.empty
    }

    FlightCourse addAircraft(Aircraft aircraft) {
        this.aircraft = aircraft
        this
    }
}

class FlightPosition {
    double latitude
    double longitude
    long altitude
    long timestamp
}