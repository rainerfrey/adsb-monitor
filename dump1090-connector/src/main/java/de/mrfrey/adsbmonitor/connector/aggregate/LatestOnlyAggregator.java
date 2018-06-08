package de.mrfrey.adsbmonitor.connector.aggregate;

import org.springframework.integration.aggregator.AbstractAggregatingMessageGroupProcessor;
import org.springframework.integration.store.MessageGroup;
import org.springframework.messaging.Message;

import java.util.Comparator;
import java.util.Map;

public class LatestOnlyAggregator extends AbstractAggregatingMessageGroupProcessor {
    @Override
    protected Object aggregatePayloads(MessageGroup group, Map<String, Object> defaultHeaders) {
        return group.getMessages().stream().sorted(
                Comparator.<Message<?>, Long>comparing(m -> m.getHeaders().getTimestamp()).reversed()
        ).findFirst().get().getPayload();
    }
}
