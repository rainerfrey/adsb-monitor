package de.mrfrey.adsbmonitor.ui.flight;

import de.mrfrey.adsbmonitor.ui.aircraft.AircraftRepository;
import de.mrfrey.adsbmonitor.ui.position.Position;
import de.mrfrey.adsbmonitor.ui.position.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;

@Service
public class FlightService {
    @Autowired
    PositionRepository positionRepository;

    @Autowired
    AircraftRepository aircraftRepository;

    public Stream<FlightCourse> getData(Long from, Long to) {


        Map<String, FlightCourse> data = positionRepository.getPositions(from, to)
                .collect(
                        groupingBy(Position::getFlightId, Collector.of(
                                FlightCourse::new,
                                FlightCourse::add,
                                FlightCourse::addAll
                        ))
                );
        return data.values().stream()
                .map(flight -> flight.addAircraft(aircraftRepository.findById(flight.getIcao().toUpperCase())))
                .sorted(comparing(FlightCourse::getLastTimestamp).reversed());
    }
}
