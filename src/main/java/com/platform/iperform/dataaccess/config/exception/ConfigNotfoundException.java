package com.platform.iperform.dataaccess.config.exception;

public class ConfigNotfoundException extends RuntimeException{
    public ConfigNotfoundException() {
    }

    public ConfigNotfoundException(String message) {
        super(message);
    }
}
