package de.mrfrey.adsbmonitor.ui.aircraft

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Aircraft {
    @Id
    String icao
    String registration
    Operator operator
    Model model
}

class Operator {
    String icao
    String name
}

class Model {
    String icao
    String name
}