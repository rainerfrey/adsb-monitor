package de.mrfrey.adsbmonitor.ui.flight

import org.springframework.hateoas.Link
import org.springframework.hateoas.Resources

class FlightResources extends Resources<FlightResource> {
    FlightResources(Iterable<FlightResource> content, Link... links) {
        super(content, links)
    }

    FlightResources(Iterable<FlightResource> content, Iterable<Link> links) {
        super(content, links)
    }

    Date lastTimestamp
    Date from
    Date to
}
