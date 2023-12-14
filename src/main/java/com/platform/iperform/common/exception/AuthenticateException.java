package com.platform.iperform.common.exception;

public class AuthenticateException extends RuntimeException{
    public AuthenticateException() {
        super();
    }

    public AuthenticateException(String message) {
        super(message);
    }
}
