package de.mrfrey.adsbmonitor.ui.position

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "positionData")
class Position {
    @Id
    ObjectId id

    String flightId
    String icao
    double latitude
    double longitude
    long altitude
    boolean onGround
    double heading
    long speed
    long timestamp
}
