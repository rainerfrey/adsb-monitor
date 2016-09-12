package de.mrfrey.adsbmonitor.connector.flow;

import de.mrfrey.adsbmonitor.connector.data.FlightData;
import de.mrfrey.adsbmonitor.connector.util.LRUCache;

import java.util.Collections;
import java.util.Map;


public class DuplicateFilter {
    private Map<String, FlightData> cache = Collections.synchronizedMap(new LRUCache<>(200));

    public boolean duplicate(FlightData data) {
        FlightData last = cache.get(data.getFlightId());
        cache.put(data.getFlightId(), data);
        return data.equals(last);
    }
}
