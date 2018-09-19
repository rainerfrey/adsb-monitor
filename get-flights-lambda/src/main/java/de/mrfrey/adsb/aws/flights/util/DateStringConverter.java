package de.mrfrey.adsb.aws.flights.util;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.Instant;

public class DateStringConverter implements DynamoDBTypeConverter<Long, String> {
    @Override
    public Long convert( String value ) {
        return Instant.parse( value ).getEpochSecond();
    }

    @Override
    public String unconvert( Long value ) {
        return Instant.ofEpochSecond( value ).toString();
    }
}
