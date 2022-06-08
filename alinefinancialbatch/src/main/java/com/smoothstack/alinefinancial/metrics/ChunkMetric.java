package com.smoothstack.alinefinancial.metrics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChunkMetric {
    private Long chunk;
    private Long duration;
}
