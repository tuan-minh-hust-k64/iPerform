package com.platform.iperform.dataaccess.eks.exception;

public class EksNotFoundException extends RuntimeException{
    public EksNotFoundException() {
        super();
    }

    public EksNotFoundException(String message) {
        super(message);
    }
}
