package com.ePark.entity;

import lombok.Data;

@Data
public class ChargeRequest {

    public enum Currency {
        GBP, EUR, USD;
    }
    private String description;
    private long amount;
    private Currency currency;
    private String customerId;
    private String paymentMethodId;
    private String stripeToken;
    
}