package com.seerbit.transactionservice.core.exceptions.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Project title: transaction-service
 *
 * @author johnadeshola
 * Date: 8/23/23
 * Time: 7:01 PM
 */
@Setter
@Getter
@NoArgsConstructor
@Builder
public class ErrorDetail implements Serializable {

    private Date timestamp;
    private String message;
    private String details;
    private int code;
    private List<ValidationError> validation;

    public ErrorDetail(Date timestamp, String message, String details, int code) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.code = code;
    }

    public ErrorDetail(Date timestamp, String message, String details, int code, List<ValidationError> validation) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.code = code;
        this.validation = validation;
    }
}
