package de.mrfrey.adsb.alexa.flight;

import com.amazonaws.services.lambda.invoke.LambdaFunction;

import java.util.List;

public interface FlightService {
    @LambdaFunction( functionName = "getflights" )
    List<Flight> fetchFlights();
}
