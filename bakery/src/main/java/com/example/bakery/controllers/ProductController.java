package com.example.bakery.controllers;

import com.example.bakery.dto.ProductRequest;
import com.example.bakery.models.Category;
import com.example.bakery.models.Product;
import com.example.bakery.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class    ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }

    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategoryId(@PathVariable Long categoryId){
        return productService.getProductsByCategoryId(categoryId);
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String name) {
        return productService.searchProductsByName(name);
    }


    @PostMapping
    public Product createProduct(@RequestBody ProductRequest request) {
        Product product = new Product();
        product.setProductName(request.getProductName());
        product.setProductDescription(request.getProductDescription());
        product.setProductCompound(request.getProductCompound());
        product.setProductPrice(request.getProductPrice());
        product.setProductQuantity(request.getProductQuantity());

        Category category = new Category();
        category.setCategoryId(request.getCategoryId());
        product.setCategory(category);

        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setProductName(request.getProductName());
        product.setProductDescription(request.getProductDescription());
        product.setProductCompound(request.getProductCompound());
        product.setProductPrice(request.getProductPrice());
        product.setProductQuantity(request.getProductQuantity());
        product.setProductImg(request.getProductImg());
        product.setProductWeight(request.getProductWeight());

        if (request.getCategoryId() != null) {
            Category category = new Category();
            category.setCategoryId(request.getCategoryId());
            product.setCategory(category);
        }

        return productService.updateProduct(id, product);
    }

    @GetMapping(params = "ids")
    public List<Product> getProductsByIds(@RequestParam String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return productService.getProductsByIds(idList);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
    }
}
