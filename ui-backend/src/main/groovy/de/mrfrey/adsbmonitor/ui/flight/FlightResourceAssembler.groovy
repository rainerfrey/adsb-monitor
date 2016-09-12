package de.mrfrey.adsbmonitor.ui.flight

import org.springframework.hateoas.ResourceAssembler
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.stereotype.Component

@Component
class FlightResourceAssembler implements ResourceAssembler<FlightCourse, FlightResource> {
    @Override
    FlightResource toResource(FlightCourse flight) {
        def resource = new FlightResource()
        resource.flightId = flight.flightId
        resource.icao = flight.icao
        resource.aircraft = flight.aircraft
        resource.altitude = flight.altitude
        resource.speed = flight.speed
        resource.heading = flight.heading
        resource.lastTimestamp = new Date(flight.lastTimestamp)
        flight.positions.each {
            resource.positions << new Point(latitude: it.latitude, longitude: it.longitude, altitude: it.altitude, timestamp: new Date(it.timestamp))
        }
        resource.add(ControllerLinkBuilder.linkTo(FlightDataController).slash(flight.flightId).withSelfRel())
        return resource
    }
}
