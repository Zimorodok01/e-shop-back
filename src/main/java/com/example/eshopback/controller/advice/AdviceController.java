package com.example.eshopback.controller.advice;

import com.example.eshopback.model.exception.ErrorBody;
import com.example.eshopback.model.exception.ErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class AdviceController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> buildErrorResponse(ErrorException exception) {
        exception.setTimeline(new Date());
        return new ResponseEntity<>(ErrorBody.builder()
                .message(exception.getMessage())
                .status(exception.getStatus())
                .timeline(exception.getTimeline()).build(), exception.getStatus());
    }
}
