package com.example.bakery.service;


import com.example.bakery.models.Product;
import com.example.bakery.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public  Product createProduct(Product product){
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id){
        return  productRepository.findById(id);
    }

    public Product updateProduct(Long id, Product productDetails){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Product not found"));
        product.setProductName(productDetails.getProductName());
        product.setProductDescription(productDetails.getProductDescription());
        product.setProductCompound(productDetails.getProductCompound());
        product.setProductPrice(productDetails.getProductPrice());
        product.setProductWeight(productDetails.getProductWeight());
        product.setProductQuantity(productDetails.getProductQuantity());
        product.setProductImg(productDetails.getProductImg());
        product.setCategory(productDetails.getCategory());
        return  productRepository.save(product);
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    public List<Product> getProductsByCategoryId(Long categoryId){
        return productRepository.findByCategory_CategoryId(categoryId);
    }

    public List<Product> searchProductsByName(String productName) {
        return productRepository.findByProductNameContainingIgnoreCase(productName);
    }

    public List<Product> getProductsByIds(List<Long> ids) {
        return productRepository.findAllById(ids);
    }

}
