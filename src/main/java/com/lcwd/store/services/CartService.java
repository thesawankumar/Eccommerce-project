package com.lcwd.store.services;

import com.lcwd.store.dtos.AddItemToCartRequest;
import com.lcwd.store.dtos.CartDto;

public interface CartService {
    //add items to cart:
    // case1: cart for user is not available
    //case2: cart available add the item to cart
    CartDto addItemToCart(String userId, AddItemToCartRequest request);
    
    //remove item from cart
    void removeItemFromCart(String userId, int cartItem);
    
    //clear cart
    void clearCart(String userId);
    
    CartDto getCartByUser(String userId);
}
