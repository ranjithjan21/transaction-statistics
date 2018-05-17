package com.n26.statistics.service

import com.n26.statistics.domain.Transaction
import spock.lang.Specification
import spock.lang.Subject

import java.time.ZoneOffset
import java.time.ZonedDateTime

class DefaultStatisticsServiceSpec extends Specification {

    @Subject
    private StatisticsService statisticsService

    def setup() {
        statisticsService = new DefaultStatisticsService()
    }

    def "verify calculate measure the statistics from list of transactions"() {
        given:
        def transactions = [new Transaction(ZonedDateTime.now(ZoneOffset.UTC), 100),
                            new Transaction(ZonedDateTime.now(ZoneOffset.UTC), 50),
                            new Transaction(ZonedDateTime.now(ZoneOffset.UTC), 150),
                            new Transaction(ZonedDateTime.now(ZoneOffset.UTC), 200)]
        when:
        def statistics = statisticsService.calculate(transactions)

        then:
        statistics
        statistics.sum == 500.0
        statistics.avg == 125.0
        statistics.min == 50.0
        statistics.max == 200.0
        statistics.count == 4
    }
}
