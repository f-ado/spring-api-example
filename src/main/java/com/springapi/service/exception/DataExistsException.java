package com.springapi.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User with given username/email already exists")
public class DataExistsException extends Exception {
    public DataExistsException() {}
}
