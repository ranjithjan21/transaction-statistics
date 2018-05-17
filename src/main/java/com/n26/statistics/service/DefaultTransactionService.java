package com.n26.statistics.service;

import com.n26.statistics.domain.Statistics;
import com.n26.statistics.domain.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultTransactionService implements TransactionService {

    private final Integer expirySeconds;
    private final StatisticsService statisticsService;
    private final List<Transaction> transactions;

    private Statistics currentStatistics;

    public DefaultTransactionService(
        @Value("${transaction.expirySeconds}") final Integer expirySeconds,
        final StatisticsService statisticsService) {

        this.expirySeconds = expirySeconds;
        this.statisticsService = statisticsService;
        this.transactions = new ArrayList<>();
    }

    @Override
    public synchronized void add(final Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public synchronized Statistics statistics() {
        return currentStatistics;
    }

    @Override
    public synchronized void removeOld() {
        transactions.removeIf(transaction ->
            transaction.getTimestamp().plusSeconds(expirySeconds).isBefore(ZonedDateTime.now(ZoneOffset.UTC)));

        currentStatistics = statisticsService.calculate(transactions);
    }
}