package com.seerbit.transactionservice.resource;

import com.seerbit.transactionservice.core.constants.AppConstants;
import com.seerbit.transactionservice.core.utils.AppUtils;
import com.seerbit.transactionservice.model.request.TransactionRequest;
import com.seerbit.transactionservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionResource.class)
class TransactionResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private TransactionRequest request;

    @BeforeEach
    void setUp() {
        request = TransactionRequest.builder()
                .amount("200")
                .timestamp(AppUtils.parseTimestamp(new Timestamp(System.currentTimeMillis())))
                .build();
    }

    @Test
    void createTransactionWithValidInputSucceed() throws Exception {
        LocalDateTime localDateTime = AppUtils.parseDateUtil(request.getTimestamp()).toLocalDateTime().minusSeconds(2);
        request.setTimestamp(localDateTime.format(AppConstants.dateTimeFormatter));
        String json = AppUtils.toJson(request);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(AppConstants.RestMessage.created))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void createTransactionWithInValidInputReturn400() throws Exception {
        LocalDateTime localDateTime = AppUtils.parseDateUtil(request.getTimestamp()).toLocalDateTime().minusSeconds(2);
        request.setTimestamp(localDateTime.format(AppConstants.dateTimeFormatter));
        request.setAmount(null);
        String json = AppUtils.toJson(request);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTransactionVerifyInputSucceed() throws Exception {
        LocalDateTime localDateTime = AppUtils.parseDateUtil(request.getTimestamp()).toLocalDateTime().minusSeconds(2);
        request.setTimestamp(localDateTime.format(AppConstants.dateTimeFormatter));
        String json = AppUtils.toJson(request);

        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
        ArgumentCaptor<TransactionRequest> argumentCaptor = ArgumentCaptor.forClass(TransactionRequest.class);
        verify(transactionService, times(1)).createTransaction(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getAmount()).isEqualTo("200");
        assertThat(argumentCaptor.getValue().getTimestamp()).isEqualTo(request.getTimestamp());
    }

    @Test
    void transactionStatisticsSucceed() throws Exception {
        mockMvc.perform(get("/statistics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(AppConstants.RestMessage.success))
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @Test
    void deleteTransactionsSucceed() throws Exception {
        mockMvc.perform(delete("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(AppConstants.RestMessage.success))
                .andExpect(jsonPath("$.status").value("NO_CONTENT"));
    }
}
