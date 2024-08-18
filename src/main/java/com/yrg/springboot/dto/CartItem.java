package com.yrg.springboot.dto;


import lombok.Data;

@Data
public class CartItem {
    private String cartId;
    private String itemId;
    private String itemName;
    private String userId;
    private String userName;
    private int quantity;
    private double price;
}
