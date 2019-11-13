package com.springapi.service.exception;

public class ServiceException extends Exception {
    protected ServiceException(final String exceptionMessage) {
        super(exceptionMessage);
    }
}
