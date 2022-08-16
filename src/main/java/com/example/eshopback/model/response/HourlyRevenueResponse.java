package com.example.eshopback.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
public class HourlyRevenueResponse {
    private String hour;
    private double orderSum;
    private double revenueSum;
}
