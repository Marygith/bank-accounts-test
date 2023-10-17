package com.mn.aston_test.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AccountAlreadyExistsException extends ResponseStatusException {
    public AccountAlreadyExistsException() {
        super(HttpStatus.CONFLICT, "Account with given name already exists");
    }
}
