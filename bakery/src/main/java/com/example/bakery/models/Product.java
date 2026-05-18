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
    @Column(name = "product_price", nullable = false)
    private Float productPrice;
    @Column(name = "product_weight", nullable = false)
    private Float productWeight;
    @Column(name = "product_quantity", nullable = false)
    private Integer productQuantity;
    @Column(name = "product_img")
    private String productImg;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
