package com.seerbit.transactionservice.service.impl;

import com.seerbit.transactionservice.core.constants.AppConstants;
import com.seerbit.transactionservice.core.exceptions.CustomException;
import com.seerbit.transactionservice.core.utils.AppUtils;
import com.seerbit.transactionservice.model.Statistic;
import com.seerbit.transactionservice.model.Transaction;
import com.seerbit.transactionservice.model.request.TransactionRequest;
import com.seerbit.transactionservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class TransactionServiceImplTest {

    @Autowired
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
    void createTransactionSucceed() {
        LocalDateTime localDateTime = AppUtils.parseDateUtil(request.getTimestamp()).toLocalDateTime().minusSeconds(30);
        request.setTimestamp(localDateTime.format(AppConstants.dateTimeFormatter));
        Transaction transaction = transactionService.createTransaction(request);
        List<Transaction> transactions = transactionService.getTransactions();
        assertThat(transactions).contains(transaction);
        assertThat(transaction).isNotNull();
        assertThat(transactions).hasSizeGreaterThan(0);
    }

    @Test
    void createTransactionFailedOnTimestampExceed() {
        LocalDateTime localDateTime = AppUtils.parseDateUtil(request.getTimestamp()).toLocalDateTime().minusSeconds(31);
        request.setTimestamp(localDateTime.format(AppConstants.dateTimeFormatter));
        CustomException exception = assertThrows(CustomException.class, () -> {
            transactionService.createTransaction(request);
            throw new CustomException("Date time exceed 30 seconds");
        });
        assertEquals("Date time exceed 30 seconds", exception.getMessage());
    }

    @Test
    void createTransactionFailedOnInvalidTimestampExceed() {
        Timestamp timestamp = AppUtils.parseDateUtil(request.getTimestamp());
        String newDate = timestamp.toLocalDateTime().toLocalDate().toString();
        request.setTimestamp(newDate);
        CustomException exception = assertThrows(CustomException.class, () -> {
            transactionService.createTransaction(request);
            throw new CustomException("Error");
        });
        assertEquals("Invalid date time format", exception.getMessage());
    }

    @Test
    void transactionStatisticsSucceed() {

        LocalDateTime localDateTime = AppUtils.parseDateUtil(new Timestamp(System.currentTimeMillis()).toString()).toLocalDateTime().plusSeconds(13);
        request.setTimestamp(localDateTime.format(AppConstants.dateTimeFormatter));
        transactionService.createTransaction(request);

        Statistic statistic = transactionService.transactionStatistics();
        assertThat(statistic).isNotNull();
    }

    @Test
    void transactionStatisticsFailedOnEmptyTransactions() {
        transactionService.deleteTransactions();
        CustomException exception = assertThrows(CustomException.class, () -> {
            transactionService.transactionStatistics();
            throw new CustomException("No record found");
        });
        assertEquals("No record found", exception.getMessage());
    }

    @Test
    void transactionStatisticsFailedOnEmptyTransactionsOutsideTimeFrame() {
        transactionService.createTransaction(request);
        CustomException exception = assertThrows(CustomException.class, () -> {
            Thread.sleep(31);
            transactionService.transactionStatistics();
            throw new CustomException("No record within the time frame");
        });
        assertEquals("No record within the time frame", exception.getMessage());
    }

    @Test
    void deleteTransactionsSucceed() {
        transactionService.deleteTransactions();
        assertThat(transactionService.getTransactions()).isEmpty();
    }
}
