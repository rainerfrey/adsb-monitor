package de.mrfrey.adsbmonitor.connector.data.aircraft;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface AircraftRepository extends Repository<Aircraft, String> {
    Optional<Aircraft> findById( String icao );
}
