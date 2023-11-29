package com.platform.iperform.dataaccess.eks.exception;

public class KeyStepNotFoundException extends RuntimeException{
    public KeyStepNotFoundException() {
    }

    public KeyStepNotFoundException(String message) {
        super(message);
    }
}
