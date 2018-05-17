package com.n26.statistics.service

import com.n26.statistics.domain.Statistics
import com.n26.statistics.domain.Transaction
import spock.lang.Specification
import spock.lang.Subject

import java.time.ZoneOffset
import java.time.ZonedDateTime

class DefaultTransactionServiceSpec extends Specification {

    @Subject
    private TransactionService transactionService

    private StatisticsService statisticsService

    def setup() {
        statisticsService = Mock(StatisticsService)
        transactionService = new DefaultTransactionService(60, statisticsService)
    }

    def "verify transaction stored without any error"() {
        when:
        transactionService.add(new Transaction(ZonedDateTime.now(ZoneOffset.UTC), 50))

        then:
        noExceptionThrown()
    }

    def "verify statistics returns a measurement of transactions without any error"() {
        given:
        def measuredStatistics = Statistics.builder().avg(50).min(50).count(3).max(50).sum(50).build()
        when:
        transactionService.removeOld()
        def statistics = transactionService.statistics()

        then:
        statisticsService.calculate(_ as List) >> measuredStatistics

        noExceptionThrown()
        statistics == measuredStatistics
    }
}
