package com.mn.aston_test.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadPinException extends ResponseStatusException {

    public BadPinException() {
        super(HttpStatus.BAD_REQUEST, "Pin must be a four-digit integer");
    }

}
