package de.mrfrey.adsbmonitor.ui.flight

import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.Link

class FlightResources extends CollectionModel<FlightResource> {
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
