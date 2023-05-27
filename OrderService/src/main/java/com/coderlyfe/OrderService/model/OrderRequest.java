package com.coderlyfe.OrderService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private long orderId;
    private long amount;
    private long quantity;
    private PaymentMode paymentMode;
    private long productId;
}
