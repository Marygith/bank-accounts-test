package com.mn.aston_test.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InsufficientFundsException extends ResponseStatusException {
    public InsufficientFundsException() {
        super(HttpStatus.BAD_REQUEST, "Transaction is not possible, insufficient funds");
    }
}
