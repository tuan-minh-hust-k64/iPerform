package com.platform.iperform.common.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.platform.iperform.common.exception.ErrorCommon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorCommon handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ErrorCommon.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message("Unexpected err!")
                .build();
    }
    @ResponseBody
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCommon handleExceptionDataInValid(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ErrorCommon.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Data invalid!")
                .build();
    }
    @ResponseBody
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorCommon handleExceptionInvalidException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ErrorCommon.builder()
                .code(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Data invalid format!")
                .build();
    }
}
