package com.n26.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class TransactionStatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionStatisticsApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper(final Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {

        return jackson2ObjectMapperBuilder
                .modules(Arrays.asList(new Jdk8Module(), new JavaTimeModule()))
                .timeZone(TimeZone.getTimeZone(ZoneOffset.UTC))
                .build();
    }
}
