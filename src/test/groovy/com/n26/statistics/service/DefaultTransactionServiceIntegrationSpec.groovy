package com.n26.statistics.service

import com.n26.statistics.domain.Transaction
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Subject

import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.stream.IntStream

class DefaultTransactionServiceIntegrationSpec extends Specification {

    @Subject
    private TransactionService transactionService

    def setup() {
        transactionService = new DefaultTransactionService(60, new DefaultStatisticsService())
        Executors.newScheduledThreadPool(1)//for every 100 milliseconds remove old
                .scheduleAtFixedRate({transactionService.removeOld()}, 0, 100, TimeUnit.MILLISECONDS)
    }

    def "verify statistics"() {

        when:
        IntStream.rangeClosed(1, 10000)
                .parallel()
                .peek({i -> transactionService.add(new Transaction(ZonedDateTime.now(ZoneOffset.UTC).plusNanos(i % 1000) , 50.0))})
                .peek({i ->
                    if (i % 500 == 0) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            assert false
                        }
                    }
                })
                .forEach({i ->
                    if (i % 1000 == 0) {
                        def statistics = transactionService.statistics()
                        assert statistics.avg == 50.0
                        assert statistics.min == 50.0
                        assert statistics.max == 50.0
                        assert statistics.count > 0
                        assert statistics.sum > 50.0
                    }
                })

        then:
        noExceptionThrown()
    }
}
