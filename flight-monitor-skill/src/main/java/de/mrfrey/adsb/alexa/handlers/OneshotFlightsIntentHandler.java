package de.mrfrey.adsb.alexa.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaInvokerFactory;
import de.mrfrey.adsb.alexa.flight.Flight;
import de.mrfrey.adsb.alexa.flight.FlightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static java.util.Comparator.comparingDouble;
import static java.util.stream.Collectors.joining;

public class OneshotFlightsIntentHandler implements RequestHandler {
    private static final Logger log = LoggerFactory.getLogger( OneshotFlightsIntentHandler.class );

    private final AWSLambda client;

    public OneshotFlightsIntentHandler() {
        client = AWSLambdaClientBuilder.standard().withRegion( Regions.EU_CENTRAL_1 ).build();
    }

    @Override
    public boolean canHandle( HandlerInput input ) {
        return input.matches( intentName( "OneshotFlightRequest" ) );
    }

    @Override
    public Optional<Response> handle( HandlerInput input ) {
        int limit = 5;
        String speech;
        List<Flight> flights = fetchFlights();
        log.debug( "Found flights: {}", flights.size() );
        if ( flights.isEmpty() ) {
            speech = "Ich habe keine Flugzeuge gefunden.";
        }
        else {
            logFlights( flights, limit );
            speech = getFlightsText( flights, limit );
        }
        return input.getResponseBuilder().withSpeech( speech ).build();
    }

    private String getFlightsText( List<Flight> flights, int limit ) {
        String flightsText = flights.stream()
                                    .limit( limit )
                                    .sorted( comparingDouble( Flight::getDistanceFromHome ) )
                                    .map( OneshotFlightsIntentHandler::toSpeechString )
                                    .collect( joining( "</p>\n<p>", "<p>", "</p>" ) );
        return String.format( "<speak>Diese Flugzeuge sind in deiner Nähe: %s</speak>", flightsText );

    }

    private List<Flight> fetchFlights() {
        FlightService flightService = LambdaInvokerFactory.builder().lambdaClient( client ).build( FlightService.class );
        return flightService.fetchFlights();
    }

    private static String toSpeechString( Flight flight ) {
        return String.format( Locale.GERMANY, "In <say-as interpret-as=\"unit\">%.1fkm</say-as> Entfernung <break/> %s von %s", flight.getDistanceFromHome(), flight.getModel(), flight.getOperator() );
    }

    private void logFlights( List<Flight> flights, int limit ) {
        flights.stream().limit( limit ).forEach( flight ->
                                                     log.debug( "flight: {} airline: {}", flight.getFlightId(), flight.getOperator() )
        );

    }

}
