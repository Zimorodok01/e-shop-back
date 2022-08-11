package com.example.eshopback.model.response;

import lombok.*;

import java.util.List;

@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RevenueResponse {
    private double todaySum = 0.0;
    private double weeklySum = 0.0;

    private int todayAmount = 0;
    private int todayCash = 0;
    private int todayElectronic = 0;

    private List<HourlyRevenueResponse> todayOrders;
    private List<HourlyRevenueResponse> weekAgoOrders;
}


