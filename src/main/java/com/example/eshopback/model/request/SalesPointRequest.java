package com.example.eshopback.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter
public class SalesPointRequest {
    private String pointName;
    private List<Long> employees;
}
