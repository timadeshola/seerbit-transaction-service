package com.seerbit.transactionservice.resource;

import com.seerbit.transactionservice.core.constants.AppConstants;
import com.seerbit.transactionservice.model.AppResponse;
import com.seerbit.transactionservice.model.Statistic;
import com.seerbit.transactionservice.model.request.TransactionRequest;
import com.seerbit.transactionservice.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project title: transaction-service
 *
 * @author johnadeshola
 * Date: 8/23/23
 * Time: 6:13 PM
 */
@RestController
@RequiredArgsConstructor
public class TransactionResource {
    private final TransactionService transactionService;

    @PostMapping("transactions")
    public ResponseEntity<AppResponse<Void>> createTransaction(@RequestBody @Valid TransactionRequest request) {
        transactionService.createTransaction(request);
        return ResponseEntity.status(201).body(AppResponse.<Void>builder()
                .data(null)
                .status(HttpStatus.CREATED)
                .message(AppConstants.RestMessage.created)
                .build());
    }

    @GetMapping("statistics")
    public ResponseEntity<AppResponse<Statistic>> transactionStatistics() {
        Statistic response = transactionService.transactionStatistics();
        return ResponseEntity.ok(AppResponse.<Statistic>builder()
                .data(response)
                .status(HttpStatus.OK)
                .message(AppConstants.RestMessage.success)
                .build());
    }

    @DeleteMapping("transactions")
    public ResponseEntity<AppResponse<Void>> deleteTransactions() {
        transactionService.deleteTransactions();
        return ResponseEntity.ok(AppResponse.<Void>builder()
                .data(null)
                .status(HttpStatus.valueOf(204))
                .message(AppConstants.RestMessage.success)
                .build());
    }
}
