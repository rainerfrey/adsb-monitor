package de.mrfrey.adsbmonitor.connector.data.aircraft;

import org.springframework.data.repository.Repository;

public interface AircraftRepository extends Repository<Aircraft, String> {
    Aircraft findOne(String icao);
}
