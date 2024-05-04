package com.example.secumix.security.modelapp.response;

import lombok.Data;
@Data
public class TradeResponse {

    private int tradeId;

    private String title;

    private String date;

    private long cost;

    private String cateName;
}
