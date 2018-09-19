package de.mrfrey.adsb.aws.flights.util;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AmazonDynamoDBException;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTimeToLiveRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTimeToLiveResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.TimeToLiveSpecification;
import com.amazonaws.services.dynamodbv2.model.UpdateTimeToLiveRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateTimeToLiveResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.amazonaws.services.dynamodbv2.model.KeyType.HASH;

public class TableDefinition {
    private static final Logger log = LogManager.getFormatterLogger( TableDefinition.class );
    public static final String TABLE = "flights";
    public static final String KEY = "flight_id";
    public static final String TTL_ATTRIBUTE = "expires";
    private final AmazonDynamoDB client;

    public TableDefinition( AmazonDynamoDB client ) {
        this.client = client;
    }

    public void createTable() {
        CreateTableRequest createTableRequest = new CreateTableRequest()
            .withTableName( TABLE )
            .withKeySchema( new KeySchemaElement().withKeyType( HASH ).withAttributeName( KEY ) )
            .withAttributeDefinitions(
                new AttributeDefinition().withAttributeName( KEY ).withAttributeType( ScalarAttributeType.S )
            )
            .withProvisionedThroughput( new ProvisionedThroughput( 3l, 3l ) );
        log.info( "Create table %s", createTableRequest );
        CreateTableResult table = client.createTable( createTableRequest );
        log.info( "Create table result %s", table );
    }

    public void updateTTL() {
        UpdateTimeToLiveRequest updateTimeToLiveRequest = new UpdateTimeToLiveRequest()
            .withTableName( TABLE )
            .withTimeToLiveSpecification( new TimeToLiveSpecification()
                                              .withAttributeName( TTL_ATTRIBUTE )
                                              .withEnabled( true )
            );
        log.info( "Update ttl %s", updateTimeToLiveRequest );
        UpdateTimeToLiveResult updateTimeToLiveResult = client.updateTimeToLive( updateTimeToLiveRequest );
        log.info( "Update ttl result %s", updateTimeToLiveResult );
    }

    public void configureAutoscaling() {
    }

    public void setup() {
        try {
            DescribeTableResult result = client.describeTable( TABLE );
            log.info( "Found table %s", result );
        } catch ( ResourceNotFoundException x ) {
            createTable();
            log.debug( "waiting for table to be available" );
            try {
                Thread.sleep( 2000 );
            } catch ( InterruptedException e ) {
                Thread.currentThread().interrupt();
            }
        }

        try {
            DescribeTimeToLiveResult ttlResult = client.describeTimeToLive( new DescribeTimeToLiveRequest().withTableName( TABLE ) );
            log.info( "Found TTL settings %s", ttlResult );
            if ( ttlResult.getTimeToLiveDescription().getTimeToLiveStatus() == "DISABLED" ) {
                updateTTL();
            }
        } catch ( ResourceNotFoundException x ) {
            log.error( "Did not find table for ttl settings: %s", x.getMessage(), x );
        } catch ( AmazonDynamoDBException x ) {
            if ( x.getErrorCode().equals( "UnknownOperationException" ) ) {
                log.warn( "TTL not supported by this endpoint (e.g. local DynamoDB): %s", x.getMessage(), x );
            }
            else {
                throw x;
            }
        }
    }
}
