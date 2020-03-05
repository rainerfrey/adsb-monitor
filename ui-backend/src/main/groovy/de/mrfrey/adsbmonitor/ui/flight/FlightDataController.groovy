package de.mrfrey.adsbmonitor.ui.flight

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.CollectionModel
import org.springframework.hateoas.server.core.EmbeddedWrappers
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import static java.util.stream.Collectors.toList
import static org.springframework.web.bind.annotation.RequestMethod.GET

@RestController
@RequestMapping(value = "/positions", produces = [MediaType.APPLICATION_JSON_VALUE, "application/hal+json"])
@CrossOrigin
class FlightDataController {
    @Autowired
    FlightService flightService

    @Autowired
    FlightResourceAssembler flightResourceAssembler

    @RequestMapping(method = GET)
    CollectionModel<FlightResource> getPositionData(
            @RequestParam(value = "from", defaultValue = "15") Integer fromMinutesAgo,
            @RequestParam(value = "to", required = false) Integer toMinutesAgo) {

        Long from = timestampFromMinutes(fromMinutesAgo)
        Long to = timestampFromMinutes(toMinutesAgo)

        def data = flightService.getData(from, to)
        def positions = data.map({ flightResourceAssembler.toModel(it) }).collect(toList())
        def timestamp
        if (positions.isEmpty()) {
            positions = Collections.singletonList(new EmbeddedWrappers(false).emptyCollectionOf(FlightResource))
            timestamp = new Date(from)
        } else {
            timestamp = positions.first().lastTimestamp
        }

        def resources = new FlightResources(positions)
        resources.lastTimestamp = timestamp
        resources.from = new Date(from)
        resources.to = to ? new Date(to) : new Date()
        return resources
    }

    Long timestampFromMinutes(Integer minutesAgo) {
        (minutesAgo != null) ? System.currentTimeMillis() - (minutesAgo * 60 * 1000) : null
    }
}
