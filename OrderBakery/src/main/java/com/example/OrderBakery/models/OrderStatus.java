package com.example.OrderBakery.models;

public enum OrderStatus {
    PENDING("в ожидании"),
    COMPLETED("завершен"),
    CANCELLED("отменен");

    private final String value;

    OrderStatus(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
