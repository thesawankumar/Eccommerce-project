package com.lcwd.store.services.impl;

import com.lcwd.store.dtos.AddItemToCartRequest;
import com.lcwd.store.dtos.CartDto;
import com.lcwd.store.entities.Cart;
import com.lcwd.store.entities.CartItem;
import com.lcwd.store.entities.Product;
import com.lcwd.store.entities.User;
import com.lcwd.store.exceptions.BadApiRequest;
import com.lcwd.store.exceptions.ResourceNotFoundException;
import com.lcwd.store.repositories.CartItemRepository;
import com.lcwd.store.repositories.CartRepository;
import com.lcwd.store.repositories.ProductRepository;
import com.lcwd.store.repositories.UserRepository;
import com.lcwd.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
        int quantity = request.getQuantity();
        String productId = request.getProductId();
        
        if(quantity<=0)
        {
            throw new BadApiRequest("Request quantity not found");
        }
        //fetch the product
        Product product = productRepository.findById(productId).orElseThrow(() ->
                new ResourceNotFoundException("Product not found in db !!"));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found in db !!"));
        
        Cart cart = null;
        try
        {
            cart=cartRepository.findByUser(user).get();
            
        }catch (NoSuchElementException e)
        {
            cart=new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }
        //cart operations
        AtomicBoolean updated= new AtomicBoolean(false);
        List<CartItem> items = cart.getItems();
        items = items.stream().map(item ->
        {
            if (item.getProduct().equals(productId)) {
                //item already present in cart
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());
        
//        cart.setItems(updatedItems);
        //create items
     if(!updated.get())
     {
         CartItem cartItem = CartItem.builder().
                 quantity(quantity).
                 totalPrice(quantity * product.getDiscountedPrice()).cart(cart).
                 product(product).build();
         cart.getItems().add(cartItem);
     }
        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart,CartDto.class);
    }
    
    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        //conditions
        
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() ->
                new ResourceNotFoundException("Cart item not found"));
        cartItemRepository.delete(cartItem1);
    }
    
    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("UserId not found in db !!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() ->
                new ResourceNotFoundException("Cart of User not found in db !!"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }
    
    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("UserId not found in db !!"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() ->
                new ResourceNotFoundException("Cart of User not found in db !!"));
        return mapper.map(cart,CartDto.class);
    }
}
