package com.example.CartBakery.services;


import com.example.CartBakery.models.CartItem;
import com.example.CartBakery.repositories.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    public CartItem addItemToCart(CartItem cartItem) {
        CartItem existingItem = cartItemRepository.findByItemSessionIdAndProductId(
                cartItem.getItemSessionId(),
                cartItem.getProductId()
        );

        if (existingItem != null) {
            existingItem.setItemQuantity(existingItem.getItemQuantity() + cartItem.getItemQuantity());
            return cartItemRepository.save(existingItem);
        } else {
            return cartItemRepository.save(cartItem);
        }
    }

    public Optional<CartItem> getCartItemById(Long id){
        return cartItemRepository.findById(id);
    }

    public List<CartItem> getCart(String itemSessionId){
        return cartItemRepository.findByItemSessionId(itemSessionId);
    }


    @Transactional
    public void clearCart(String itemSessionId){
        cartItemRepository.deleteByItemSessionId(itemSessionId);
    }

    @Transactional
    public void deleteItemCart(String itemSessionId, Long productId){
        cartItemRepository.deleteByItemSessionIdAndProductId(itemSessionId, productId);
    }

    @Transactional
    public void decreaseQuantity(String itemSessionId, Long productId) {
        CartItem item = cartItemRepository.findByItemSessionIdAndProductId(itemSessionId, productId);

        if (item != null) {
            if (item.getItemQuantity() > 1) {
                item.setItemQuantity(item.getItemQuantity() - 1);
                cartItemRepository.save(item);
            } else {
                cartItemRepository.delete(item);

            }
        }
    }


}
