package com.example.CartBakery.repositories;

import com.example.CartBakery.models.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByItemSessionId(String itemSessionId);
    CartItem findByItemSessionIdAndProductId(String itemSessionId, Long productId);
    void deleteByItemSessionIdAndProductId(String itemSessionId, Long productId);
    void deleteByItemSessionId(String itemSessionId);
}
