package de.mrfrey.adsbmonitor.ui.flight

import com.fasterxml.jackson.annotation.JsonProperty
import de.mrfrey.adsbmonitor.ui.aircraft.Aircraft
import org.springframework.hateoas.RepresentationModel
import org.springframework.hateoas.server.core.Relation

@Relation(collectionRelation = "flights")
class FlightResource extends RepresentationModel {
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