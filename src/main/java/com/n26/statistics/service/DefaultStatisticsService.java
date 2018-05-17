package com.n26.statistics.service;


import com.n26.statistics.domain.Statistics;
import com.n26.statistics.domain.Transaction;
import lombok.val;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultStatisticsService implements StatisticsService {

    @Override
    public Statistics calculate(final List<Transaction> transactions) {
        final val statistics = transactions
            .stream()
            .collect(Collectors.summarizingDouble(Transaction::getAmount));

        return Statistics
            .builder()
            .sum(statistics.getSum())
            .min(statistics.getMin())
            .max(statistics.getMax())
            .count(statistics.getCount())
            .avg(statistics.getAverage())
            .build();
    }
}
