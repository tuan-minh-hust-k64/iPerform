package com.platform.iperform.common.exception;

public class CommentNotFoundException extends RuntimeException{
    public CommentNotFoundException() {
        super();
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}
