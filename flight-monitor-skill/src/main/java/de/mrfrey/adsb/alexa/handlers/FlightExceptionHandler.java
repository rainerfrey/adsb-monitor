package de.mrfrey.adsb.alexa.handlers;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class FlightExceptionHandler implements ExceptionHandler {
    Logger log = LoggerFactory.getLogger( FlightExceptionHandler.class );

    @Override
    public boolean canHandle( HandlerInput input, Throwable throwable ) {
        return throwable instanceof RuntimeException;
    }

    @Override
    public Optional<Response> handle( HandlerInput input, Throwable throwable ) {
        log.error( throwable.toString(), throwable );
        return input.getResponseBuilder()
                    .withSpeech( "<speak><say-as interpret-as\"interjection\">ach du meine güte</say-as><break/>Das hat nicht geklappt.</speak>" )
                    .build();
    }
}
