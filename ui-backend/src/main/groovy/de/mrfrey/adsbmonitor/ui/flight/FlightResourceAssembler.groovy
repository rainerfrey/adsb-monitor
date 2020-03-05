package de.mrfrey.adsbmonitor.ui.flight


import org.springframework.hateoas.server.RepresentationModelAssembler
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.stereotype.Component

@Component
class FlightResourceAssembler implements RepresentationModelAssembler<FlightCourse, FlightResource> {
    @Override
    FlightResource toModel(FlightCourse flight) {
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
        resource.add(WebMvcLinkBuilder.linkTo(FlightDataController).slash(flight.flightId).withSelfRel())
        return resource
    }
}
