package com.platform.iperform.dataaccess.comment.exception;

public class QuestionNotFoundException extends RuntimeException{
    public QuestionNotFoundException() {
    }

    public QuestionNotFoundException(String message) {
        super(message);
    }
}
