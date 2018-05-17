package com.n26.statistics.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;

/**
 * A Transaction holds timestamp and amount.
 */
@Getter
@ToString
public class Transaction {

    @ApiModelProperty(required = true, dataType = "java.lang.Long")
    private final ZonedDateTime timestamp;

    @ApiModelProperty(required = true)
    private final Double amount;

    @JsonCreator
    public Transaction(
        @JsonProperty("timestamp") final ZonedDateTime timestamp,
        @JsonProperty("amount") final Double amount) {

        this.timestamp = timestamp;
        this.amount = amount;
    }
}