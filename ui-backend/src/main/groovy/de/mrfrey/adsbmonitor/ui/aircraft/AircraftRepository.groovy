package de.mrfrey.adsbmonitor.ui.aircraft

import org.springframework.data.repository.Repository

import java.util.stream.Stream

interface AircraftRepository extends Repository<Aircraft, String> {
    Optional<Aircraft> findById(String id)

    Stream<Aircraft> findAll()
}