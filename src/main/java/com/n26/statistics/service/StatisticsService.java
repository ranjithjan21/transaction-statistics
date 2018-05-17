package com.n26.statistics.service;

import com.n26.statistics.domain.Statistics;
import com.n26.statistics.domain.Transaction;

import java.util.List;

public interface StatisticsService {

    /**
     * Calculates the statistics of list of transactions.
     *
     * @param transactions
     * @return Statistics
     */
    Statistics calculate(List<Transaction> transactions);
}
