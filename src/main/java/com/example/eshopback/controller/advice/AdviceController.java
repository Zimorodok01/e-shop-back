package com.example.eshopback.controller.advice;

import com.example.eshopback.model.exception.ErrorBody;
import com.example.eshopback.model.exception.ErrorException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class AdviceController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {ErrorException.class})
    public ResponseEntity<?> buildErrorResponse(ErrorException exception) {
        exception.setTimeline(new Date());
        return new ResponseEntity<>(ErrorBody.builder()
                .message(exception.getMessage())
                .status(exception.getStatus())
                .timeline(exception.getTimeline()).build(), exception.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(ErrorBody.builder()
                .message(ex.getMessage())
                .status(status)
                .timeline(new Date()).build(), status);
    }
}
