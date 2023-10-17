package com.mn.aston_test.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PinIsNotCorrectException extends ResponseStatusException {
    public PinIsNotCorrectException() {
        super(HttpStatus.BAD_REQUEST, "Incorrect pin");
    }
}
