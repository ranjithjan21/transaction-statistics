package com.n26.statistics.domain;

import lombok.Builder;
import lombok.Value;

/**
 * Statistics that holds measurement of last 60 seconds of transactions.
 */
@Value
@Builder
public class Statistics {

    private Double sum;
    private Double avg;
    private Double max;
    private Double min;
    private Long count;
}
