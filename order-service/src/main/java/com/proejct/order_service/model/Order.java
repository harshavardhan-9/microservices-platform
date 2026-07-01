package com.proejct.order_service.model;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

public class Order {
    private int id;
    private String name;
    private int quantity;
    private double price;
}
