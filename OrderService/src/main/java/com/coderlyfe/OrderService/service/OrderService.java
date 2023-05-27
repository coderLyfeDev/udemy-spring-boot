package com.coderlyfe.OrderService.service;

import com.coderlyfe.OrderService.model.OrderRequest;
import com.coderlyfe.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
