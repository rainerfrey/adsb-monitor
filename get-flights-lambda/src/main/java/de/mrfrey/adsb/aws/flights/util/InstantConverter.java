package de.mrfrey.adsb.aws.flights.util;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.Instant;

public class InstantConverter implements DynamoDBTypeConverter<Long, Instant> {
    @Override
    public Long convert(Instant value) {
        return value.getEpochSecond();
    }

    @Override
    public Instant unconvert(Long timestamp) {
        return Instant.ofEpochSecond(timestamp);
    }
}
