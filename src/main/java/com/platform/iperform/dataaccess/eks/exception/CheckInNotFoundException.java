package com.platform.iperform.dataaccess.eks.exception;

public class CheckInNotFoundException extends RuntimeException{
    public CheckInNotFoundException() {
    }

    public CheckInNotFoundException(String message) {
        super(message);
    }
}
