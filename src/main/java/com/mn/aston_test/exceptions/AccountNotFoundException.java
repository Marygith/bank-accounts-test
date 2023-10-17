package com.mn.aston_test.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccountNotFoundException extends ResponseStatusException {

    public AccountNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Account with given name was not found");
    }
}
