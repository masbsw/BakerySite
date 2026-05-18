package com.example.bakery.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequest {
    private String productName;
    private String productDescription;
    private String productCompound;
    private Float productPrice;
    private Float productWeight;
    private Integer productQuantity;
    private String productImg;
    private Long categoryId;

}
