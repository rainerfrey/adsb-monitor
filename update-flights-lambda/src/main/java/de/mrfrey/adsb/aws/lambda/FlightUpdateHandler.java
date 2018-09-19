package de.mrfrey.adsb.aws.lambda;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.AttributeUpdate;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.mrfrey.adsb.aws.data.Flight;
import de.mrfrey.adsb.aws.util.TableDefinition;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static java.time.temporal.ChronoUnit.HOURS;

public class FlightUpdateHandler implements RequestHandler<SNSEvent, Void> {
    private ObjectMapper objectMapper;
    private DynamoDB dynamoDB;

    public FlightUpdateHandler() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule( new JavaTimeModule() );
        objectMapper.disable( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES );
        objectMapper.disable( DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS );
        initDynamoDB();
    }

    private void initDynamoDB() {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion( Regions.EU_CENTRAL_1 ).build();
        this.dynamoDB = new DynamoDB( client );
        new TableDefinition( client ).setup();
    }


    @Override
    public Void handleRequest( SNSEvent input, Context context ) {
        LambdaLogger logger = context.getLogger();
        input.getRecords().forEach( snsRecord -> {
            try {
                logger.log( snsRecord.getSNS().getMessage() );
                Flight flight = objectMapper.readValue( snsRecord.getSNS().getMessage(), Flight.class );
                updateFlight( flight );
                logger.log( "Done updating flight " + flight.getFlightId() );
            } catch ( JsonParseException | JsonMappingException e ) {
                logger.log( e.toString() );
            } catch ( IOException e ) {
                logger.log( e.toString() );
                throw new RuntimeException( e.getMessage(), e );
            }
            catch ( AmazonServiceException x ) {
                logger.log( x.toString() );
                throw x;
            }

        } );
        return null;
    }

    private void updateFlight( Flight flight ) {
        List<AttributeUpdate> updates = new LinkedList<>();
        updates.add( new AttributeUpdate( "timestamp" )
                         .put( flight.getTimestamp().getEpochSecond() ) );
        updates.add( new AttributeUpdate( "expires" ).put(
            flight.getTimestamp().plus( 2, HOURS ).getEpochSecond() ) );
        if ( flight.getRegistration() != null ) {
            updates.add( new AttributeUpdate( "registration" ).put( flight.getRegistration() ) );
        }
        if ( flight.getModel() != null ) {
            updates.add( new AttributeUpdate( "model" ).put( flight.getModel() ) );
        }
        if ( flight.getOperator() != null ) {
            updates.add( new AttributeUpdate( "operator" ).put( flight.getOperator() ) );
        }
        if ( flight.getDistanceFromHome() != null ) {
            updates.add( new AttributeUpdate( "distanceFromHome" ).put( flight.getDistanceFromHome() ) );
        }
        dynamoDB.getTable( "flights" )
                .updateItem( new PrimaryKey( "flight_id", flight.getFlightId() ),
                             updates.stream().toArray( AttributeUpdate[]::new )
                );
    }
}
