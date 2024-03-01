package com.platform.iperform.common.exception.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.platform.iperform.common.exception.AuthenticateException;
import com.platform.iperform.common.exception.ErrorCommon;
import com.platform.iperform.common.exception.HrmsException;
import com.platform.iperform.common.exception.NotFoundException;
import graphql.GraphQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.graphql.client.GraphQlClientException;
import org.springframework.graphql.client.GraphQlTransportException;
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
                .message(exception.getMessage())
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
                .message("Data invalid format!").build();
    }
    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorCommon handleExceptionNotFound(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ErrorCommon.builder()
                .code(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(value = {AuthenticateException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorCommon handleExceptionAuthenticate(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ErrorCommon.builder()
                .code(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .message(exception.getMessage())
                .build();
    }

    @ExceptionHandler(value = {GraphQLException.class, GraphQlClientException.class, GraphQlTransportException.class, HrmsException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorCommon handleGraphQLException(Exception e) {
        log.error("HRMS exception::" + e.getMessage() + e);
        return ErrorCommon.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(e.getMessage())
                .build();
    }
}
