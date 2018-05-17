package com.n26.statistics.service;

import com.n26.statistics.domain.Statistics;
import com.n26.statistics.domain.Transaction;

public interface TransactionService {

    /**
     * Store the transaction.
     *
     * @param transaction
     */
    void add(Transaction transaction);

    /**
     * Returns the statistics of the last 60 seconds transactions in O(1) time complexity.
     *
     * @return Statistics
     */
    Statistics statistics();

    /**
     * At every 100 milliseconds, this method removeOld gets invoked by the scheduler
     * {@link TransactionCleaner#cleanOldTransactions()} and its job is to clear the expired transactions and compute
     * the statistics of the latest 60 seconds of transactions.
     */
    void removeOld();
}
