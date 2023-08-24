package com.seerbit.transactionservice.core.exceptions;

import com.seerbit.transactionservice.core.exceptions.model.ErrorDetail;
import com.seerbit.transactionservice.model.AppResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * Project title: transaction-service
 *
 * @author johnadeshola
 * Date: 8/23/23
 * Time: 7:01 PM
 */
@RestControllerAdvice
public class ResourceAdvice {

    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<AppResponse<ErrorDetail>> handleCustomException(CustomException ex, WebRequest request) {
        ErrorDetail errorDetails = ErrorDetail.builder()
                .message(ex.getMessage())
                .code(ex.getStatus().value())
                .details(request.getDescription(true))
                .timestamp(new Date())
                .build();
        return ResponseEntity.status(errorDetails.getCode()).body(AppResponse.<ErrorDetail>builder()
                .message(errorDetails.getMessage())
                .status(HttpStatus.valueOf(errorDetails.getCode()))
                .error(errorDetails)
                .build());
    }
}
