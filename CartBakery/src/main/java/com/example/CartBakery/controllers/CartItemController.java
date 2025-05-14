package com.example.CartBakery.controllers;


import com.example.CartBakery.models.CartItem;
import com.example.CartBakery.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/session/{itemSessionId}")
    public List<CartItem> getCart(@PathVariable String itemSessionId) {
        return cartItemService.getCart(itemSessionId);
    }

    @GetMapping("/item/{id}")
    public Optional<CartItem> getCartItemById(@PathVariable Long id){
        return cartItemService.getCartItemById(id);
    }

    @PostMapping
    public CartItem addItemToCart(@RequestBody CartItem cartItem) {
        return cartItemService.addItemToCart(cartItem);
    }

    @DeleteMapping("/clear/{itemSessionId}")
    public void clearCart(@PathVariable String itemSessionId) {
        cartItemService.clearCart(itemSessionId);
    }

    @DeleteMapping("/{itemSessionId}/{productId}")
    public void deleteItemCart(@PathVariable String itemSessionId,
                               @PathVariable Long productId) {
        cartItemService.deleteItemCart(itemSessionId, productId);
    }

    @PatchMapping("/decrease/{itemSessionId}/{productId}")
    public void decreaseQuantity(@PathVariable String itemSessionId,
                                 @PathVariable Long productId) {
        cartItemService.decreaseQuantity(itemSessionId, productId);
    }


}
