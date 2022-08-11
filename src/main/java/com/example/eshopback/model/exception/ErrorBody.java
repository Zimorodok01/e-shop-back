package com.example.eshopback.model.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.000 ")
    private Date timeline;
}
