package com.coderlyfe.PaymentService.service;

import com.coderlyfe.PaymentService.model.PaymentRequest;
import com.coderlyfe.PaymentService.model.PaymentResponse;
import org.springframework.http.HttpStatus;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(String orderId);
}
