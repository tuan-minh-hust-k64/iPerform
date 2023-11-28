package com.platform.iperform.common.exception;

public class ConfigNotFoundException extends RuntimeException{
    public ConfigNotFoundException() {
        super();
    }

    public ConfigNotFoundException(String message) {
        super(message);
    }
}
