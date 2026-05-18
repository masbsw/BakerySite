package com.example.bakery.controllers;

import com.example.bakery.models.Category;
import com.example.bakery.models.Product;
import com.example.bakery.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getAllProductsReturnsOkWhenNumericFieldsArePresent() throws Exception {
        Category category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("Bread");

        Product product = new Product();
        product.setProductId(10L);
        product.setProductName("Baguette");
        product.setProductDescription("Fresh bread");
        product.setProductCompound("Flour, water");
        product.setProductPrice(120.0f);
        product.setProductWeight(0.35f);
        product.setProductQuantity(7);
        product.setProductImg("/img/baguette.png");
        product.setCategory(category);

        given(productService.getAllProducts()).willReturn(List.of(product));

        mockMvc.perform(get("/api/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(10))
                .andExpect(jsonPath("$[0].productPrice").value(120.0))
                .andExpect(jsonPath("$[0].productWeight").value(0.35))
                .andExpect(jsonPath("$[0].productQuantity").value(7));
    }

    @Test
    void getAllProductsDoesNotFailWhenLegacyNumericFieldsAreNull() throws Exception {
        Product product = new Product();
        product.setProductId(11L);
        product.setProductName("Legacy cake");
        product.setProductPrice(null);
        product.setProductWeight(null);
        product.setProductQuantity(null);

        given(productService.getAllProducts()).willReturn(List.of(product));

        mockMvc.perform(get("/api/products").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(11))
                .andExpect(jsonPath("$[0].productPrice").isEmpty())
                .andExpect(jsonPath("$[0].productWeight").isEmpty())
                .andExpect(jsonPath("$[0].productQuantity").isEmpty());
    }
}
