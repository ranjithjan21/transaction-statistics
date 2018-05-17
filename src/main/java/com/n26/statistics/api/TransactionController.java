package com.n26.statistics.api;

import com.n26.statistics.domain.Statistics;
import com.n26.statistics.domain.Transaction;
import com.n26.statistics.service.TransactionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static javax.servlet.http.HttpServletResponse.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Api(value = "transaction-statistics",
    description = "Provides APIs to create and get statistics of transactions",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
)
public class TransactionController {

    private static final String TRANSACTIONS_URL = "/transactions";
    private static final String STATISTICS_URL = "/statistics";

    private final Integer expirySeconds;
    private final TransactionService transactionService;

    public TransactionController(
        @Value("${transaction.expirySeconds}") final Integer expirySeconds,
        final TransactionService transactionService) {

        this.expirySeconds = expirySeconds;
        this.transactionService = transactionService;
    }

    @ApiOperation(produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE, value = "Create a transaction.")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE, path = TRANSACTIONS_URL)
    public ResponseEntity<Void> transactions(@RequestBody final Transaction transaction) {
        transactionService.add(transaction);
        if (transaction.getTimestamp().isBefore(ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(expirySeconds))) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(produces = APPLICATION_JSON_UTF8_VALUE, consumes = APPLICATION_JSON_UTF8_VALUE, value = "Get statistics of last 60 seconds transactions in O(1) time complexity.", response = Statistics.class)
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE, path = STATISTICS_URL)
    public Statistics statistics() {
        return transactionService.statistics();
    }
}