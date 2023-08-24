package com.seerbit.transactionservice.service;

import com.seerbit.transactionservice.model.Statistic;
import com.seerbit.transactionservice.model.Transaction;
import com.seerbit.transactionservice.model.request.TransactionRequest;

import java.util.List;

/**
 * Project title: transaction-service
 *
 * @author johnadeshola
 * Date: 8/23/23
 * Time: 5:19 PM
 */
public interface TransactionService {

    public Transaction createTransaction(TransactionRequest request);

    public Statistic transactionStatistics();

    public void deleteTransactions();

    public List<Transaction> getTransactions();
}
