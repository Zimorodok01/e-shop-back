package com.example.eshopback.model.response;

public class ErrorResponse {
    public ErrorResponse(Exception e) {
        this.exception = e;
    }

    private Exception exception;
}
