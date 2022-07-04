package com.example.eshopback.model.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter@Setter
@Builder
public class ErrorBody {
    private HttpStatus status;
    private String message;
    private Date timeline;
}
