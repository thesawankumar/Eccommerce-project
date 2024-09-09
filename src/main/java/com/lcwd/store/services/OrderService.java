package com.lcwd.store.services;


import com.lcwd.store.dtos.CreateOrderRequest;
import com.lcwd.store.dtos.OrderDto;
import com.lcwd.store.dtos.PageableResponse;

import java.util.List;

public interface OrderService  {

    //create order
    OrderDto createOrder(CreateOrderRequest orderDto);
    
    //remove order
    void removeOrder(String orderId);
    
    
    //get orders of user
    List<OrderDto> getOrderOfUser(String userId);
    
    //get orders
    PageableResponse<OrderDto> getOrders(int pageNumber,int pageSize,String sortBy,String sortDir);
    //order logic related to order
}
