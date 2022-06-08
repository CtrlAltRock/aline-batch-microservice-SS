package com.smoothstack.alinefinancial.listeners;

import com.smoothstack.alinefinancial.metrics.ChunkMetric;
import com.smoothstack.alinefinancial.metrics.ChunkMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.Instant;


@Slf4j(topic = "StepChunkListener")
public class StepChunkListener implements ChunkListener {

    @Autowired
    ChunkMetrics chunkMetrics;

    private Instant startTime;
    private Instant endTime;
    private Long chunk = 1L;

    //private Timer.Sample timer;


    @Override
    public void beforeChunk(ChunkContext context) {
        //timer = Timer.start(Metrics.globalRegistry);
        startTime = Instant.now();
    }

    @Override
    public void afterChunk(ChunkContext context) {
        //Long duration = timer.stop(Timer.builder("ChunkListener").register(Metrics.globalRegistry));
        endTime = Instant.now();
        chunkMetrics.add(chunk, new ChunkMetric(chunk, Duration.between(startTime, endTime).toMillis()));
        chunk++;

    }

    @Override
    public void afterChunkError(ChunkContext context) {

    }
}
