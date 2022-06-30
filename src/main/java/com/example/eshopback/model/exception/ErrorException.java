package com.example.eshopback.model.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@AllArgsConstructor
@Getter
@Builder
@Setter
public class ErrorException extends RuntimeException {
    private HttpStatus status;
    private String message;
    private Date timeline;
}
