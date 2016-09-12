package de.mrfrey.adsbmonitor.ui.flight

import com.fasterxml.jackson.annotation.JsonProperty
import de.mrfrey.adsbmonitor.ui.aircraft.Aircraft
import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.core.Relation

@Relation(collectionRelation = "flights")
class FlightResource extends ResourceSupport {
    @JsonProperty("id")
    String flightId
    String icao
    Aircraft aircraft
    long altitude
    long speed
    double heading
    Date lastTimestamp
    List<Point> positions = []
}

class Point {
    double latitude
    double longitude
    long altitude
    Date timestamp
}