package de.mrfrey.adsb.aws.flights.lambda;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTableMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import de.mrfrey.adsb.aws.flights.data.Flight;
import de.mrfrey.adsb.aws.flights.util.TableDefinition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class GetFlights implements RequestHandler<Object, List<Flight>> {
    private static final Logger log = LogManager.getFormatterLogger( GetFlights.class );
    private DynamoDBTableMapper<Flight, String, ?> tableMapper;
    private TableDefinition tableDefinition;

    public GetFlights() {
        initDynamoDB();
        tableDefinition.setup();
    }

    @Override
    public List<Flight> handleRequest( Object input, Context context ) {
        return getFlights();
    }

    public List<Flight> getFlights() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<Flight> flights = tableMapper.scan( scanExpression );
        log.debug( "Flights %d", flights.size() );
        return flights.stream().sorted( comparing( Flight::getTimestamp ).reversed() ).collect( toList() );
    }

    private void initDynamoDB() {
//        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", Regions.EU_CENTRAL_1.getName());
//        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withEndpointConfiguration(endpointConfiguration).build();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion( Regions.EU_CENTRAL_1 ).build();
        DynamoDBMapper mapper = new DynamoDBMapper( client );
        this.tableMapper = mapper.newTableMapper( Flight.class );
        this.tableDefinition = new TableDefinition( client );
    }

}
