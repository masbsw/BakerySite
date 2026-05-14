package com.example.bakery.models;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product")
public class Product  {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "product_name")
    private String productName;
    @Column(name = "product_description")
    private String productDescription;
    @Column(name = "product_compound")
    private String productCompound;
    @Column(name = "product_price")
    private float productPrice;
    @Column(name = "product_weight")
    private float productWeight;
    @Column(name = "product_quantity")
    private int productQuantity;
    @Column(name = "product_img")
    private String productImg;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
