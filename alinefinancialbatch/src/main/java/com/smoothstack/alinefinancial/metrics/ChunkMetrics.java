package com.smoothstack.alinefinancial.metrics;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "chunk.metrics")
public class ChunkMetrics {

    private HashMap<Long, ChunkMetric> chunkMetricsMap = new HashMap<>();


    @ReadOperation
    public Map<Long, ChunkMetric> chunks() {
        return chunkMetricsMap;
    }

    public void add(Long chunk, ChunkMetric chunkMetric) {
        chunkMetricsMap.put(chunk, chunkMetric);
    }


}
