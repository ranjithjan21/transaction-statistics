package com.n26.statistics.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TransactionCleaner {

    private final TransactionService transactionService;

    public TransactionCleaner(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * At every 100 milliseconds, this method removeOld gets invoked by the scheduler and its job is to clear the
     * expired transactions and compute the statistics of the latest 60 seconds of transactions.
     */
    @Scheduled(fixedRate = 100)
    public void cleanOldTransactions() {
        transactionService.removeOld();
    }
}
