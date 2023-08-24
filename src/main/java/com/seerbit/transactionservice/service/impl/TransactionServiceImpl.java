package com.seerbit.transactionservice.service.impl;

import com.seerbit.transactionservice.core.exceptions.CustomException;
import com.seerbit.transactionservice.core.utils.AppUtils;
import com.seerbit.transactionservice.model.Statistic;
import com.seerbit.transactionservice.model.Transaction;
import com.seerbit.transactionservice.model.request.TransactionRequest;
import com.seerbit.transactionservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Project title: transaction-service
 *
 * @author johnadeshola
 * Date: 8/23/23
 * Time: 5:19 PM
 */
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    List<Transaction> transactions = new CopyOnWriteArrayList<>();

    @Override
    public Transaction createTransaction(TransactionRequest request) {

        if (!AppUtils.isValidDate(request.getTimestamp())) {
            throw new CustomException("Invalid date time format", HttpStatus.valueOf(400));
        }

        if (AppUtils.timestampCheck(new Timestamp(System.currentTimeMillis()), AppUtils.parseDateUtil(request.getTimestamp()), 300)) {
            throw new CustomException("Date time exceed 30 seconds", HttpStatus.valueOf(204));
        }

        if (AppUtils.parseDateUtil(request.getTimestamp()).getTime() > System.currentTimeMillis()) {
            throw new CustomException("Transaction time is in the future", HttpStatus.valueOf(422));
        }

        Transaction transaction = Transaction.builder()
                .amount(new BigDecimal(request.getAmount()))
                .timestamp(AppUtils.parseDateUtil(request.getTimestamp()))
                .build();
        transactions.add(transaction);
        return transaction;
    }

    @Override
    public Statistic transactionStatistics() {
        if (this.transactions.isEmpty()) {
            throw new CustomException("No record found", HttpStatus.valueOf(204));
        }

        List<Transaction> transactionList = this.transactions.parallelStream().filter(transaction -> !AppUtils.timestampCheck(new Timestamp(System.currentTimeMillis()), transaction.getTimestamp(), 30)).toList();
        if (transactionList.isEmpty()) {
            throw new CustomException("No record within the time frame", HttpStatus.valueOf(204));
        }

        return Statistic.builder()
                .sum(transactionList.stream().map(Transaction::getAmount).toList().stream().reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP))
                .avg(transactionList.stream().map(Transaction::getAmount).toList().stream().reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(transactions.size()), RoundingMode.HALF_UP))
                .max(transactionList.stream().map(Transaction::getAmount).toList().stream().reduce(BigDecimal.ZERO, BigDecimal::max).setScale(2, RoundingMode.HALF_UP))
                .min(transactionList.stream().map(Transaction::getAmount).toList().stream().min(Comparator.naturalOrder()).orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP))
                .count((long) transactionList.size())
                .build();
    }

    @Override
    public void deleteTransactions() {
        transactions.clear();
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactions;
    }
}
