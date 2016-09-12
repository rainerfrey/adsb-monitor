package de.mrfrey.adsbmonitor.ui.aircraft

import org.springframework.data.repository.Repository

import java.util.stream.Stream

interface AircraftRepository extends Repository<Aircraft, String> {
    Aircraft findOne(String id)

    Stream<Aircraft> findAll()
}