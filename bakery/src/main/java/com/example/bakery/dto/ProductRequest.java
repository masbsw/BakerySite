package com.example.bakery.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String productName;
    private String productDescription;
    private String productCompound;
    private float productPrice;
    private float productWeight;
    private int productQuantity;
    private String productImg;
    private Long categoryId;

}
