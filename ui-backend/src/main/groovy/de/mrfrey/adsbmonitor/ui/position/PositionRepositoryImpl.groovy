package de.mrfrey.adsbmonitor.ui.position


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.util.StreamUtils

import java.util.stream.Stream

import static org.springframework.data.mongodb.core.query.Criteria.where
import static org.springframework.data.mongodb.core.query.Query.query

class PositionRepositoryImpl implements PositionRepositoryCustom {
    @Autowired
    MongoOperations mongoOperations

    @Override
    Stream<Position> getPositions(Long begin, Long end) {
        Criteria timeRange = where("timestamp").gte(begin)
        if (end) {
            timeRange.lte(end)
        }
        Query query = query(timeRange);

        StreamUtils.createStreamFromIterator(mongoOperations.stream(query, Position))
    }
}
