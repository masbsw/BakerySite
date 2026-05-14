package com.example.bakery.repositories;

import com.example.bakery.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_CategoryId(Long categoryId);

    List<Product> findByProductNameContainingIgnoreCase(String productName);



}
