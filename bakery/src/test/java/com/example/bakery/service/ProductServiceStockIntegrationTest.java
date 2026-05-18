package com.example.bakery.service;

import com.example.bakery.dto.StockDecreaseItemRequest;
import com.example.bakery.dto.StockDecreaseResponse;
import com.example.bakery.exceptions.ProductStockConflictException;
import com.example.bakery.models.Product;
import com.example.bakery.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ProductServiceStockIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
    }

    @Test
    void decreaseStockReducesQuantitiesWhenAllItemsAreAvailable() {
        Product croissant = productRepository.save(buildProduct("Круассан", 8));
        Product tart = productRepository.save(buildProduct("Тарт", 5));

        StockDecreaseResponse response = productService.decreaseStock(List.of(
                buildItem(croissant.getProductId(), 3),
                buildItem(tart.getProductId(), 2)
        ));

        assertEquals("Остатки успешно обновлены", response.getMessage());
        assertEquals(2, response.getItems().size());
        assertEquals(5, productRepository.findById(croissant.getProductId()).orElseThrow().getProductQuantity());
        assertEquals(3, productRepository.findById(tart.getProductId()).orElseThrow().getProductQuantity());
    }

    @Test
    void decreaseStockDoesNotPartiallyUpdateQuantitiesWhenAnyItemIsInsufficient() {
        Product bread = productRepository.save(buildProduct("Хлеб", 4));
        Product pie = productRepository.save(buildProduct("Пирог", 1));

        assertThrows(ProductStockConflictException.class, () -> productService.decreaseStock(List.of(
                buildItem(bread.getProductId(), 2),
                buildItem(pie.getProductId(), 3)
        )));

        assertEquals(4, productRepository.findById(bread.getProductId()).orElseThrow().getProductQuantity());
        assertEquals(1, productRepository.findById(pie.getProductId()).orElseThrow().getProductQuantity());
    }

    private Product buildProduct(String name, int quantity) {
        Product product = new Product();
        product.setProductName(name);
        product.setProductDescription(name + " description");
        product.setProductCompound("test");
        product.setProductPrice(100.0f);
        product.setProductWeight(0.5f);
        product.setProductQuantity(quantity);
        product.setProductImg("/img/test.png");
        return product;
    }

    private StockDecreaseItemRequest buildItem(Long productId, int quantity) {
        StockDecreaseItemRequest item = new StockDecreaseItemRequest();
        item.setProductId(productId);
        item.setQuantity(quantity);
        return item;
    }
}
