package com.n26.statistics.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.n26.statistics.api.TransactionController
import com.n26.statistics.domain.Statistics
import com.n26.statistics.domain.Transaction
import com.n26.statistics.service.TransactionService
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import spock.lang.Subject

import java.time.ZoneOffset
import java.time.ZonedDateTime

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class TransactionControllerSpec extends Specification {

    @Subject
    private TransactionController transactionController

    private TransactionService transactionService
    private MockMvc mockMvc
    private ObjectMapper objectMapper

    def setup() {
        transactionService = Mock(TransactionService)
        transactionController = new TransactionController(60, transactionService)
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build()
        objectMapper = new ObjectMapper()
        objectMapper.registerModules(Arrays.asList(new Jdk8Module(), new JavaTimeModule()))
    }

    def "verify transactions api creates transaction and returns 201 for valid timestamp"() {
        given:
        def transaction = new Transaction(ZonedDateTime.now(ZoneOffset.UTC), 30)

        when:
        def result = mockMvc.perform(post("/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(transaction)))

        then:
        1 * transactionService.add(*_)

        result.andExpect(status().isCreated())
    }

    def "verify transactions api returns 204 when timestamp is expired"() {
        given:
        def transaction = new Transaction(ZonedDateTime.now(ZoneOffset.UTC).minusSeconds(65), 30)

        when:
        def result = mockMvc.perform(post("/transactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(transaction)))

        then:
        1 * transactionService.add(*_)

        result.andExpect(status().isNoContent())
    }

    def "verify statistics api returns 200 with measured statistics"() {
        given:
        def statistics = Statistics.builder().avg(50).min(50).count(3).max(50).sum(50).build()

        when:
        def result = mockMvc.perform(get("/statistics"))

        then:
        1 * transactionService.statistics() >> statistics

        result.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(content().string(objectMapper.writeValueAsString(statistics)))
    }
}
