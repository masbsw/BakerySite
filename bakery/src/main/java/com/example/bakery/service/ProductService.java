package com.example.bakery.service;


import com.example.bakery.dto.StockDecreaseItemRequest;
import com.example.bakery.dto.StockDecreaseItemResponse;
import com.example.bakery.dto.StockDecreaseResponse;
import com.example.bakery.exceptions.ProductStockConflictException;
import com.example.bakery.models.Product;
import com.example.bakery.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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

    @Transactional
    public StockDecreaseResponse decreaseStock(List<StockDecreaseItemRequest> items) {
        Map<Long, Integer> requestedQuantities = new LinkedHashMap<>();

        for (StockDecreaseItemRequest item : items) {
            requestedQuantities.merge(item.getProductId(), item.getQuantity(), Integer::sum);
        }

        List<Long> productIds = new ArrayList<>(requestedQuantities.keySet());
        List<Product> products = productRepository.findAllByProductIdInForUpdate(productIds);

        if (products.size() != productIds.size()) {
            List<Long> foundIds = products.stream().map(Product::getProductId).toList();
            Long missingId = productIds.stream()
                    .filter(productId -> !foundIds.contains(productId))
                    .findFirst()
                    .orElse(null);
            throw new ProductStockConflictException("Товар с id " + missingId + " не найден.");
        }

        Map<Long, Product> productsById = products.stream()
                .collect(LinkedHashMap::new, (map, product) -> map.put(product.getProductId(), product), Map::putAll);

        for (Map.Entry<Long, Integer> entry : requestedQuantities.entrySet()) {
            Product product = productsById.get(entry.getKey());
            int availableQuantity = product.getProductQuantity() == null ? 0 : product.getProductQuantity();
            int requestedQuantity = entry.getValue();

            if (availableQuantity < requestedQuantity) {
                throw new ProductStockConflictException(
                        "Недостаточно товара \"" + product.getProductName() + "\" на складе. Доступно: "
                                + availableQuantity + ", запрошено: " + requestedQuantity + "."
                );
            }
        }

        List<StockDecreaseItemResponse> responseItems = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : requestedQuantities.entrySet()) {
            Product product = productsById.get(entry.getKey());
            int remainingQuantity = product.getProductQuantity() - entry.getValue();
            product.setProductQuantity(remainingQuantity);
            responseItems.add(new StockDecreaseItemResponse(
                    product.getProductId(),
                    product.getProductName(),
                    entry.getValue(),
                    remainingQuantity
            ));
        }

        productRepository.saveAll(products);

        return new StockDecreaseResponse("Остатки успешно обновлены", responseItems);
    }
}
